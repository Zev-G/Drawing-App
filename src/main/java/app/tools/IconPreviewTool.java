package app.tools;

import app.Drawing;
import app.misc.FlatButton;
import app.misc.Shortcuts;
import com.me.tmw.properties.ColorProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;

public abstract class IconPreviewTool extends Tool {

    protected final SVGPath icon = new SVGPath();
    private final Button select = new FlatButton(icon);

    protected final ObjectProperty<KeyCombination> shortcut = new SimpleObjectProperty<>();
    protected final ColorProperty defaultColor = new ColorProperty();
    protected final ColorProperty selectedColor = new ColorProperty();

    public IconPreviewTool(Drawing drawing) {
        super(drawing);

        defaultColor.set(Color.WHITE);
        selectedColor.set(Color.web("#5495eb"));

        icon.setStroke(Color.web("#262626"));
        icon.setStrokeWidth(2);
        icon.setStrokeLineCap(StrokeLineCap.ROUND);
        icon.setPickOnBounds(true);
        icon.setCursor(Cursor.HAND);
        icon.setFill(defaultColor.get());

        icon.setOpacity(0.8);
        icon.setOnMouseEntered(event -> icon.setOpacity(1));
        icon.setOnMouseExited(event -> icon.setOpacity(0.8));
        select.setOnAction(event -> drawing.getToolSelectionModel().select(this));

        selected.addListener(observable -> {
            icon.fillProperty().unbind();
            if (isSelected()) {
                icon.fillProperty().bind(selectedColor);
            } else {
                icon.fillProperty().bind(defaultColor);
            }
        });

        Shortcuts.install(shortcut, select::fire, select);
    }

    @Override
    public Node getIcon() {
        return select;
    }

}
