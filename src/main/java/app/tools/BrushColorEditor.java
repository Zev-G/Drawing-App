package app.tools;

import app.misc.ColorEyedropper;
import app.misc.FlatButton;
import app.misc.Res;
import app.misc.Settings;
import com.me.tmw.nodes.util.NodeMisc;
import com.me.tmw.properties.editors.ColorPropertyEditor;
import com.me.tmw.properties.editors.PropertyEditorBase;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BrushColorEditor extends PropertyEditorBase<Color> {

    private static final String STYLE_SHEET = Res.css("brush-color-editor");

    private final ColorPropertyEditor mainEditor;
    private final ColorEyedropper eyedropper = new ColorEyedropper();
    private final Button eyeDropButton = new FlatButton(NodeMisc.svgPath(
            "M 50.75 333.25 c -12 12 -18.75 28.28 -18.75 45.26 V 424 L 0 480 l 32 32 l 56 -32 h 45.49 c 16.97 0 33.25 -6.74 45.25 -18.74 l 126.64 -126.62 l -128 -128 L 50.75 333.25 z M 483.88 28.12 c -37.47 -37.5 -98.28 -37.5 -135.75 0 l -77.09 77.09 l -13.1 -13.1 c -9.44 -9.44 -24.65 -9.31 -33.94 0 l -40.97 40.97 c -9.37 9.37 -9.37 24.57 0 33.94 l 161.94 161.94 c 9.44 9.44 24.65 9.31 33.94 0 L 419.88 288 c 9.37 -9.37 9.37 -24.57 0 -33.94 l -13.1 -13.1 l 77.09 -77.09 c 37.51 -37.48 37.51 -98.26 0.01 -135.75 z"
            , 0.05
    ));

    private final HBox mainLayout = new HBox();
    private final StackPane addPreset = new StackPane();
    private final HBox presets = new HBox();

    public BrushColorEditor(Property<Color> value) {
        super(value);
        this.mainEditor = new ColorPropertyEditor(value);

        mainLayout.getStylesheets().add(STYLE_SHEET);
        addPreset.getStyleClass().add("add-preset");
        mainLayout.getStyleClass().add("brush-color-editor");
        eyeDropButton.getStyleClass().add("eye-dropper");
        presets.getStyleClass().add("presets");

        Circle addPresetCircle = new Circle(12);
        addPresetCircle.getStyleClass().add("add-preset-circle");
        addPreset.getChildren().addAll(addPresetCircle, new Label("+"));

        addPreset.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                loadPreset(get());
            }
        });

        eyeDropButton.setOnAction(event -> {
            if (mainLayout.getScene() != null && mainLayout.getScene().getWindow() != null) {
                eyedropper.display(mainLayout.getScene().getWindow()).setResultHandler(dropResult -> value.setValue(dropResult.getColor()));
            }
        });

        mainLayout.getChildren().addAll(mainEditor.getNode(), eyeDropButton, presets, addPreset);

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
