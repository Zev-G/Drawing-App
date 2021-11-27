package app;

import app.misc.EffectPassThroughPane;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LayersEditor extends StackPane {

    private static final String STYLE_SHEET = Res.css("layers-editor");

    private final VBox layersView = new VBox();

    public LayersEditor(Drawing drawing) {
        getStylesheets().add(STYLE_SHEET);
        getStyleClass().add("layers-editor");
        layersView.getStyleClass().add("layers-view");

        drawing.getLayers().addListener((ListChangeListener<Layer>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Layer added : change.getAddedSubList()) {
                        added(added, -1);
                    }
                }
            }
        });

        getChildren().add(layersView);
    }

    private void added(Layer layer, int added) {
        LayerView view = new LayerView(layer);
        if (added < 0) layersView.getChildren().add(view);
        else layersView.getChildren().add(added, view);
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

            name.setText(layer.getClass().getSimpleName());

            getChildren().addAll(visible, name);
        }
    }

}
