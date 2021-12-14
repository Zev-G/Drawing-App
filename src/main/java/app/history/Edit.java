package app.history;

import app.Drawing;

public abstract class Edit {

    public abstract void undo();
    public abstract void redo();

    protected final Drawing drawing;

    public Edit(Drawing drawing) {
        this.drawing = drawing;
    }

    public Drawing getDrawing() {
        return drawing;
    }

}
