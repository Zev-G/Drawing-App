package app;

import com.me.tmw.nodes.util.NodeMisc;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.Stack;

public class PlotCanvas extends Canvas {

    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final Drawing drawing;

    private final Stack<Image> history = new Stack<>();

    public PlotCanvas(double w, double h, Drawing drawing) {
        super(w, h);
        this.drawing = drawing;

        layoutXProperty().bind(x.add(drawing.xOffsetProperty()));
        layoutYProperty().bind(y.add(drawing.yOffsetProperty()));
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public Stack<Image> getHistory() {
        return history;
    }

    public void pushToHistory() {
        snapshot(param -> {
            history.add(param.getImage());
            return null;
        }, NodeMisc.TRANSPARENT_SNAPSHOT_PARAMETERS, null);
    }

    public void clear() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    }

    public void loadImage(Image image) {
        getGraphicsContext2D().drawImage(image, 0, 0);
    }

    public void clearHistory() {
        history.clear();
    }
}
