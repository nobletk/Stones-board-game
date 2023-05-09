package boardGame.Movement;

/**
 * Class extending the Record class that returns the new calculated position of the stone.
 *
 * @param row coordinate of the stone
 * @param col coordinate of the stone
 */
public record Position(int row, int col) {

    public Position moveTo(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Returns a string of the row and column coordinates of the stone.
     *
     * @return a string of the row and column coordinates of the stone
     */
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }
}
