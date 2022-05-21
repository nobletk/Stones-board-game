package boardGame.model;

import javafx.beans.property.ObjectProperty;

import java.util.*;

public class BoardGameModel {

    public static int BoardSize = 5;

    private final Stone[] stones;

    public BoardGameModel() {
        this(new Stone(StoneColor.BLUE_1, new Position(1, 0)),
                new Stone(StoneColor.BLUE_2, new Position(0, 0)),
                new Stone(StoneColor.BLUE_3, new Position(0, 1)),
                new Stone(StoneColor.BLUE_4, new Position(0, 2)),
                new Stone(StoneColor.BLUE_5, new Position(0, 3)),
                new Stone(StoneColor.BLUE_6, new Position(0, 4)),
                new Stone(StoneColor.BLUE_7, new Position(1, 4)),
                new Stone(StoneColor.RED_1, new Position(3, 0)),
                new Stone(StoneColor.RED_2, new Position(4, 0)),
                new Stone(StoneColor.RED_3, new Position(4, 1)),
                new Stone(StoneColor.RED_4, new Position(4, 2)),
                new Stone(StoneColor.RED_5, new Position(4, 3)),
                new Stone(StoneColor.RED_6, new Position(4, 4)),
                new Stone(StoneColor.RED_7, new Position(3, 4)));
    }

    public BoardGameModel(Stone... stones) {
        checkStones(stones);
        this.stones = stones.clone();
    }

    private void checkStones(Stone[] stones) {
        var seen = new HashSet<Position>();
        for (var stone : stones) {
            if (!isOnBoard(stone.getPosition()) || seen.contains(stone.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(stone.getPosition());
        }
    }

    public int getStoneCount() {
        return stones.length;
    }

    public StoneColor getStoneColor(int stoneNumber) {
        return stones[stoneNumber].getColor();
    }

    public Position getStonePosition(int stoneNumber) {
        return stones[stoneNumber].getPosition();
    }

    public ObjectProperty<Position> positionProperty(int stoneNumber) {
        return stones[stoneNumber].positionProperty();
    }

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

    public Set<StoneDirection> getValidMoves(int stoneNumber) {
        EnumSet<StoneDirection> validMoves = EnumSet.noneOf(StoneDirection.class);
        for (var direction : StoneDirection.values()) {
            if (isValidMove(stoneNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    public void move(int stoneNumber, StoneDirection direction) {
        stones[stoneNumber].moveTo(direction);
    }

    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.col() < BoardSize
                && 0 <= position.row() && position.col() < BoardSize;
    }

    public List<Position> getStonePositions() {
        List<Position> positions = new ArrayList<>(stones.length);
        for (var stone : stones) {
            positions.add(stone.getPosition());
        }
        return positions;
    }

    public OptionalInt getStoneNumber(Position position) {
        for (int i = 0; i < stones.length; i++) {
            if (stones[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

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
    }
}
