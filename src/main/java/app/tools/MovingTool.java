package app.tools;

import app.InfiniDraw;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;

public class MovingTool extends IconPreviewTool {

    public MovingTool(InfiniDraw drawing) {
        super(drawing);

        icon.setContent("M4 0l16 12.279-6.951 1.17 4.325 8.817-3.596 1.734-4.35-8.879-5.428 4.702z");
        shortcut.set(KeyCombination.valueOf("M"));
    }

    @Override
    public boolean isDraggable(MouseEvent event) {
        return true;
    }

}
