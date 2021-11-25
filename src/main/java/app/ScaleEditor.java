package app;

import app.misc.FlatButton;
import com.me.tmw.properties.editors.DoublePropertyEditor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

public class ScaleEditor extends HBox {

    private static final String STYLE_SHEET = Res.css("scale-editor");

    private final DoubleProperty scale;

    private final Label view = new Label();

    private final Button increase = new FlatButton("+");
    private final Rectangle reset = new Rectangle(10, 10);
    private final Button decrement = new FlatButton("-");

    public ScaleEditor(DoubleProperty scale, Drawing drawing) {
        this.scale = scale;

        getStylesheets().add(STYLE_SHEET);
        getStyleClass().add("scale-editor");
        increase.getStyleClass().add("increment");
        decrement.getStyleClass().add("decrement");
        reset.getStyleClass().add("reset");

        view.textProperty().bind(Bindings.createStringBinding(() -> Math.round(scale.get() * 100) + "%", scale));

        increase.setOnAction(actionEvent -> drawing.increaseScale());
        reset.setOnMousePressed(mouseEvent -> drawing.setScale(1));
        decrement.setOnAction(actionEvent -> drawing.decreaseScale());

        getChildren().addAll(view, increase, reset, decrement);
    }

}
