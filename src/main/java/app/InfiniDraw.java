package app;

import app.history.Edit;
import app.tools.DrawTool;
import app.tools.MovingTool;
import app.tools.Tool;
import com.me.tmw.debug.util.Debugger;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class InfiniDraw extends VBox {

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

    private final DoubleProperty xOffset = new SimpleDoubleProperty();
    private final DoubleProperty yOffset = new SimpleDoubleProperty();
    private StringProperty debug = new SimpleStringProperty();

    private final HBox toolBar = new HBox();
    private final StackPane body = new StackPane();
    private final VBox icons = new VBox();
    private final StackPane layersView = new StackPane();
    
    private double startX;
    private double startY;
    private double xOffsetI;
    private double yOffsetI;
    private boolean dragging = false;

    public InfiniDraw() {
        getChildren().addAll(toolBar, body);
        VBox.setVgrow(body, Priority.ALWAYS);
        body.getChildren().addAll(layersView, icons);

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

        icons.setPadding(new Insets(10));
        icons.setAlignment(Pos.CENTER_LEFT);
        icons.setSpacing(10);
        tools.addListener((ListChangeListener<Tool>) change -> {
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
        tools.addAll(new DrawTool(this), new MovingTool(this));

        toolSelectionModel.selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (oldValue != null) oldValue.setSelected(false);
            if (newValue != null) newValue.setSelected(true);
        });
        toolSelectionModel.select(0);
        setOnMousePressed(event -> {
            dragging = true;
            startX = event.getX();
            startY = event.getY();
            xOffsetI = getXOffset();
            yOffsetI = getYOffset();
            getSelectedTool().handleMousePressed(event);
        });
        setFocusTraversable(true);
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Z && event.isControlDown() && !history.isEmpty()) {
                history.pop().undo();
            }
        });
        Debugger.showProperty(debug, this);
        setOnScroll(scrollEvent -> {
            if (scrollEvent.isControlDown()) {
                int mult = scrollEvent.getDeltaY() > 0 ? 1 : -1;
                setScaleX(getScaleX() + (
                        0.1 * mult
                ));
                setScaleY(getScaleY() + (
                        0.1 * mult
                ));
            } else {
//                if (scrollEvent.getDeltaY() > 0) {
//                    brushSize.set(brushSize.get() + 1);
//                } else if (brushSize.get() > 1) {
//                    brushSize.set(brushSize.get() - 1);
//                }
            }
        });
        setOnMouseDragged(event -> {
            getSelectedTool().handleMouseDragged(event);
            if (dragging && getSelectedTool().isDraggable(event)) {
                double deltaX = event.getX() - startX;
                double deltaY = event.getY() - startY;
                setXOffset(deltaX + xOffsetI);
                setYOffset(deltaY + yOffsetI);
            }
        });
        setOnMouseReleased(event -> {
            getSelectedTool().handleMouseReleased(event);
            dragging = false;
        });
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

}
