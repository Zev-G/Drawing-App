package app.misc;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

public final class Settings {

    // PERFORMANCE
    public static BooleanProperty USE_GLOSS_EFFECT = new SimpleBooleanProperty(Settings.class, "Use Gloss Effect", false);

    // GENERAL
    public static Color[] DEFAULT_COLOR_PRESETS = { Color.BLACK, Color.WHITE, Color.RED, Color.BLUE };

    public static boolean isUseGlossEffect() {
        return USE_GLOSS_EFFECT.get();
    }

    public static BooleanProperty useGlossEffectProperty() {
        return USE_GLOSS_EFFECT;
    }

    public static void setUseGlossEffect(boolean useGlossEffect) {
        USE_GLOSS_EFFECT.set(useGlossEffect);
    }
}
