package app.tools;

import app.EditableProperty;
import app.Drawing;
import com.me.tmw.properties.NodeProperty;
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
    protected final NodeProperty cursor = new NodeProperty((Node) null);

    public Tool(Drawing drawing) {
        this.drawing = drawing;
    }

    public void handleMousePressed(MouseEvent event) {}
    public void handleMouseReleased(MouseEvent event) {}
    public void handleMouseDragged(MouseEvent event) {}
    public boolean isDraggable(MouseEvent event) { return false; }

    public Node getCursor() {
        return cursor.get();
    }

    public NodeProperty cursorProperty() {
        return cursor;
    }

    public void setCursor(Node cursor) {
        this.cursor.set(cursor);
    }

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
