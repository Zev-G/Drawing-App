package app;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InfiniDraw extends Pane {

    private static final double SIZE = 20;

    private final Map<Plot, PlotCanvas> canvasMap = new HashMap<>();
    
    private final DoubleProperty xOffset = new SimpleDoubleProperty();
    private final DoubleProperty yOffset = new SimpleDoubleProperty();
    
    private double startX;
    private double startY;
    private double xOffsetI;
    private double yOffsetI;
    private boolean dragging = false;

    public InfiniDraw() {
        setOnMousePressed(event -> {
            dragging = true;
            startX = event.getX();
            startY = event.getY();
            xOffsetI = getXOffset();
            yOffsetI = getYOffset();
        });
        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                draw(event.getX(), event.getY(), 6, 6);
            } else if (dragging) {
                double deltaX = event.getX() - startX;
                double deltaY = event.getY() - startY;
                setXOffset(deltaX + xOffsetI);
                setYOffset(deltaY + yOffsetI);
            }
        });
        setOnMouseReleased(event -> dragging = false);
    }

    private void draw(double x, double y, double w, double h) {
        PlotCanvas topLeft = find(x - w, y - h);
        PlotCanvas topRight = find(x + w, y - h);
        PlotCanvas bottomLeft = find(x - w, y + h);
        PlotCanvas bottomRight = find(x + w, y + h);
        Set<PlotCanvas> unique = new HashSet<>();
        unique.add(topLeft);
        unique.add(topRight);
        unique.add(bottomLeft);
        unique.add(bottomRight);
        for (PlotCanvas canvas : unique) {
            drawRelative(canvas, x, y, w, h);
        }
    }

    private void drawRelative(PlotCanvas canvas, double x, double y, double w, double h) {
        double relativeToCanvasX = x - canvas.getLayoutX();
        double relativeToCanvasY = y - canvas.getLayoutY();
        canvas.getGraphicsContext2D().fillOval(
                relativeToCanvasX, relativeToCanvasY,
                w, h
        );
    }

    private PlotCanvas find(double x, double y) {
        int plotX = (int) ((x - getXOffset()) / SIZE);
        int plotY = (int) ((y - getYOffset()) / SIZE);

        Plot plot = new Plot(plotX, plotY);
        if (canvasMap.containsKey(plot)) return canvasMap.get(plot);

        PlotCanvas canvas = new PlotCanvas(SIZE, SIZE, this);
        canvas.setX(plotX * SIZE);
        canvas.setY(plotY * SIZE);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, SIZE, SIZE);
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        getChildren().add(canvas);
        canvasMap.put(plot, canvas);

        return canvas;
    }

    public Map<Plot, PlotCanvas> getCanvasMap() {
        return canvasMap;
    }

    public double getXOffset() {
        return xOffset.get();
    }

    public DoubleProperty xOffsetProperty() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset.set(xOffset);
    }

    public double getYOffset() {
        return yOffset.get();
    }

    public DoubleProperty yOffsetProperty() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset.set(yOffset);
    }

}
