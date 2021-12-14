package app.layers;

import app.Drawing;
import app.Plot;
import app.PlotCanvas;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

import java.util.HashMap;
import java.util.Map;

public class PaintLayer extends LayerBase {

    private static final boolean WHITE_CANVAS_BG = false;
    private static final double SIZE = 500;

    private ObjectProperty<Path> currentPath = new SimpleObjectProperty<>();
    private final Drawing drawing;

    private final Pane pane = new Pane();

    public PaintLayer(Drawing drawing) {
        this.drawing = drawing;
//        pane.setSnapToPixel(tr);
        currentPath.addListener((observableValue, path, t1) -> {
            if (t1 != null) {
                pane.getChildren().add(t1);
            }
        });
    }

    @Override
    public Node getView() {
        return pane;
    }

    public Path getCurrentPath() {
        return currentPath.get();
    }

    public ObjectProperty<Path> currentPathProperty() {
        return currentPath;
    }

    public void setCurrentPath(Path currentPath) {
        this.currentPath.set(currentPath);
    }
}
