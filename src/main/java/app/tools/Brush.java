package app.tools;

import javafx.scene.canvas.GraphicsContext;

public abstract class Brush {

    protected final DrawTool tool;

    public Brush(DrawTool tool) {
        this.tool = tool;
    }

    public abstract double getWidth();
    public abstract double getHeight();
    public abstract void draw(GraphicsContext context, double x, double y);

}
