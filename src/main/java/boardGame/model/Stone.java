package boardGame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class representing the stones' color.
 */
public class Stone {

    private final StoneColor color;
    private final ObjectProperty<Position> position = new SimpleObjectProperty<>();

    public Stone(StoneColor color, Position position) {
        this.color = color;
        this.position.set(position);
    }

    /**
     * Returns the color of the stone.
     * @return the color of the stone
     */
    public StoneColor getColor() {
        return color;
    }

    /**
     * Returns the position of the stone.
     * @return the position of the stone
     */
    public Position getPosition() {
        return position.get();
    }

    /**
     * Returns the new position of the moving stone according to the direction.
     * @param direction the new direction of the moving stone
     */
    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    /**
     * Returns the position of the stone wrapped in an object property.
     * @return the position of the stone
     */
    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    /**
     * Returns the color and position of the stone as a string.
     * @return the color and position of the stone
     */
    public String toString() {
        return color.toString() + position.get().toString();
    }

    public static void main(String[] args) {
        Stone stone = new Stone(StoneColor.BLUE, new Position(0, 0));
        stone.positionProperty().addListener((observableValue, oldPosition, newPosition) -> {
            System.out.printf("%s -> %s%n", oldPosition.toString(), newPosition.toString());
        });
        System.out.println(stone);
        stone.moveTo(StoneDirection.DOWN_DIAGONAL_RIGHT);
        System.out.println(stone);
    }
}
