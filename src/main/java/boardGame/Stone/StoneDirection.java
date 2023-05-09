package boardGame.Stone;

import boardGame.Movement.Direction;

/**
 * Class representing stone directional changes.
 */
public enum StoneDirection implements Direction {

    DOWN_DIAGONAL_RIGHT(1, 1),
    DOWN_DIAGONAL_LEFT(1, -1),
    UP_DIAGONAL_RIGHT(-1, 1),
    UP_DIAGONAL_LEFT(-1, -1);

    private final int rowChange;
    private final int colChange;

    StoneDirection(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * Returns the change in the row coordinate when moving the stone.
     *
     * @return the change in the row coordinate
     */
    @Override
    public int getRowChange() {
        return rowChange;
    }

    /**
     * Returns the change in the column coordinate when moving the stone.
     *
     * @return the change in the column coordinate
     */
    @Override
    public int getColChange() {
        return colChange;
    }

    /**
     * Returns the direction that corresponds to the coordinate changes specified.
     *
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     * @return the direction that corresponds to the coordinate changes
     */
    public static StoneDirection of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        System.out.println(of(1, 1));
    }
}
