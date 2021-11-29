package app;

import com.me.tmw.nodes.util.NodeMisc;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LayersEditor extends VBox {

    private static final String STYLE_SHEET = Res.css("layers-editor");
    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private final VBox layersView = new VBox();
    private final Button add = new Button("Add");
    private final HBox buttons = new HBox(add);

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
            }
        });

        add.setOnAction(actionEvent -> drawing.getLayers().add(new PaintLayer(drawing)));

        getChildren().addAll(layersView, buttons);
    }

    private void added(Layer layer, int added) {
        if (added < 0) added = drawing.getLayers().size() - 1;
        added = (drawing.getLayers().size() - 1) - added;

        LayerView view = new LayerView(layer);
        layersView.getChildren().add(added, view);
    }

    private static class LayerView extends HBox {

        private final Layer layer;

        private final Label name = new Label();
        private final CheckBox visible = new CheckBox();

        private LayerView(Layer layer) {
            this.layer = layer;

            getStyleClass().add("layer-view");

            Node view = layer.getView();
            if (view.visibleProperty().isBound()) view.visibleProperty().unbind();
            visible.setSelected(view.isVisible());
            view.visibleProperty().bind(visible.selectedProperty());

            NodeMisc.runAndAddListener(layer.selectedProperty(), observable -> pseudoClassStateChanged(SELECTED, layer.isSelected()));

            setOnMouseEntered(mouseEvent -> {
                for (Layer other : layer.getDrawing().getLayers()) {
                    if (other != layer) {
                        other.getView().setOpacity(0.5);
                    }
                }
            });
            setOnMouseExited(mouseEvent -> {
                for (Layer other : layer.getDrawing().getLayers()) {
                    other.getView().setOpacity(1);
                }
            });
            setOnMousePressed(mouseEvent -> layer.getDrawing().getLayerSelectionModel().select(layer));

            name.setText(layer.getClass().getSimpleName());

            getChildren().addAll(name, visible);
        }
    }

}
