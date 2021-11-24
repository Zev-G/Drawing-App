package app.misc;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class FlatButton extends Button {

    public FlatButton() {
        this("", null);
    }
    public FlatButton(String name) {
        this(name, null);
    }
    public FlatButton(Node node) {
        this("", node);
    }

    public FlatButton(String name, Node node) {
        super(name, node);
        setBackground(null);
        setPadding(new Insets(0));
    }

}
