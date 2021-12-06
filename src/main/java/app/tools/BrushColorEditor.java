package app.tools;

import app.misc.Res;
import app.misc.Settings;
import com.me.tmw.properties.editors.ColorPropertyEditor;
import com.me.tmw.properties.editors.PropertyEditorBase;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BrushColorEditor extends PropertyEditorBase<Color> {

    private static final String STYLE_SHEET = Res.css("brush-color-editor");

    private final ColorPropertyEditor mainEditor;

    private final HBox mainLayout = new HBox();
    private final StackPane addPreset = new StackPane();
    private final HBox presets = new HBox();

    public BrushColorEditor(Property<Color> value) {
        super(value);
        this.mainEditor = new ColorPropertyEditor(value);

        mainLayout.getStylesheets().add(STYLE_SHEET);
        addPreset.getStyleClass().add("add-preset");
        mainLayout.getStyleClass().add("brush-color-editor");
        presets.getStyleClass().add("presets");

        Circle addPresetCircle = new Circle(12);
        addPresetCircle.getStyleClass().add("add-preset-circle");
        addPreset.getChildren().addAll(addPresetCircle, new Label("+"));

        addPreset.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                loadPreset(get());
            }
        });

        mainLayout.getChildren().addAll(mainEditor.getNode(), presets, addPreset);

        loadPresets(Settings.DEFAULT_COLOR_PRESETS);

        setNode(mainLayout);
    }

    private void loadPresets(Color[] presetColors) {
        for (Color presetColor : presetColors) {
            loadPreset(presetColor);
        }
    }

    private void loadPreset(Color presetColor) {
        BrushColorPreset preset = new BrushColorPreset(this);
        preset.setColor(presetColor);
        presets.getChildren().add(preset);
    }

    public HBox getPresetsView() {
        return presets;
    }

}
