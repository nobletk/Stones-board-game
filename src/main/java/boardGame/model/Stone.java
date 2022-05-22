package boardGame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class Stone {

    private final StoneColor color;
    private final ObjectProperty<Position> position = new SimpleObjectProperty<>();

    public Stone(StoneColor color, Position position) {
        this.color = color;
        this.position.set(position);
    }

    public StoneColor getColor() {
        return color;}

    public Position getPosition() {
        return position.get();
    }

    public void moveTo(Direction direction){
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    public String toString() {
        return color.toString() + position.get().toString();
    }

    public static void main(String[] args) {
        Stone stone = new Stone(StoneColor.BLUE, new Position(0,0) );
        stone.positionProperty().addListener((observableValue, oldPosition, newPosition) -> {
            System.out.printf("%s -> %s%n", oldPosition.toString(), newPosition.toString());
        });
        System.out.println(stone);
        stone.moveTo(StoneDirection.DOWN_DIAGONAL_RIGHT);
        System.out.println(stone);
    }
}
