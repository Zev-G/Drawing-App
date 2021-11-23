package app;

import com.me.tmw.debug.util.Debugger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    private static final double SIZE = 500;

    private final Map<Plot, PlotCanvas> canvasMap = new HashMap<>();
    
    private final DoubleProperty xOffset = new SimpleDoubleProperty();
    private final DoubleProperty yOffset = new SimpleDoubleProperty();
    
    private double startX;
    private double startY;
    private double xOffsetI;
    private double yOffsetI;
    private boolean dragging = false;
    private double lastDrawX;
    private double lastDrawY;

    private DoubleProperty brushSize = new SimpleDoubleProperty(5);

    private StringProperty debug = new SimpleStringProperty();

    public InfiniDraw() {
        setOnMousePressed(event -> {
            dragging = true;
            startX = event.getX();
            startY = event.getY();
            xOffsetI = getXOffset();
            yOffsetI = getYOffset();
            lastDrawX = startX;
            lastDrawY = startY;
        });
        Debugger.showProperty(debug, this);
        setOnScroll(scrollEvent -> {
            if (scrollEvent.isControlDown()) {
                int mult = scrollEvent.getDeltaY() > 0 ? 1 : -1;
                setScaleX(getScaleX() + (
                        0.1 * mult
                ));
                setScaleY(getScaleY() + (
                        0.1 * mult
                ));
            } else {
//                if (scrollEvent.getDeltaY() > 0) {
//                    brushSize.set(brushSize.get() + 1);
//                } else if (brushSize.get() > 1) {
//                    brushSize.set(brushSize.get() - 1);
//                }
            }
        });
        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY && !event.isShiftDown()) {
                double x = event.getX();
                double y = event.getY();
                drawBetween(lastDrawX, lastDrawY, x, y, brushSize.get(), brushSize.get());
//                draw(x, y, brushSize.get(), brushSize.get());
                lastDrawX = x;
                lastDrawY = y;
            } else if (dragging) {
                double deltaX = event.getX() - startX;
                double deltaY = event.getY() - startY;
                setXOffset(deltaX + xOffsetI);
                setYOffset(deltaY + yOffsetI);
            }
        });
        setOnMouseReleased(event -> dragging = false);
    }

    private void drawBetween(double xOld, double yOld, double x, double y, double w, double h) {
        double step = Math.sqrt(Math.pow(w / 3, 2) + Math.pow(h / 3, 2));
        double hyp = Math.sqrt(Math.pow(x - xOld, 2) + Math.pow(y - yOld, 2));
        if (hyp > step) {
            double deg = Math.atan((x - xOld) / (y - yOld));

            boolean xNeg = x - xOld < 0;
            boolean yNeg = y - yOld < 0;

            double xStep = Math.sin(deg) * step;
            if ((xNeg && yNeg) || (!xNeg && yNeg)) xStep *= -1;
            double yStep = Math.cos(deg) * step;
            if (yNeg) yStep *= -1;

            double newX = xOld;
            double newY = yOld;
            do {
                newX += xStep;
                newY += yStep;
                draw(newX, newY, w, h);
                hyp = Math.sqrt(Math.pow(x - newX, 2) + Math.pow(y - newY, 2));
            } while (hyp > step);
        } else {
            draw(x, y, w, h);
        }
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
        canvas.getGraphicsContext2D().fillRect(
                relativeToCanvasX, relativeToCanvasY,
                w, h
        );
    }

    private PlotCanvas find(double x, double y) {
        int plotX = (int) ((x - getXOffset()) / SIZE);
        int plotY = (int) ((y - getYOffset()) / SIZE);
        if (x - getXOffset() < 0) plotX--;
        if (y - getYOffset() < 0) plotY--;

        Plot plot = new Plot(plotX, plotY);
        if (canvasMap.containsKey(plot)) return canvasMap.get(plot);

        PlotCanvas canvas = new PlotCanvas(SIZE, SIZE, this);
        canvas.setX(plotX * SIZE);
        canvas.setY(plotY * SIZE);
        canvas.getGraphicsContext2D().setFill(Color.WHITE);
        canvas.getGraphicsContext2D().fillRect(0, 0, SIZE, SIZE);
        canvas.getGraphicsContext2D().setFill(Color.web("#7a362f"));
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
