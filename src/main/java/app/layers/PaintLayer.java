package app.layers;

import app.Drawing;
import app.Plot;
import app.PlotCanvas;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class PaintLayer extends LayerBase {

    private static final boolean WHITE_CANVAS_BG = false;
    private static final double SIZE = 500;

    private final Map<Plot, PlotCanvas> canvasMap = new HashMap<>();
    private final Drawing drawing;

    private final Pane pane = new Pane();

    public PaintLayer(Drawing drawing) {
        this.drawing = drawing;
        pane.setSnapToPixel(false);
    }

    @Override
    public Node getView() {
        return pane;
    }

    public PlotCanvas find(double x, double y) {
        int plotX = (int) ((x - drawing.getXOffset()) / SIZE);
        int plotY = (int) ((y - drawing.getYOffset()) / SIZE);
        if (x - drawing.getXOffset() < 0) plotX--;
        if (y - drawing.getYOffset() < 0) plotY--;

        Plot plot = new Plot(plotX, plotY);
        if (canvasMap.containsKey(plot)) return canvasMap.get(plot);

        PlotCanvas canvas = new PlotCanvas(SIZE + 1, SIZE + 1, drawing);
        canvas.setX(plotX * SIZE);
        canvas.setY(plotY * SIZE);
        if (WHITE_CANVAS_BG) {
            canvas.getGraphicsContext2D().setFill(Color.WHITE);
            canvas.getGraphicsContext2D().fillRect(0, 0, SIZE, SIZE);
            canvas.clearHistory();
            canvas.pushToHistory();
        }
        canvas.getGraphicsContext2D().setFill(Color.web("#7a362f"));
        pane.getChildren().add(canvas);
        canvasMap.put(plot, canvas);

        return canvas;
    }

    public Map<Plot, PlotCanvas> getCanvasMap() {
        return canvasMap;
    }

}
