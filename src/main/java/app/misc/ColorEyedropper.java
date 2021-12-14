package app.misc;

import com.me.tmw.properties.ColorProperty;
import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.util.function.Consumer;

public class ColorEyedropper extends Popup {

    private final Path shape = new Path();
    private final Robot robot = new Robot();

    private final double size = 30;

    private final double height = size;
    private final double pointHeight = size * 0.3;
    private final double width = height - pointHeight;

    private static final Color OFFSET = Color.rgb(128, 128, 128, 0.01);

    private final BooleanProperty inProgress = new SimpleBooleanProperty(false);
    private final ObjectProperty<Drop> currentDrop = new SimpleObjectProperty<>();

    public ColorEyedropper() {
        shape.getElements().addAll(
                new MoveTo(0, 0), new LineTo(width, 0), new LineTo(width, height - pointHeight),
                new LineTo(width / 2, height), new LineTo(0, height - pointHeight), new LineTo(0, 0)
        );
        shape.setStroke(Color.BLACK);
        shape.setStrokeWidth(2);
        setAutoFix(false);
        Region region = new Region() {
            {
                getChildren().add(shape);
                setMinHeight(height + 50);
                shape.setLayoutX(50);
                setMinWidth(100);
            }
        };
        region.setBackground(new Background(new BackgroundFill(OFFSET, CornerRadii.EMPTY, Insets.EMPTY)));
        region.setPickOnBounds(true);
        getContent().add(region);


        region.setOnMouseReleased(mouseEvent -> {
            update(mouseEvent.getScreenX(), mouseEvent.getScreenY());
            finished();
        });
        shape.setPickOnBounds(true);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update(robot.getMouseX(), robot.getMouseY());
            }
        };

        showingProperty().addListener(observable -> {
            if (isShowing()) timer.start();
            else timer.stop();
        });
    }

    private void finished() {
        hide();

        inProgress.set(false);
        if (currentDrop.get() != null) {
            currentDrop.get().setInProgress(false);
            currentDrop.get().finished();
        }
    }

    public Drop display(Window owner) {
        Drop drop = new Drop();
        currentDrop.set(drop);
        inProgress.set(true);
        show(owner);
        return drop;
    }

    private void update(double screenX, double screenY) {
        setX(screenX - width / 2 - 50);
        setY(screenY - getHeight() + 40);
        Color color = robot.getPixelColor(screenX, screenY);
        shape.setFill(color);

        if (getCurrentDrop() != null) {
            getCurrentDrop().setLastX(screenX);
            getCurrentDrop().setLastY(screenY);
            getCurrentDrop().setLastColor(color);
        }
    }

    public boolean isInProgress() {
        return inProgress.get();
    }

    public ReadOnlyBooleanProperty inProgressProperty() {
        return inProgress;
    }

    public Drop getCurrentDrop() {
        return currentDrop.get();
    }

    public ReadOnlyObjectProperty<Drop> currentDropProperty() {
        return currentDrop;
    }

    public static class Drop {

        private final DoubleProperty lastX = new SimpleDoubleProperty();
        private final DoubleProperty lastY = new SimpleDoubleProperty();
        private final ColorProperty lastColor = new ColorProperty();
        private final ObjectProperty<Consumer<DropResult>> resultHandler = new SimpleObjectProperty<>(this, "drop handler");
        private final BooleanProperty inProgress = new SimpleBooleanProperty();

        public Drop() {

        }

        public boolean isInProgress() {
            return inProgress.get();
        }

        public BooleanProperty inProgressProperty() {
            return inProgress;
        }

        public void setInProgress(boolean inProgress) {
            this.inProgress.set(inProgress);
        }

        public void setResultHandler(Consumer<DropResult> resultHandler) {
            this.resultHandler.set(resultHandler);
        }

        public Consumer<DropResult> getResultHandler() {
            return resultHandler.get();
        }

        public ObjectProperty<Consumer<DropResult>> resultHandlerProperty() {
            return resultHandler;
        }

        public void setLastColor(Color lastColor) {
            this.lastColor.set(lastColor);
        }

        public Color getLastColor() {
            return lastColor.get();
        }

        public ColorProperty lastColorProperty() {
            return lastColor;
        }

        public double getLastX() {
            return lastX.get();
        }

        public void setLastX(double lastX) {
            this.lastX.set(lastX);
        }

        public DoubleProperty lastXProperty() {
            return lastX;
        }

        public double getLastY() {
            return lastY.get();
        }

        public void setLastY(double lastY) {
            this.lastY.set(lastY);
        }

        public DoubleProperty lastYProperty() {
            return lastY;
        }

        public void finished() {
            if (getResultHandler() != null) {
                resultHandler.get().accept(new DropResult(this));
            }
        }

    }

    public static class DropResult {

        private final Color color;
        private final double screenX;
        private final double screenY;
        private final Drop drop;

        public DropResult(Drop drop) {
            this.drop = drop;
            this.color = drop.getLastColor();
            this.screenX = drop.getLastX();
            this.screenY = drop.getLastY();
        }

        public Color getColor() {
            return color;
        }

        public double getScreenX() {
            return screenX;
        }

        public double getScreenY() {
            return screenY;
        }

        public Drop getDrop() {
            return drop;
        }

        @Override
        public String toString() {
            return "DropResult{" +
                    "color=" + color +
                    ", screenX=" + screenX +
                    ", screenY=" + screenY +
                    ", drop=" + drop +
                    '}';
        }

    }

}
