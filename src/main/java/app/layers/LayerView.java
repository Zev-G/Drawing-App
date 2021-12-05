package app.layers;

import app.misc.FlatButton;
import com.me.tmw.listeners.schedulers.ConsumerEventScheduler;
import com.me.tmw.nodes.control.svg.SVG;
import com.me.tmw.nodes.util.NodeMisc;
import javafx.animation.TranslateTransition;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

import java.util.Optional;

public class LayerView extends HBox {

    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private final Layer layer;

    private final SVGPath viewPath = NodeMisc.svgPath(
            SVG.resizePath("M 15 12 c 0 1.654 -1.346 3 -3 3 s -3 -1.346 -3 -3 s 1.346 -3 3 -3 s 3 1.346 3 3 z m 9 -0.449 s -4.252 8.449 -11.985 8.449 c -7.18 0 -12.015 -8.449 -12.015 -8.449 s 4.446 -7.551 12.015 -7.551 c 7.694 0 11.985 7.551 11.985 7.551 z m -7 0.449 c 0 -2.757 -2.243 -5 -5 -5 s -5 2.243 -5 5 s 2.243 5 5 5 s 5 -2.243 5 -5 z",
                    0.75)
    );
    private final FlatButton view = new FlatButton("", viewPath);
    private final Label name = new Label();
    private final CheckBox visible = new CheckBox();

    private boolean markedForMove = false;

    private final LayersRearranger rearranger;

    public LayerView(Layer layer, LayersRearranger rearranger) {
        this.layer = layer;
        this.rearranger = rearranger;

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
        this.view.setOnAction(event -> {
            visible.setSelected(true);
            for (Layer other : layer.getDrawing().getLayers()) {
                if (other != layer) {
                    LayerView otherView = rearranger.getLayerViewMap().get(other);
                    if (otherView != null) otherView.visible.setSelected(false);
                }
            }
        });

        setOnMouseDragged(mouseEvent -> {
            Optional<LayerView> hovered = findForChild(mouseEvent.getPickResult().getIntersectedNode());
            if (hovered.isPresent() && hovered.get() != this) {
                markedForMove = true;
                int index = rearranger.getLayers().indexOf(layer);
                rearranger.getLayers().remove(layer);
                int indexHover = rearranger.getLayers().indexOf(hovered.get().layer);
                if (index <= indexHover) indexHover++;
                rearranger.getLayers().add(indexHover, layer);
                markedForMove = false;
            }
        });

        viewPath.setPickOnBounds(true);
        viewPath.getStyleClass().add("view-svg");

        setOnMousePressed(mouseEvent -> layer.getDrawing().getLayerSelectionModel().select(layer));

        name.setText(layer.getClass().getSimpleName());

        getChildren().addAll(this.view, name, visible);
    }

    private Optional<LayerView> findForChild(Node node) {
        if (node instanceof LayerView) return Optional.of((LayerView) node);
        if (node == null) return Optional.empty();
        return findForChild(node.getParent());
    }

    public boolean isMarkedForMove() {
        return markedForMove;
    }

}