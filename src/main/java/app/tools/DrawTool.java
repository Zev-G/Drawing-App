package app.tools;

import app.*;
import app.history.CanvasEdit;
import app.layers.Layer;
import app.layers.PaintLayer;
import com.me.tmw.nodes.control.svg.SVG;
import com.me.tmw.properties.ColorProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;

import java.util.HashSet;
import java.util.Set;

public class DrawTool extends IconPreviewTool {

    private static final boolean DRAW_BETWEEN = true;
    private static final boolean DRAW_THROUGH_CANVASES = true;

    private DoubleProperty brushSize = new SimpleDoubleProperty(this, "Brush Size", 15);
    private ObjectProperty<Brush> brush = new SimpleObjectProperty<>(new CircleBrush(this));
    private ColorProperty brushColor = new ColorProperty(this, "Brush Color", Color.BLACK);

    private BooleanProperty currentlyDrawing = new SimpleBooleanProperty(false);

    private double lastDrawX;
    private double lastDrawY;

    public DrawTool(Drawing drawing) {
        super(drawing);

        icon.setContent(SVG.resizePath(
                "M 167.02 309.34 c -40.12 2.58 -76.53 17.86 -97.19 72.3 c -2.35 6.21 -8 9.98 -14.59 9.98 c -11.11 0 -45.46 -27.67 -55.25 -34.35 C 0 439.62 37.93 512 128 512 c 75.86 0 128 -43.77 128 -120.19 c 0 -3.11 -0.65 -6.08 -0.97 -9.13 l -88.01 -73.34 z M 457.89 0 c -15.16 0 -29.37 6.71 -40.21 16.45 C 213.27 199.05 192 203.34 192 257.09 c 0 13.7 3.25 26.76 8.73 38.7 l 63.82 53.18 c 7.21 1.8 14.64 3.03 22.39 3.03 c 62.11 0 98.11 -45.47 211.16 -256.46 c 7.38 -14.35 13.9 -29.85 13.9 -45.99 C 512 20.64 486 0 457.89 0 z"
                , 0.05));
        shortcut.set(KeyCombination.valueOf("B"));

        super.cursor.bind(Bindings.createObjectBinding(() -> getBrush().getCursor(), brush));

        editableProperties.add(EditableProperty.create(brushSize, () -> new BrushSizeEditor(brushSize)));
        editableProperties.add(EditableProperty.create(brushColor, () -> new BrushColorEditor(brushColor)));

        brushSize.addListener(observable -> {
            if (getBrushSize() > 250) {
                setBrushSize(250);
            }
        });
    }

    @Override
    public void handleMousePressed(MouseEvent event) {
        currentlyDrawing.set(true);
        lastDrawX = event.getX() - getBrushSize() / 2;
        lastDrawY = event.getY() - getBrushSize() / 2;
        handleMouseDragged(event);
    }

    @Override
    public void handleMouseDragged(MouseEvent event) {
        if (!isDraggable(event) && isCurrentlyDrawing()) {
            double x = event.getX() - getBrushSize() / 2;
            double y = event.getY() - getBrushSize() / 2;
            PaintLayer paintLayer = getTopPaintLayer();
            if (paintLayer.getCurrentPath() == null) {
                Path path = new Path();
                path.strokeWidthProperty().bind(brushSize);
                path.strokeProperty().bind(brushColor);
                paintLayer.setCurrentPath(path);
                path.setFill(Color.TRANSPARENT);
                path.setStrokeLineCap(StrokeLineCap.ROUND);
                path.getElements().add(new MoveTo(x, y));
            } else if (lastDrawX != x || lastDrawY != y) {
                Path path = paintLayer.getCurrentPath();
                path.getElements().add(new LineTo(x, y));
            }
            lastDrawX = x;
            lastDrawY = y;
        }
    }

    @Override
    public void handleMouseReleased(MouseEvent event) {
        double scale = drawing.getScale();
        drawing.setScale(1);
        PaintLayer layer = getTopPaintLayer();
        layer.setCurrentPath(null);
        currentlyDrawing.set(false);
        drawing.setScale(scale);
    }

    private PaintLayer getTopPaintLayer() {
        Layer selected = drawing.getLayerSelectionModel().getSelectedItem();
        if (selected instanceof PaintLayer) {
            return (PaintLayer) selected;
        } else {
            PaintLayer newLayer = new PaintLayer(drawing);
            if (selected == null) {
                drawing.getLayers().add(newLayer);
            } else {
                drawing.getLayers().add(drawing.getLayerSelectionModel().getSelectedIndex(), newLayer);
            }
            drawing.getLayerSelectionModel().select(newLayer);
            return newLayer;
        }
    }

    private void drawRelative(PlotCanvas canvas, double x, double y) {
        double relativeToCanvasX = x - canvas.getLayoutX();
        double relativeToCanvasY = y - canvas.getLayoutY();
        canvas.getGraphicsContext2D().setFill(getBrushColor());

        getBrush().draw(canvas.getGraphicsContext2D(), relativeToCanvasX , relativeToCanvasY);
    }

    @Override
    public boolean isDraggable(MouseEvent event) {
        return event.getButton() != MouseButton.PRIMARY || event.isShiftDown();
    }

    public double getBrushSize() {
        return brushSize.get();
    }

    public DoubleProperty brushSizeProperty() {
        return brushSize;
    }

    public void setBrushSize(double brushSize) {
        this.brushSize.set(brushSize);
    }

    public Brush getBrush() {
        return brush.get();
    }

    public ObjectProperty<Brush> brushProperty() {
        return brush;
    }

    public void setBrush(Brush brush) {
        this.brush.set(brush);
    }

    public Color getBrushColor() {
        return brushColor.get();
    }

    public ColorProperty brushColorProperty() {
        return brushColor;
    }

    public void setBrushColor(Color brushColor) {
        this.brushColor.set(brushColor);
    }

    public boolean isCurrentlyDrawing() {
        return currentlyDrawing.get();
    }

}
