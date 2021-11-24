package app.tools;

import javafx.scene.canvas.GraphicsContext;

public class CircleBrush extends Brush {

    public CircleBrush(DrawTool tool) {
        super(tool);
    }

    @Override
    public double getWidth() {
        return tool.getBrushSize();
    }

    @Override
    public double getHeight() {
        return tool.getBrushSize();
    }

    @Override
    public void draw(GraphicsContext context, double x, double y) {
        context.fillOval(x, y, tool.getBrushSize(), tool.getBrushSize());
    }

}
