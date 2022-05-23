package boardGame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoneDirectionTest {

    @Test
    void testOf() {
        assertEquals(StoneDirection.DOWN_DIAGONAL_RIGHT, StoneDirection.of(1, 1));
        assertEquals(StoneDirection.DOWN_DIAGONAL_LEFT, StoneDirection.of(1, -1));
        assertEquals(StoneDirection.UP_DIAGONAL_LEFT, StoneDirection.of(-1, -1));
        assertEquals(StoneDirection.UP_DIAGONAL_RIGHT, StoneDirection.of(-1, 1));
    }

    @Test
    void testOf_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> StoneDirection.of(1, 0));
    }
}