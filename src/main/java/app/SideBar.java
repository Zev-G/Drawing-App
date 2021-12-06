package app;

import app.misc.EffectPassThroughPane;
import app.misc.Settings;
import app.tools.Tool;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SideBar extends StackPane {

    private final Drawing drawing;
    private final VBox icons = new VBox();

    public SideBar(Drawing drawing) {
        this.drawing = drawing;

        EffectPassThroughPane iconsGloss = new EffectPassThroughPane(drawing.getLayersView());

        iconsGloss.setTranslateX(-7.5);

        BoxBlur blur = new BoxBlur(15, 15, 3);
        iconsGloss.setEffect(blur);
        blur.setInput(new ColorAdjust(0, 0, 0.2, 0));

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
