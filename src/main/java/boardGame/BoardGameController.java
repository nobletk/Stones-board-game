package boardGame;

import boardGame.model.*;

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


/**
 * Class representing the board game controller including the rules of the game.
 */
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

    private List<Position> selectableBluePositions = new ArrayList<>();
    private List<Position> selectableRedPositions = new ArrayList<>();


    private Position selected;

    private BoardGameModel model = new BoardGameModel();

    @FXML
    private GridPane gameBoard;

    @FXML
    private TextField numberOfTurnsField;

    PlayerNameController nameController = new PlayerNameController();

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

    private void checkWinner() {
        if (selectableRedPositions.containsAll(model.blueInitialPositions)) {
            Logger.debug("RED is the winner!");
        }
        if (selectableBluePositions.containsAll(model.redInitialPositions)) {
            Logger.debug("BLUE is the winner!");
        }
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
        if (numberOfTurns.getValue() % 2 == 0) {
            switch (selectionPhase) {
                case SELECT_FROM -> {
                    if (selectableBluePositions.contains(position)) {
                        selectPosition(position);
                        alterSelectionPhase();
                    }
                }
                case SELECT_TO -> {
                    if (selectableBluePositions.contains(position)) {
                        var stoneNumber = model.getBlueStoneNumber(selected).getAsInt();
                        var direction = StoneDirection.of(position.row() - selected.row(), position.col() - selected.col());
                        Logger.debug("Moving stone {} {}", stoneNumber, direction);
                        model.move(stoneNumber, direction);
                        numberOfTurns.set(numberOfTurns.get() + 1);
                        deselectSelectedPosition();
                        alterSelectionPhase();
                        checkWinner();
                    }
                }
            }
        } else {
            switch (selectionPhase) {
                case SELECT_FROM -> {
                    if (selectableRedPositions.contains(position)) {
                        selectPosition(position);
                        alterSelectionPhase();
                    }
                }
                case SELECT_TO -> {
                    if (selectableRedPositions.contains(position)) {
                        var stoneNumber = model.getRedStoneNumber(selected).getAsInt();
                        var direction = StoneDirection.of(position.row() - selected.row(), position.col() - selected.col());
                        Logger.debug("Moving stone {} {}", stoneNumber, direction);
                        model.move(stoneNumber, direction);
                        numberOfTurns.set(numberOfTurns.get() + 1);
                        deselectSelectedPosition();
                        alterSelectionPhase();
                        checkWinner();
                    }
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
        if (numberOfTurns.getValue() % 2 == 0) {
            selectableBluePositions.clear();
            switch (selectionPhase) {
                case SELECT_FROM -> selectableBluePositions.addAll(model.getBluePositions());
                case SELECT_TO -> {
                    var stoneNumber = model.getBlueStoneNumber(selected).getAsInt();
                    for (var direction : model.getValidMoves(stoneNumber)) {
                        selectableBluePositions.add(selected.moveTo(direction));
                    }
                }
            }
        } else {
            selectableRedPositions.clear();
            switch (selectionPhase) {
                case SELECT_FROM -> selectableRedPositions.addAll(model.getRedPositions());
                case SELECT_TO -> {
                    var stoneNumber = model.getRedStoneNumber(selected).getAsInt();
                    for (var direction : model.getValidMoves(stoneNumber)) {
                        selectableRedPositions.add(selected.moveTo(direction));
                    }
                }
            }
        }
    }

    private void showSelectablePositions() {
        if (numberOfTurns.getValue() % 2 == 0) {
            for (var selectablePosition : selectableBluePositions) {
                var square = getSquare(selectablePosition);
                square.getStyleClass().add("selectable");
            }
        } else {
            for (var selectablePosition : selectableRedPositions) {
                var square = getSquare(selectablePosition);
                square.getStyleClass().add("selectable");
            }
        }

    }

    private void hideSelectablePositions() {
        if (numberOfTurns.getValue() % 2 == 0) {
            for (var selectablePosition : selectableBluePositions) {
                var square = getSquare(selectablePosition);
                square.getStyleClass().remove("selectable");
            }
        } else {
            for (var selectablePosition : selectableRedPositions) {
                var square = getSquare(selectablePosition);
                square.getStyleClass().remove("selectable");
            }
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
