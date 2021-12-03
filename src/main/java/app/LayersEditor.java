package app;

import app.misc.FlatButton;
import com.me.tmw.nodes.control.svg.SVG;
import com.me.tmw.nodes.util.NodeMisc;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LayersEditor extends VBox {

    private static final String STYLE_SHEET = Res.css("layers-editor");
    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private final VBox layersView = new VBox();
    private final Button add = new Button("Add");
    private final HBox buttons = new HBox(add);

    private final Map<Layer, LayerView> layerViewMap = new HashMap<>();

    private LayerView lastHovered;

    private final Drawing drawing;

    public LayersEditor(Drawing drawing) {
        this.drawing = drawing;

        getStylesheets().add(STYLE_SHEET);
        getStyleClass().add("layers-editor");
        layersView.getStyleClass().add("layers-view");

        drawing.getLayers().addListener((ListChangeListener<Layer>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Layer added : change.getAddedSubList()) {
                        added(added, drawing.getLayers().indexOf(added));
                    }
                }
                if (change.wasRemoved()) {
                    for (Layer removed : change.getRemoved()) {
                        remove(removed);
                    }
                }
            }
        });

        add.setOnAction(actionEvent -> drawing.getLayers().add(new PaintLayer(drawing)));

        getChildren().addAll(layersView, buttons);
    }

    private void added(Layer layer, int added) {
        if (added < 0) added = drawing.getLayers().size() - 1;
        added = (drawing.getLayers().size() - 1) - added;

        LayerView view = layerViewMap.computeIfAbsent(layer, LayerView::new);
        if (view.markedForMove) {
            ObservableList<Node> nodes = FXCollections.observableArrayList(layersView.getChildren());
            nodes.remove(view);
            nodes.add(added, view);
            layersView.getChildren().setAll(nodes);
        } else {
            layersView.getChildren().add(added, view);
        }
    }

    private void remove(Layer layer) {
        LayerView view = layerViewMap.get(layer);
        if (view != null && !view.markedForMove) layersView.getChildren().remove(view);
    }

    private class LayerView extends HBox {

        private final Layer layer;

        private final SVGPath viewPath = NodeMisc.svgPath(
                SVG.resizePath("M 15 12 c 0 1.654 -1.346 3 -3 3 s -3 -1.346 -3 -3 s 1.346 -3 3 -3 s 3 1.346 3 3 z m 9 -0.449 s -4.252 8.449 -11.985 8.449 c -7.18 0 -12.015 -8.449 -12.015 -8.449 s 4.446 -7.551 12.015 -7.551 c 7.694 0 11.985 7.551 11.985 7.551 z m -7 0.449 c 0 -2.757 -2.243 -5 -5 -5 s -5 2.243 -5 5 s 2.243 5 5 5 s 5 -2.243 5 -5 z",
                        0.75)
        );
        private final FlatButton view = new FlatButton("", viewPath);
        private final Label name = new Label();
        private final CheckBox visible = new CheckBox();

        private boolean markedForMove = false;

        private LayerView(Layer layer) {
            this.layer = layer;

            getStyleClass().add("layer-view");

            Node view = layer.getView();
            if (view.visibleProperty().isBound()) view.visibleProperty().unbind();
            visible.setSelected(view.isVisible());
            view.visibleProperty().bind(visible.selectedProperty());

            NodeMisc.runAndAddListener(layer.selectedProperty(), observable -> pseudoClassStateChanged(SELECTED, layer.isSelected()));

            viewPath.setOnMouseEntered(mouseEvent -> {
                for (Layer other : layer.getDrawing().getLayers()) {
                    if (other != layer) {
                        other.getView().setOpacity(0.05);
                    }
                }
            });
            viewPath.setOnMouseExited(mouseEvent -> {
                for (Layer other : layer.getDrawing().getLayers()) {
                    other.getView().setOpacity(1);
                }
            });

            setOnMouseDragged(mouseEvent -> {
                Optional<LayerView> hovered = findForChild(mouseEvent.getPickResult().getIntersectedNode());
                if (hovered.isPresent() && hovered.get() != this) {
                    markedForMove = true;
                    int index = drawing.getLayers().indexOf(layer);
                    drawing.getLayers().remove(layer);
                    int indexHover = drawing.getLayers().indexOf(hovered.get().layer);
                    if (index <= indexHover) indexHover++;
                    drawing.getLayers().add(indexHover, layer);
                    markedForMove = false;
                }
            });

            viewPath.setPickOnBounds(true);
            viewPath.getStyleClass().add("view-svg");

            setOnMousePressed(mouseEvent -> layer.getDrawing().getLayerSelectionModel().select(layer));

            name.setText(layer.getClass().getSimpleName());

            getChildren().addAll(this.view, name, visible);
        }
    }

    private Optional<LayerView> findForChild(Node node) {
        if (node instanceof LayerView) return Optional.of((LayerView) node);
        if (node == null) return Optional.empty();
        return findForChild(node.getParent());
    }

}
