package app;

import app.tools.Tool;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SideBar extends StackPane {

    private final Drawing drawing;
    private final VBox icons = new VBox();

    public SideBar(Drawing drawing) {
        this.drawing = drawing;

        StackPane iconsGloss = new StackPane();
        iconsGloss.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.8), new CornerRadii(10), Insets.EMPTY)));
        iconsGloss.setEffect(new GaussianBlur(20));
        iconsGloss.setMouseTransparent(true);
        icons.setPadding(new Insets(10));
        icons.setAlignment(Pos.CENTER_LEFT);
        icons.setSpacing(10);

        getChildren().addAll(iconsGloss, icons);
        setAlignment(Pos.CENTER_LEFT);

        drawing.getTools().addListener((ListChangeListener<Tool>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tool added : change.getAddedSubList()) {
                        icons.getChildren().add(added.getIcon());
                        added.setSelected(false);
                    }
                }
                if (change.wasRemoved()) {
                    for (Tool removed : change.getRemoved()) {
                        icons.getChildren().remove(removed.getDrawing());
                    }
                }
            }
        });
    }

}
