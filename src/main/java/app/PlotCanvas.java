package app;

import com.me.tmw.nodes.util.NodeMisc;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.SnapshotResult;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.Stack;

public class PlotCanvas extends Canvas {

    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final InfiniDraw infiniDraw;

    private final Stack<Image> history = new Stack<>();

    public PlotCanvas(double w, double h, InfiniDraw draw) {
        super(w, h);
        this.infiniDraw = draw;

        history.add(blankImage((int) Math.round(w), (int) Math.round(h)));

        layoutXProperty().bind(x.add(infiniDraw.xOffsetProperty()));
        layoutYProperty().bind(y.add(infiniDraw.yOffsetProperty()));
    }

    private Image blankImage(int w, int h) {
        WritableImage image = new WritableImage(w, h);
        PixelWriter writer = image.getPixelWriter();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                writer.setColor(x, y, Color.TRANSPARENT);
            }
        }
        return image;
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

    public InfiniDraw getInfiniDraw() {
        return infiniDraw;
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
