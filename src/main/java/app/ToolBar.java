package app;

import javafx.scene.layout.HBox;

public class ToolBar extends HBox {
    
    private static final String STYLE_SHEET = Res.css("tool-bar");

    private final Drawing drawing;

    public ToolBar(Drawing drawing) {
        this.drawing = drawing;
        
        getStylesheets().add(STYLE_SHEET);
        getStyleClass().add("tool-bar");

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
