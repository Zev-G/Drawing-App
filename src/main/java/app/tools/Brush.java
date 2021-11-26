package app.tools;

import com.me.tmw.properties.NodeProperty;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

public abstract class Brush {

    protected final NodeProperty cursor = new NodeProperty((Node) null);
    protected final DrawTool tool;

    public Brush(DrawTool tool) {
        this.tool = tool;
    }

    public abstract double getWidth();
    public abstract double getHeight();
    public abstract void draw(GraphicsContext context, double x, double y);

    public Node getCursor() {
        return cursor.get();
    }

    public NodeProperty cursorProperty() {
        return cursor;
    }

    public void setCursor(Node cursor) {
        this.cursor.set(cursor);
    }

}
