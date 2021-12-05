package app.misc;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;

public class EffectPassThroughPane extends Pane {

    private final Parent container;

    private WritableImage image;

    public EffectPassThroughPane(Parent container) {
        this.container = container;
        getStyleClass().add("effect-pass-through-pane");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
            }
        };

        if (Settings.USE_GLOSS_EFFECT) timer.start();
    }

    private void updateBackground() {
        if (getWidth() <= 0 || getHeight() <= 0 || getOpacity() == 0) {
            return;
        }

        if (image == null || getWidth() != image.getWidth() || getHeight() != image.getHeight()) {
            image = new WritableImage((int) getWidth(), (int) getHeight());

            BackgroundImage bgImage = new BackgroundImage(
                    image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT
            );
            setBackground(new Background(bgImage));
        }

        double x = getLayoutX() + getTranslateX();
        double y = getLayoutY() + getTranslateY();

        SnapshotParameters parameters = new SnapshotParameters();
        Rectangle2D rect = new Rectangle2D(
                x, y, getWidth(), getHeight()
        );
        parameters.setViewport(rect);

        container.snapshot(parameters, image);
    }

}
