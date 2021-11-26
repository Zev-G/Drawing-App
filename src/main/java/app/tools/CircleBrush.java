package app.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleBrush extends Brush {

    private final Circle circleCursor = new Circle();

    public CircleBrush(DrawTool tool) {
        super(tool);

        this.cursor.set(circleCursor);
        circleCursor.radiusProperty().bind(tool.brushSizeProperty().divide(2));
        circleCursor.setStrokeWidth(1.5);
        circleCursor.setStroke(Color.BLACK);
        circleCursor.setFill(Color.TRANSPARENT);;
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
