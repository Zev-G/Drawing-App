package app.misc;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Popup;
import javafx.stage.Window;

public class ColorEyedropper extends Popup {

    private final Path shape = new Path();
    private final Robot robot = new Robot();

    private final double size = 20;

    private final double height = size;
    private final double pointHeight = size * 0.3;
    private final double width = height - pointHeight;

    public ColorEyedropper() {
        shape.getElements().addAll(
                new MoveTo(0, 0), new LineTo(width, 0), new LineTo(width, height - pointHeight),
                new LineTo(width / 2, height), new LineTo(0, height - pointHeight), new LineTo(0, 0)
        );
        shape.setStroke(Color.BLACK);
        shape.setStrokeWidth(2);
        Region region = new Region() {
            {
                getChildren().add(shape);
                Line line = new Line();
                setMinHeight(height + 20);
                setStyle("-fx-background-color: transparent;");
            }
        };
        region.setPickOnBounds(true);
        getContent().add(region);


        region.setOnMousePressed(mouseEvent -> hide());
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

    public void display(Window owner) {
        show(owner);
    }

    private void update(double screenX, double screenY) {
        setX(screenX - width / 2);
        setY(screenY - getHeight() + 10);

        shape.setFill(robot.getPixelColor(screenX, screenY));
    }

}
