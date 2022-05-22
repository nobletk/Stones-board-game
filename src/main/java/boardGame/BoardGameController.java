package boardGame;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

import boardGame.model.BoardGameModel;
import boardGame.model.Position;
import boardGame.model.StoneDirection;


public class BoardGameController {

    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    private BoardGameModel model = new BoardGameModel();

    @FXML
    private GridPane gameBoard;

    @FXML
    private TextField numberOfTurnsField;

    private IntegerProperty numberOfTurns = new SimpleIntegerProperty();

    @FXML
    private void initialize() {
        createBoard();
        createStones();
        createBindings();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void createBoard() {
        for (int i = 0; i < gameBoard.getRowCount(); i++) {
            for (int j = 0; j < gameBoard.getColumnCount(); j++) {
                var square = createSquare();
                gameBoard.add(square, j, i);
            }
        }
    }

    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private void createStones() {
        for (int i = 0; i < model.getStoneCount(); i++) {
            model.positionProperty(i).addListener(this::stonePositionChange);
            var stone = createStone(Color.valueOf(model.getStoneColor(i).name()));
            getSquare(model.getStonePosition(i)).getChildren().add(stone);
        }
    }

    private Circle createStone(Color color) {
        var stone = new Circle(50);
        stone.setFill(color);
        return stone;
    }

    private void createBindings() {
        numberOfTurnsField.textProperty().bind(numberOfTurns.asString());
    }

    @FXML
    private void handleMouseClick(MouseEvent mouseEvent) {
        var square = (StackPane) mouseEvent.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

    private void handleClickOnSquare(Position position) {
        switch (selectionPhase) {
            case SELECT_FROM -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
            }
            case SELECT_TO -> {
                if (selectablePositions.contains(position)) {
                    var stoneNumber = model.getStoneNumber(selected).getAsInt();
                    var direction = StoneDirection.of(position.row() - selected.row(), position.col() - selected.col());
                    Logger.debug("Moving stone {} {}", stoneNumber, direction);
                    model.move(stoneNumber, direction);
                    numberOfTurns.set(numberOfTurns.get() + 1);
                    deselectSelectedPosition();
                    alterSelectionPhase();
                }
            }
        }
    }

    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
        selected = position;
        showSelectedPosition();
    }

    private void showSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().add("selected");
    }

    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var square = getSquare(selected);
        square.getStyleClass().remove("selected");
    }

    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM -> selectablePositions.addAll(model.getStonePositions());
            case SELECT_TO -> {
                var stoneNumber = model.getStoneNumber(selected).getAsInt();
                for (var direction : model.getValidMoves(stoneNumber)) {
                    selectablePositions.add(selected.moveTo(direction));
                }
            }
        }
    }

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    private StackPane getSquare(Position position) {
        for (var child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private void stonePositionChange(ObservableValue<? extends Position> observableValue, Position oldPosition, Position newPosition) {
        Logger.debug("Move {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }
}
