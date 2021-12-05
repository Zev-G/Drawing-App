package app.layers;

import app.Drawing;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class LayerBase implements Layer {

    protected final ObjectProperty<Drawing> drawing = new SimpleObjectProperty<>();
    protected final BooleanProperty selected = new SimpleBooleanProperty();

    @Override
    public ObjectProperty<Drawing> drawingProperty() {
        return drawing;
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

}
