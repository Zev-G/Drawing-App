package app;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;

public class PlotCanvas extends Canvas {

    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final InfiniDraw infiniDraw;

    public PlotCanvas(double w, double h, InfiniDraw draw) {
        super(w, h);
        this.infiniDraw = draw;

        layoutXProperty().bind(x.add(infiniDraw.xOffsetProperty()));
        layoutYProperty().bind(y.add(infiniDraw.yOffsetProperty()));
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

}
