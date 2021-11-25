package app;

import app.history.Edit;
import app.tools.DrawTool;
import app.tools.MovingTool;
import app.tools.Tool;
import com.me.tmw.debug.util.Debugger;
import com.me.tmw.properties.NodeProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.*;

public class Drawing extends AnchorPane {

    private static final String STYLE_SHEET = Res.css("main");

    private final Stack<Edit> history = new Stack<>();
    private final ObservableList<Tool> tools = FXCollections.observableArrayList();
    private final SelectionModel<Tool> toolSelectionModel = new SingleSelectionModel<>() {
        @Override
        protected Tool getModelItem(int i) {
            return tools.get(i);
        }

        @Override
        protected int getItemCount() {
            return tools.size();
        }
    };
    private final ObservableList<Layer> layers = FXCollections.observableArrayList();
    private final NodeProperty cursor = new NodeProperty();
    private final SimpleDoubleProperty scale = new SimpleDoubleProperty(1);

    private final DoubleProperty xOffset = new SimpleDoubleProperty();
    private final DoubleProperty yOffset = new SimpleDoubleProperty();
    private StringProperty debug = new SimpleStringProperty();

    private final ToolBar toolBar = new ToolBar(this);
    private final Pane cursorView = new Pane();
    private final StackPane body = new StackPane();
    private final SideBar sideBar = new SideBar(this);
    private final StackPane layersView = new StackPane();

    private final ScaleEditor scaleEditor = new ScaleEditor(scale, this);
    
    private double startX;
    private double startY;
    private double xOffsetI;
    private double yOffsetI;
    private boolean dragging = false;

    public Drawing() {
        getChildren().addAll(body, toolBar, scaleEditor);
        getStylesheets().add(STYLE_SHEET);

        cursor.addListener(observable -> {
            cursorView.getChildren().clear();
            Node cursor = this.cursor.get();
            if (cursor != null) {
                cursorView.getChildren().add(cursor);
                cursor.scaleXProperty().unbind();
                cursor.scaleYProperty().unbind();
                cursor.scaleXProperty().bind(scale);
                cursor.scaleYProperty().bind(scale);
            }
        });

        AnchorPane.setRightAnchor(scaleEditor, 10D);
        AnchorPane.setBottomAnchor(scaleEditor, 10D);

        AnchorPane.setTopAnchor(toolBar, 0D);
        AnchorPane.setLeftAnchor(toolBar, 0D);
        AnchorPane.setRightAnchor(toolBar, 0D);

        toolBar.setMinHeight(35);
        AnchorPane.setTopAnchor(body, 35D);
        AnchorPane.setBottomAnchor(body, 0D);
        AnchorPane.setLeftAnchor(body, 0D);
        AnchorPane.setRightAnchor(body, 0D);

        layersView.setId("Layers View");

        VBox.setVgrow(body, Priority.ALWAYS);
        BorderPane sideBarHolder = new BorderPane();
        sideBarHolder.setLeft(sideBar);
        sideBarHolder.setPickOnBounds(false);
        body.setPickOnBounds(false);

        body.getChildren().addAll(layersView, cursorView, sideBarHolder);

        this.cursorView.setMouseTransparent(true);

        layers.addListener((ListChangeListener<Layer>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Layer added : change.getAddedSubList()) {
                        layersView.getChildren().add(added.getView());
                    }
                }
                if (change.wasRemoved()) {
                    for (Layer removed : change.getAddedSubList()) {
                        layersView.getChildren().remove(removed.getView());
                    }
                }
            }
        });

        tools.addAll(new DrawTool(this), new MovingTool(this));

        toolSelectionModel.selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.setSelected(false);
            }
            cursorView.getChildren().clear();
            if (newValue != null) {
                newValue.setSelected(true);
                Node cursor = newValue.getCursor();
                if (cursor != null) {
                    this.cursorView.getChildren().add(cursor);
                }
                this.cursor.set(cursor);
            }
        });
        toolSelectionModel.select(0);
        layersView.setOnMousePressed(event -> {
            dragging = true;
            startX = event.getX();
            startY = event.getY();
            xOffsetI = getXOffset();
            yOffsetI = getYOffset();
            getSelectedTool().handleMousePressed(event);
            requestFocus();
        });
        body.setOnMouseMoved(event -> updateCursor(event.getX(), event.getY()));
        body.setOnMouseDragged(event -> updateCursor(event.getX(), event.getY()));
        setFocusTraversable(true);
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Z && event.isControlDown() && !history.isEmpty()) {
                history.pop().undo();
            }
        });
        Debugger.showProperty(debug, this);
        setOnScroll(scrollEvent -> {
            if (scrollEvent.isControlDown()) {
                if (scrollEvent.getDeltaY() > 0) {
                    increaseScale();
                } else {
                    decreaseScale();
                }
            }
        });
        layersView.scaleXProperty().bind(scale);
        layersView.scaleYProperty().bind(scale);
        layersView.setOnMouseDragged(event -> {
            getSelectedTool().handleMouseDragged(event);
            if (dragging && getSelectedTool().isDraggable(event)) {
                double deltaX = event.getX() - startX;
                double deltaY = event.getY() - startY;
                setXOffset(deltaX + xOffsetI);
                setYOffset(deltaY + yOffsetI);
            }
        });
        layersView.setOnMouseReleased(event -> {
            getSelectedTool().handleMouseReleased(event);
            dragging = false;
        });
    }

    public void increaseScale() {
        double newScale = getScale() + calculateScaleChange(getScale());
        if (newScale + calculateScaleChange(newScale) > 1 && getScale() < 1) {
            newScale = 1;
        }
        setScale(newScale);
    }

    public void decreaseScale() {
        double newScale = getScale() - calculateScaleChange(getScale());
        if (newScale - calculateScaleChange(newScale)< 1 && getScale() > 1) {
            newScale = 1;
        }
        setScale(newScale);
    }

    private double calculateScaleChange(double scale) {
        return Math.max(0.01, Math.min(1, scale * 0.08));
    }

    private void updateCursor(double x, double y) {
        if (!cursorView.getChildren().isEmpty()) {
            cursor.get().setLayoutX(x);
            cursor.get().setLayoutY(y);
        }
    }

    public SelectionModel<Tool> getToolSelectionModel() {
        return toolSelectionModel;
    }

    public double getXOffset() {
        return xOffset.get();
    }

    public DoubleProperty xOffsetProperty() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset.set(xOffset);
    }

    public double getYOffset() {
        return yOffset.get();
    }

    public DoubleProperty yOffsetProperty() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset.set(yOffset);
    }

    public Stack<Edit> getHistory() {
        return history;
    }

    public Tool getSelectedTool() {
        return toolSelectionModel.getSelectedItem();
    }

    public HBox getToolBar() {
        return toolBar;
    }

    public ObservableList<Layer> getLayers() {
        return layers;
    }

    public Layer getLastLayer() {
        if (layers.isEmpty()) return null;
        else return layers.get(layers.size() - 1);
    }

    public ObservableList<Tool> getTools() {
        return tools;
    }

    public double getScale() {
        return scale.get();
    }

    public SimpleDoubleProperty scaleProperty() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale.set(scale);
    }

}
