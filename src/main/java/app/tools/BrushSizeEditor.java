package app.tools;

import app.misc.Res;
import app.misc.FlatButton;
import com.me.tmw.properties.editors.DoublePropertyEditor;
import com.me.tmw.properties.editors.PropertyEditorBase;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class BrushSizeEditor extends PropertyEditorBase<Number> {

    private static final String STYLE_SHEET = Res.css("brush-size-editor");

    private final Button increase = new FlatButton("+");
    private final Button decrease = new FlatButton("-");
    private final DoublePropertyEditor textEditor;

    private final HBox layout;

    public BrushSizeEditor(Property<Number> value) {
        super(value);
        this.textEditor = new DoublePropertyEditor(value);

        layout = new HBox(decrease, textEditor.getNode(), increase);
        setNode(layout);

        layout.getStylesheets().add(STYLE_SHEET);
        layout.getStyleClass().add("brush-size-editor");
        increase.getStyleClass().add("increment");
        decrease.getStyleClass().add("decrement");

        layout.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                set(get().doubleValue() + 1);
            } else {
                set(get().doubleValue() - 1);
            }
        });

        increase.setOnAction(event -> set(get().doubleValue() + 1));
        decrease.setOnAction(event -> set(get().doubleValue() - 1));
    }

}
