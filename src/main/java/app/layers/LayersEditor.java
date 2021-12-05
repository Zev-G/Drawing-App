package app.layers;

import app.Drawing;
import app.misc.FlatButton;
import app.misc.Res;
import app.misc.Sheets;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LayersEditor extends VBox {

    private static final String STYLE_SHEET = Res.css("layers-editor");
    private static final PseudoClass EMPTY = PseudoClass.getPseudoClass("empty");

    private final LayersRearranger layersRearranger;
    private final ScrollPane layersScrollPane = new ScrollPane();
    private final Button add = new FlatButton("Add");
    private final Button remove = new FlatButton("Delete");
    private final HBox buttons = new HBox(add, remove);

    private final StackPane placeHolder = new StackPane(new Label("\nNo Layers\n "));

    private final Drawing drawing;

    public LayersEditor(Drawing drawing) {
        this.drawing = drawing;

        this.layersRearranger = new LayersRearranger(drawing.getLayers());
        layersScrollPane.setContent(layersRearranger);

        getStylesheets().add(STYLE_SHEET);
        getStyleClass().add("layers-editor");
        buttons.getStyleClass().add("buttons");

        layersScrollPane.setFitToWidth(true);
        layersScrollPane.getStylesheets().add(Sheets.SCROLL_PANE);
        layersScrollPane.getStyleClass().add("no-buttons");

        drawing.getLayers().addListener((ListChangeListener<Layer>) change -> {
            if (layersRearranger.getChildren().isEmpty()) {
                layersScrollPane.pseudoClassStateChanged(EMPTY, true);
                if (layersScrollPane.getContent() != placeHolder) {
                    layersScrollPane.setContent(placeHolder);
                }
            } else {
                layersScrollPane.pseudoClassStateChanged(EMPTY, false);
                if (layersScrollPane.getContent() != layersRearranger) {
                    layersScrollPane.setContent(layersRearranger);
                }
            }
        });

        remove.disableProperty().bind(drawing.getLayerSelectionModel().selectedItemProperty().isNull());

        add.setOnAction(actionEvent -> drawing.getLayers().add(new PaintLayer(drawing)));
        remove.setOnAction(event -> {
            Layer removed = drawing.getLayerSelectionModel().getSelectedItem();
            drawing.getLayers().remove(removed);
            if (drawing.getLayerSelectionModel().getSelectedItem() == removed) {
                if (!drawing.getLayers().isEmpty()) {
                    drawing.getLayerSelectionModel().select(drawing.getLayers().size() - 1);
                }
            }
        });

        getChildren().addAll(buttons, layersScrollPane);
    }

}
