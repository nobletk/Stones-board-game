package boardGame.model;

public enum StoneDirection implements Direction{

    DOWN_DIAGONAL_RIGHT(1, 1),
    DOWN_DIAGONAL_LEFT(1, -1),
    UP_DIAGONAL_RIGHT(-1, 1),
    UP_DIAGONAL_LEFT(-1, -1);

    private final int rowChange;
    private final int colChange;

    StoneDirection(int rowChange, int colChange){
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    @Override
    public int getRowChange() {
        return rowChange;
    }

    @Override
    public int getColChange() {
        return colChange;
    }

    public static StoneDirection of(int rowChange, int colChange){
        for(var direction : values()){
            if(direction.rowChange == rowChange && direction.colChange == colChange){
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        System.out.println(of(1,1));
    }
}
