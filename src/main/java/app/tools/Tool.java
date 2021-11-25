package app.tools;

import app.EditableProperty;
import app.Drawing;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public abstract class Tool {

    protected final Drawing drawing;
    protected final BooleanProperty selected = new SimpleBooleanProperty();
    protected final ObservableList<EditableProperty<?>> editableProperties = FXCollections.observableArrayList();

    public Tool(Drawing drawing) {
        this.drawing = drawing;
    }

    public void handleMousePressed(MouseEvent event) {}
    public void handleMouseReleased(MouseEvent event) {}
    public void handleMouseDragged(MouseEvent event) {}
    public boolean isDraggable(MouseEvent event) { return false; }

    public Node getCursor() { return null; }

    public abstract Node getIcon();

    public Drawing getDrawing() {
        return drawing;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public ObservableList<EditableProperty<?>> getEditableProperties() {
        return editableProperties;
    }

}
