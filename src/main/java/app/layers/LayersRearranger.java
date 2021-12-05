package app.layers;

import com.me.tmw.listeners.schedulers.ConsumerEventScheduler;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class LayersRearranger extends VBox {

    private final ObservableList<Layer> layers;
    private final Map<Layer, LayerView> layerViewMap = new HashMap<>();

    private boolean rearranging = false;

    public LayersRearranger(ObservableList<Layer> layers) {
        this.layers = layers;

        getStyleClass().add("layers-rearranger");

        layers.addListener((ListChangeListener<Layer>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Layer added : change.getAddedSubList()) {
                        added(added, layers.indexOf(added));
                    }
                }
                if (change.wasRemoved()) {
                    for (Layer removed : change.getRemoved()) {
                        remove(removed);
                    }
                }
            }
        });
    }

    private void added(Layer layer, int added) {
        if (added < 0) added = layers.size() - 1;
        added = (layers.size() - 1) - added;

        LayerView view = layerViewMap.computeIfAbsent(layer, lay -> new LayerView(layer, this));
        if (view.isMarkedForMove()) {
            ObservableList<Node> nodes = FXCollections.observableArrayList(getChildren());
            nodes.remove(view);
            nodes.add(added, view);
            setRearranging(true);
            getChildren().setAll(nodes);
//            setRearranging(false);
        } else {
            getChildren().add(added, view);
        }
    }

    private void remove(Layer layer) {
        LayerView view = layerViewMap.get(layer);
        if (view != null && !view.isMarkedForMove()) getChildren().remove(view);
    }

    public ObservableList<Layer> getLayers() {
        return layers;
    }

    public Map<Layer, LayerView> getLayerViewMap() {
        return layerViewMap;
    }

    public boolean isRearranging() {
        return rearranging;
    }

    public void setRearranging(boolean rearranging) {
        this.rearranging = rearranging;
    }

}
