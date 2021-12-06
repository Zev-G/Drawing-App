package app.tools;

import app.misc.FlatButton;
import app.misc.Res;
import com.me.tmw.properties.ColorProperty;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BrushColorPreset extends StackPane {

    private static final String STYLE_SHEET = Res.css("brush-color-preset");

    private final BrushColorEditor editor;
    private final ColorProperty color = new ColorProperty();

    private final Circle colorDisplay = new Circle();

    public BrushColorPreset(BrushColorEditor editor) {
        this.editor = editor;

        getStylesheets().add(STYLE_SHEET);
        colorDisplay.getStyleClass().add("color-display");

        colorDisplay.setRadius(12);
        colorDisplay.fillProperty().bind(color);
        colorDisplay.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                select();
            }
        });

        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem load = new MenuItem("Load");
        delete.setOnAction(event -> editor.getPresetsView().getChildren().remove(this));
        load.setOnAction(event -> load());
        menu.getItems().addAll(delete, load);

        setOnContextMenuRequested(event -> {
            menu.show(this, Side.RIGHT, 0, 0);
            event.consume();
        });

        getChildren().addAll(colorDisplay);
    }

    public void select() {
        editor.setValue(getColor());
    }
    public void load() {
        setColor(editor.get());
    }

    public BrushColorEditor getEditor() {
        return editor;
    }

    public Color getColor() {
        return color.get();
    }

    public ColorProperty colorProperty() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

}
