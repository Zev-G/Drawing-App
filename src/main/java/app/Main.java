package app;

import app.misc.ColorEyedropper;
import com.me.tmw.debug.devtools.DevScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class Main extends Application {

    Consumer<ColorEyedropper.DropResult> resultConsumer = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new DevScene(new Drawing()));
        primaryStage.show();
        primaryStage.setMaximized(true);

    }

    public static void main(String[] args) {
        Main.launch(args);
    }

}
