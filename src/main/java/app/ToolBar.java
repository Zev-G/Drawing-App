package app;

import javafx.scene.layout.HBox;

public class ToolBar extends HBox {

    private final InfiniDraw drawing;

    public ToolBar(InfiniDraw drawing) {
        this.drawing = drawing;

        drawing.getToolSelectionModel().selectedItemProperty().addListener((observableValue, tool, t1) -> {
            getChildren().clear();
            if (t1 != null) {
                for (EditableProperty<?> editableProperty : t1.getEditableProperties()) {
                    getChildren().add(editableProperty.generateEditor().getNode());
                }
            }
        });
    }

}
