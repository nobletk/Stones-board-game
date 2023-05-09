package boardGame.BoardGameModel;

import boardGame.Movement.Position;
import boardGame.Stone.Stone;
import boardGame.Stone.StoneColor;
import boardGame.Stone.StoneDirection;
import javafx.beans.property.ObjectProperty;

import java.util.*;

/**
 * Class representing the board game model, stones initial positioning and the rules of the game.
 */
public class BoardGameModel {

    public static int BoardSize = 5;

    private final Stone[] stones;

    public List<Position> blueInitialPositions = new ArrayList<>();
    public List<Position> redInitialPositions = new ArrayList<>();

    /**
     * Class that represents the initial positioning of the stones corresponding to row and column.
     */
    public BoardGameModel() {
        this(new Stone(StoneColor.BLUE, new Position(1, 0)),
                new Stone(StoneColor.BLUE, new Position(0, 0)),
                new Stone(StoneColor.BLUE, new Position(0, 1)),
                new Stone(StoneColor.BLUE, new Position(0, 2)),
                new Stone(StoneColor.BLUE, new Position(0, 3)),
                new Stone(StoneColor.BLUE, new Position(0, 4)),
                new Stone(StoneColor.BLUE, new Position(1, 4)),
                new Stone(StoneColor.RED, new Position(3, 0)),
                new Stone(StoneColor.RED, new Position(4, 0)),
                new Stone(StoneColor.RED, new Position(4, 1)),
                new Stone(StoneColor.RED, new Position(4, 2)),
                new Stone(StoneColor.RED, new Position(4, 3)),
                new Stone(StoneColor.RED, new Position(4, 4)),
                new Stone(StoneColor.RED, new Position(3, 4)));
    }

    public BoardGameModel(Stone... stones) {
        checkStones(stones);
        this.stones = stones.clone();
    }

    /**
     * creating a HashSet for the stones on the board and adding each color to its separate list.
     * @param stones the array of stones on the board
     */
    private void checkStones(Stone[] stones) {
        var seen = new HashSet<Position>();
        for (var stone : stones) {
            if (!isOnBoard(stone.getPosition()) || seen.contains(stone.getPosition())) {
                throw new IllegalArgumentException();
            }
            if (stone.getColor() == StoneColor.BLUE) {
                blueInitialPositions.add(stone.getPosition());
            } else {
                redInitialPositions.add(stone.getPosition());
            }
            seen.add(stone.getPosition());
        }
    }

    /**
     * Returns the number of the stones on the game board.
     *
     * @return the number of the stones
     */
    public int getStoneCount() {
        return stones.length;
    }

    /**
     * Returns the color the stone specified by the number given.
     *
     * @param stoneNumber the value representing the stone number
     * @return the stone color whether blue or red
     */
    public StoneColor getStoneColor(int stoneNumber) {
        return stones[stoneNumber].getColor();
    }

    /**
     * Returns the stone position on the game board for the specific stone
     * according to the stone number specified.
     *
     * @param stoneNumber the value representing the stone number
     * @return the stone position on the game board
     */
    public Position getStonePosition(int stoneNumber) {
        return stones[stoneNumber].getPosition();
    }

    /**
     * Returns an objectProperty wrapping a list of position of
     * the specified stone.
     *
     * @param stoneNumber the value representing the stone number
     * @return an objectProperty wrapping a list of position of the specified stone.
     */
    public ObjectProperty<Position> positionProperty(int stoneNumber) {
        return stones[stoneNumber].positionProperty();
    }

    /**
     * Returns boolean value checking if a move is valid or not for the specified stone number.
     *
     * @param stoneNumber the value representing the stone number
     * @param direction   the movement of the stone
     * @return boolean value whether the move is valid or not
     */
    public boolean isValidMove(int stoneNumber, StoneDirection direction) {
        if (stoneNumber < 0 || stoneNumber >= stones.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = stones[stoneNumber].getPosition().moveTo(direction);
        if (!isOnBoard(newPosition)) {
            return false;
        }
        for (var stone : stones) {
            if (stone.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * A set that contains all possible moves for the specified stone number.
     *
     * @param stoneNumber the value representing the stone number
     * @return a set containing all possible moves for that stone
     */
    public Set<StoneDirection> getValidMoves(int stoneNumber) {
        EnumSet<StoneDirection> validMoves = EnumSet.noneOf(StoneDirection.class);
        for (var direction : StoneDirection.values()) {
            if (isValidMove(stoneNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * Returns the new position of the moving stone.
     *
     * @param stoneNumber the value representing the stone number
     * @param direction   the new position of the stone
     */
    public void move(int stoneNumber, StoneDirection direction) {
        stones[stoneNumber].moveTo(direction);
    }

    /**
     * Returns a boolean value of whether the position is or isn't on the board.
     *
     * @param position the position meant to be checked
     * @return a boolean value of whether the position is or isn't on the board
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BoardSize
                && 0 <= position.col() && position.col() < BoardSize;
    }

    /**
     * Returns a list of current positioning of the blue stones.
     *
     * @return a list of current positioning of the blue stones
     */
    public List<Position> getBluePositions() {
        List<Position> positions = new ArrayList<>(7);
        for (var stone : stones) {
            if (stone.getColor() == StoneColor.BLUE) {
                positions.add(stone.getPosition());
            }
        }
        return positions;
    }

    /**
     * Returns a list of current positioning of the red stones.
     *
     * @return a list of current positioning of the red stones
     */
    public List<Position> getRedPositions() {
        List<Position> positions = new ArrayList<>(7);
        for (var stone : stones) {
            if (stone.getColor() == StoneColor.RED) {
                positions.add(stone.getPosition());
            }
        }
        return positions;
    }

    /**
     * Returns an optional integer number of the blue stone in a specific position.
     *
     * @param position the position of the blue stone
     * @return an optional integer number of the blue stone
     */
    public OptionalInt getBlueStoneNumber(Position position) {
        for (int i = 0; i < 8; i++) {
            if (stones[i].getPosition().equals(position) && getStoneColor(i) == StoneColor.BLUE) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Returns an optional integer number of the red stone in a specific position.
     *
     * @param position the position of the red stone
     * @return an optional integer number of the red stone
     */
    public OptionalInt getRedStoneNumber(Position position) {
        for (int i = 7; i < 14; i++) {
            if (stones[i].getPosition().equals(position) && getStoneColor(i) == StoneColor.RED) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Returns a string of joined elements.
     *
     * @returna string of joined elements
     */
    public String toString() {
        StringJoiner joiner = new StringJoiner(",\n", "[", "]");
        for (var stone : stones) {
            joiner.add(stone.toString());
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        BoardGameModel model = new BoardGameModel();
        System.out.println(model);
        System.out.println(model.blueInitialPositions);
        System.out.println(model.redInitialPositions);
    }
}
