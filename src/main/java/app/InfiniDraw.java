package app;

import com.me.tmw.debug.util.Debugger;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.*;

public class InfiniDraw extends Pane {

    private static final double SIZE = 150;

    private final Map<Plot, PlotCanvas> canvasMap = new HashMap<>();
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

    private final VBox icons = new VBox();

    private final DoubleProperty xOffset = new SimpleDoubleProperty();
    private final DoubleProperty yOffset = new SimpleDoubleProperty();
    
    private double startX;
    private double startY;
    private double xOffsetI;
    private double yOffsetI;
    private boolean dragging = false;

    private StringProperty debug = new SimpleStringProperty();


    public InfiniDraw() {

        getChildren().add(icons);
        icons.setLayoutX(10);
        icons.setLayoutY(10);
        tools.addListener((ListChangeListener<Tool>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tool added : change.getAddedSubList()) {
                        icons.getChildren().add(added.getIcon());
                    }
                }
                if (change.wasRemoved()) {
                    for (Tool removed : change.getRemoved()) {
                        icons.getChildren().remove(removed.getDrawing());
                    }
                }
            }
        });
        tools.add(new DrawTool(this));

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

    public PlotCanvas find(double x, double y) {
        int plotX = (int) ((x - getXOffset()) / SIZE);
        int plotY = (int) ((y - getYOffset()) / SIZE);
        if (x - getXOffset() < 0) plotX--;
        if (y - getYOffset() < 0) plotY--;

        Plot plot = new Plot(plotX, plotY);
        if (canvasMap.containsKey(plot)) return canvasMap.get(plot);

        PlotCanvas canvas = new PlotCanvas(SIZE, SIZE, this);
        canvas.setX(plotX * SIZE);
        canvas.setY(plotY * SIZE);
//        canvas.getGraphicsContext2D().setFill(Color.WHITE);
//        canvas.getGraphicsContext2D().fillRect(0, 0, SIZE, SIZE);
        canvas.getGraphicsContext2D().setFill(Color.web("#7a362f"));
        getChildren().add(canvas);
        canvasMap.put(plot, canvas);

        return canvas;
    }

    public Map<Plot, PlotCanvas> getCanvasMap() {
        return canvasMap;
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

}
