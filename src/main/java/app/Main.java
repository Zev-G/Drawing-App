package app;

import app.misc.ColorEyedropper;
import com.me.tmw.debug.devtools.DevScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
//        primaryStage.setScene(new DevScene(new Drawing()));
        primaryStage.show();
//        primaryStage.setMaximized(true);

        new ColorEyedropper().display(primaryStage);
    }

    public static void main(String[] args) {
        Main.launch(args);
    }

}
