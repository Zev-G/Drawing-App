package app;

import com.me.tmw.properties.ColorProperty;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;

public abstract class IconPreviewTool extends Tool {

    protected final SVGPath icon = new SVGPath();

    protected final ColorProperty defaultColor = new ColorProperty(Color.WHITE);
    protected final ColorProperty selectedColor = new ColorProperty(Color.web("#125fc4"));

    public IconPreviewTool(InfiniDraw drawing) {
        super(drawing);

        icon.setStroke(Color.BLACK);
        icon.setStrokeWidth(2);
        icon.setStrokeLineCap(StrokeLineCap.ROUND);
        icon.setPickOnBounds(true);
        icon.setCursor(Cursor.HAND);

        icon.setOpacity(0.5);
        icon.setOnMouseEntered(event -> icon.setOpacity(1));
        icon.setOnMouseExited(event -> icon.setOpacity(0.5));
        icon.setOnMousePressed(event -> drawing.getToolSelectionModel().select(this));

        selected.addListener(observable -> {
            icon.fillProperty().unbind();
            if (isSelected()) {
                icon.fillProperty().bind(selectedColor);
            } else {
                icon.fillProperty().bind(defaultColor);
            }
        });
    }

    @Override
    public SVGPath getIcon() {
        return icon;
    }

}
