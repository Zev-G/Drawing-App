package app;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class PaintLayer extends Pane implements Layer {

    private static final boolean WHITE_CANVAS_BG = false;
    private static final double SIZE = 150;

    private final Map<Plot, PlotCanvas> canvasMap = new HashMap<>();
    private final InfiniDraw drawing;

    public PaintLayer(InfiniDraw drawing) {
        this.drawing = drawing;
    }

    @Override
    public Node getView() {
        return this;
    }

    public PlotCanvas find(double x, double y) {
        int plotX = (int) ((x - drawing.getXOffset()) / SIZE);
        int plotY = (int) ((y - drawing.getYOffset()) / SIZE);
        if (x - drawing.getXOffset() < 0) plotX--;
        if (y - drawing.getYOffset() < 0) plotY--;

        Plot plot = new Plot(plotX, plotY);
        if (canvasMap.containsKey(plot)) return canvasMap.get(plot);

        PlotCanvas canvas = new PlotCanvas(SIZE, SIZE, drawing);
        canvas.setX(plotX * SIZE);
        canvas.setY(plotY * SIZE);
        if (WHITE_CANVAS_BG) {
            canvas.getGraphicsContext2D().setFill(Color.WHITE);
            canvas.getGraphicsContext2D().fillRect(0, 0, SIZE, SIZE);
            canvas.clearHistory();
            canvas.pushToHistory();
        }
        canvas.getGraphicsContext2D().setFill(Color.web("#7a362f"));
        getChildren().add(canvas);
        canvasMap.put(plot, canvas);

        return canvas;
    }

    public Map<Plot, PlotCanvas> getCanvasMap() {
        return canvasMap;
    }

}
