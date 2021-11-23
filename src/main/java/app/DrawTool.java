package app;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

public class DrawTool extends IconPreviewTool {

    private DoubleProperty brushSize = new SimpleDoubleProperty(5);
    private BooleanProperty currentlyDrawing = new SimpleBooleanProperty(false);
    private Set<PlotCanvas> effectedCanvases = new HashSet<>();

    private double lastDrawX;
    private double lastDrawY;

    public DrawTool(InfiniDraw drawing) {
        super(drawing);

        icon.setContent("M4 0l16 12.279-6.951 1.17 4.325 8.817-3.596 1.734-4.35-8.879-5.428 4.702z");
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
        PlotCanvas topLeft = drawing.find(x - w, y - h);
        PlotCanvas topRight = drawing.find(x + w, y - h);
        PlotCanvas bottomLeft = drawing.find(x - w, y + h);
        PlotCanvas bottomRight = drawing.find(x + w, y + h);
        Set<PlotCanvas> unique = new HashSet<>();
        unique.add(topLeft);
        unique.add(topRight);
        unique.add(bottomLeft);
        unique.add(bottomRight);
        for (PlotCanvas canvas : unique) {
            drawRelative(canvas, x, y, w, h);
            if (currentlyDrawing.get()) {
                effectedCanvases.add(canvas);
            }
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

    @Override
    public void handleMousePressed(MouseEvent event) {
        currentlyDrawing.set(true);
        effectedCanvases = new HashSet<>();
        lastDrawX = event.getX();
        lastDrawY = event.getY();
    }

    @Override
    public void handleMouseDragged(MouseEvent event) {
        if (!isDraggable(event)) {
            double x = event.getX();
            double y = event.getY();
            drawBetween(lastDrawX, lastDrawY, x, y, brushSize.get(), brushSize.get());
//                draw(x, y, brushSize.get(), brushSize.get());
            lastDrawX = x;
            lastDrawY = y;
        }
    }

    @Override
    public void handleMouseReleased(MouseEvent event) {
        drawing.getHistory().add(new CanvasEdit(effectedCanvases));
        currentlyDrawing.set(false);
    }

    @Override
    public boolean isDraggable(MouseEvent event) {
        return event.getButton() != MouseButton.PRIMARY || event.isShiftDown();
    }
}
