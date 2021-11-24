package app.misc;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;

public final class Shortcuts {

    public static void install(KeyCombination shortcut, Runnable run, Node node) {
        node.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.getAccelerators().remove(shortcut);
            }
            if (newValue != null) {
                newValue.getAccelerators().put(shortcut, run);
            }
        });
    }

    public static void install(ObjectProperty<KeyCombination> shortcutProperty, Runnable run, Node node) {
        shortcutProperty.addListener((observable, oldValue, newValue) -> {
            if (node.getScene() != null) {
                if (oldValue != null) node.getScene().getAccelerators().remove(oldValue);
                if (newValue != null) node.getScene().getAccelerators().put(newValue, run);
            }
        });
        node.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.getAccelerators().remove(shortcutProperty.get());
            }
            if (newValue != null) {
                newValue.getAccelerators().put(shortcutProperty.get(), run);
            }
        });
    }

}
