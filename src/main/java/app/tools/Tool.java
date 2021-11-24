package app.tools;

import app.InfiniDraw;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public abstract class Tool {

    protected final InfiniDraw drawing;
    protected BooleanProperty selected = new SimpleBooleanProperty();

    public Tool(InfiniDraw drawing) {
        this.drawing = drawing;
    }

    public void handleMousePressed(MouseEvent event) {}
    public void handleMouseReleased(MouseEvent event) {}
    public void handleMouseDragged(MouseEvent event) {}
    public boolean isDraggable(MouseEvent event) { return false; }

    public abstract Node getIcon();

    public InfiniDraw getDrawing() {
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
}
