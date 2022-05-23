package boardGame.model;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameModelTest {

    BoardGameModel state1 = new BoardGameModel();


    @Test
    void getStoneCount() {
        assertEquals(14, state1.getStoneCount());
        assertNotEquals(1, state1.getStoneCount());
    }

    @Test
    void getStoneColor() {
        assertEquals(StoneColor.BLUE, state1.getStoneColor(1));
        assertNotEquals(StoneColor.RED, state1.getStoneColor(1));
        assertEquals(StoneColor.RED, state1.getStoneColor(13));
        assertNotEquals(StoneColor.BLUE, state1.getStoneColor(13));
    }

    @Test
    void getStonePosition() {
        assertEquals(new Position(0, 0), state1.getStonePosition(1));
        assertNotEquals(new Position(1, 0), state1.getStonePosition(1));
        assertEquals(new Position(4, 0), state1.getStonePosition(8));
        assertNotEquals(new Position(4, 0), state1.getStonePosition(13));
    }

    @Test
    void isValidMove() {
        assertTrue(state1.isValidMove(3, StoneDirection.DOWN_DIAGONAL_RIGHT));
        assertTrue(state1.isValidMove(3, StoneDirection.DOWN_DIAGONAL_LEFT));
        assertTrue(state1.isValidMove(13, StoneDirection.UP_DIAGONAL_LEFT));
        assertFalse(state1.isValidMove(13, StoneDirection.UP_DIAGONAL_RIGHT));
        assertFalse(state1.isValidMove(7, StoneDirection.UP_DIAGONAL_LEFT));
    }

    @Test
    void getValidMoves() {
        assertEquals(EnumSet.of(StoneDirection.DOWN_DIAGONAL_RIGHT, StoneDirection.DOWN_DIAGONAL_LEFT),
                state1.getValidMoves(3));
        assertNotEquals(EnumSet.of(StoneDirection.UP_DIAGONAL_RIGHT, StoneDirection.UP_DIAGONAL_LEFT),
                state1.getValidMoves(3));
    }

    @Test
    void getBluePositions() {
        assertEquals("[(1,0), (0,0), (0,1), (0,2), (0,3), (0,4), (1,4)]", state1.getBluePositions().toString());
        assertNotEquals("[(3,0), (4,0), (4,1), (4,2), (4,3), (4,4), (3,4)]", state1.getBluePositions().toString());
    }

    @Test
    void getRedPositions() {
        assertEquals("[(3,0), (4,0), (4,1), (4,2), (4,3), (4,4), (3,4)]", state1.getRedPositions().toString());
        assertNotEquals("[(1,0), (0,0), (0,1), (0,2), (0,3), (0,4), (1,4)]", state1.getRedPositions().toString());
    }

    @Test
    void getBlueStoneNumber() {
        assertEquals(OptionalInt.of(1), state1.getBlueStoneNumber(new Position(0, 0)));
        assertEquals(OptionalInt.empty(), state1.getBlueStoneNumber(new Position(4, 3)));
        assertNotEquals(OptionalInt.of(1), state1.getBlueStoneNumber(new Position(0, 4)));
    }

    @Test
    void getRedStoneNumber() {
        assertEquals(OptionalInt.of(11), state1.getRedStoneNumber(new Position(4, 3)));
        assertEquals(OptionalInt.empty(), state1.getRedStoneNumber(new Position(0, 0)));
        assertNotEquals(OptionalInt.of(11), state1.getRedStoneNumber(new Position(4, 1)));
    }

    @Test
    void testToString() {
        assertEquals("""
                [BLUE(1,0),
                BLUE(0,0),
                BLUE(0,1),
                BLUE(0,2),
                BLUE(0,3),
                BLUE(0,4),
                BLUE(1,4),
                RED(3,0),
                RED(4,0),
                RED(4,1),
                RED(4,2),
                RED(4,3),
                RED(4,4),
                RED(3,4)]""", state1.toString());
    }
}