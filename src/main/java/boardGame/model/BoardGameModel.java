package boardGame.model;

import javafx.beans.property.ObjectProperty;

import java.util.*;

public class BoardGameModel {

    public static int BoardSize = 5;

    private final Stone[] stones;

    public List<Position> blueInitialPositions = new ArrayList<>();
    public List<Position> redInitialPositions = new ArrayList<>();

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
        return 0 <= position.row() && position.row() < BoardSize
                && 0 <= position.col() && position.col() < BoardSize;
    }

    public List<Position> getBluePositions() {
        List<Position> positions = new ArrayList<>(7);
        for (var stone : stones) {
            if (stone.getColor() == StoneColor.BLUE) {
                positions.add(stone.getPosition());
            }
        }
        return positions;
    }

    public List<Position> getRedPositions() {
        List<Position> positions = new ArrayList<>(7);
        for (var stone : stones) {
            if (stone.getColor() == StoneColor.RED) {
                positions.add(stone.getPosition());
            }
        }
        return positions;
    }

    public OptionalInt getBlueStoneNumber(Position position) {
        for (int i = 0; i < 8; i++) {
            if (stones[i].getPosition().equals(position) && getStoneColor(i) == StoneColor.BLUE) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public OptionalInt getRedStoneNumber(Position position) {
        for (int i = 7; i < 14; i++) {
            if (stones[i].getPosition().equals(position) && getStoneColor(i) == StoneColor.RED) {
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
        System.out.println(model.blueInitialPositions);
        System.out.println(model.redInitialPositions);
    }
}
