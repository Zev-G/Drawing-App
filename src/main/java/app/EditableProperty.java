package app;

import com.me.tmw.properties.editors.PropertyEditor;
import javafx.beans.property.Property;

import java.util.function.Supplier;

public interface EditableProperty<T> {

    static <T> EditableProperty<T> create(Property<T> property, Supplier<PropertyEditor<T>> editor) {
        return new EditableProperty<T>() {
            @Override
            public Property<T> getProperty() {
                return property;
            }

            @Override
            public PropertyEditor<T> generateEditor() {
                return editor.get();
            }
        };
    }

    Property<T> getProperty();
    PropertyEditor<T> generateEditor();

}
