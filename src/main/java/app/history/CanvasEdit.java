package app.history;

import app.Drawing;
import app.PlotCanvas;
import javafx.scene.image.Image;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CanvasEdit extends Edit {

    private final Set<CanvasState> newState = new HashSet<>();
    private final Set<CanvasState> oldState = new HashSet<>();

    public CanvasEdit(Collection<PlotCanvas> effected, Drawing drawing) {
        super(drawing);
        for (PlotCanvas canvas : effected) {
            oldState.add(new CanvasState(canvas));
            canvas.pushToHistory();
            newState.add(new CanvasState(canvas));
        }
    }

    @Override
    public void undo() {
        double scale = getDrawing().getScale();
        getDrawing().setScale(1);
        for (CanvasState oldCS : oldState) {
            oldCS.revertToState();
            oldCS.canvas.getHistory().pop();
        }
        getDrawing().setScale(scale);
    }

    @Override
    public void redo() {
        double scale = getDrawing().getScale();
        getDrawing().setScale(1);
        for (CanvasState newCS : newState) {
            newCS.revertToState();
            newCS.canvas.getHistory().add(newCS.view);
        }
        getDrawing().setScale(scale);
    }

    private static class CanvasState {

        private final Image view;
        private final PlotCanvas canvas;

        private CanvasState(PlotCanvas canvas) {
            this.canvas = canvas;
            this.view = canvas.getHistory().isEmpty() ? null : canvas.getHistory().peek();
        }

        private CanvasState(Image view, PlotCanvas canvas) {
            this.view = view;
            this.canvas = canvas;
        }

        private void revertToState() {
            canvas.clear();
            if (view != null) canvas.loadImage(view);
        }

    }

}
