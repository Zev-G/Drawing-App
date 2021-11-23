package app;

import javafx.scene.image.Image;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CanvasEdit extends Edit {

    private final Set<CanvasState> newState = new HashSet<>();
    private final Set<CanvasState> oldState = new HashSet<>();

    public CanvasEdit(Collection<PlotCanvas> effected) {
        for (PlotCanvas canvas : effected) {
            oldState.add(new CanvasState(canvas));
            canvas.pushToHistory();
            newState.add(new CanvasState(canvas));
        }
    }

    @Override
    public void undo() {
        for (CanvasState oldCS : oldState) {
            oldCS.revertToState();
            oldCS.canvas.getHistory().pop();
        }
    }

    @Override
    public void redo() {
        for (CanvasState newCS : newState) {
            newCS.revertToState();
            newCS.canvas.getHistory().add(newCS.view);
        }
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
