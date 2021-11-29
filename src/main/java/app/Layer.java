package app;

import com.me.tmw.nodes.util.NodeMisc;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;

public interface Layer {

    Node getView();

    ObjectProperty<Drawing> drawingProperty();
    default void setDrawing(Drawing drawing) { this.drawingProperty().set(drawing);}
    default Drawing getDrawing() { return this.drawingProperty().get(); }

    BooleanProperty selectedProperty();
    default void setSelected(boolean selected) { this.selectedProperty().set(selected); }
    default boolean isSelected() { return this.selectedProperty().get(); }

}
