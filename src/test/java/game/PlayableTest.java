package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayableTest {

    @Test
    public void testValidPlay() {
        Playable playable = new CheckersGame();
        playable = playable.setField(5, 5, PlayerType.BLACK);
        assertEquals(PlayerType.BLACK, ((CheckersGame) playable).field[5][5], "At (5/5) should be a Black Player");
        Move move = Move.of(5, 5, 6, 6, false);
        playable = playable.play(move);
        assertNull(((CheckersGame) playable).field[5][5], "At (5/5) should be null");
        assertEquals(PlayerType.BLACK, ((CheckersGame) playable).field[6][6], "At (6/6) should be a Black Player");
        playable = playable.setField(6, 6, null);
        assertNull(((CheckersGame) playable).field[6][6], "At (6/6) should be null");
    }

    @Test
    public void testInvalidPlayWithMissingPlayer() {
        assertThrows(AssertionError.class, () -> {
            Playable playable = new CheckersGame();
            Move move = Move.of(5, 5, 6, 6, false);
            playable.play(move);
        }, "Trying to play a move on an empty field should result in an assertion error");
    }

    @Test
    public void testInvalidPlayWithInvalidMove() {
        assertThrows(AssertionError.class, () -> {
            Playable playable = new CheckersGame();
            Move move = Move.of(5, 5, 7, 7, false);
            playable.play(move);
        }, "Trying to play an invalid move should result in an assertion error");
    }

    @Test
    public void testValidPlays() {
        Playable playable = new CheckersGame();
        playable = playable.setField(4, 4, PlayerType.BLACK);
        assertEquals(PlayerType.BLACK, ((CheckersGame) playable).field[4][4], "At (4/4) should be a Black Player");
        Move move = Move.of(4, 4, 5, 5, false);
        Move secondMove = Move.of(5, 5, 6, 6, false);
        playable = playable.play(move, secondMove);
        assertNull(((CheckersGame) playable).field[4][4], "At (4/4) should be null");
        assertNull(((CheckersGame) playable).field[5][5], "At (5/5) should be null");
        assertEquals(PlayerType.BLACK, ((CheckersGame) playable).field[6][6], "At (6/6) should be a Black Player");
    }

    @Test
    public void testQueenUpgrade() {
        Playable playable = new CheckersGame();
        playable = playable.setField(6, 6, PlayerType.BLACK);
        Move move = Move.of(6, 6, 7, 7, false);
        playable = playable.play(move);
        assertNull(((CheckersGame) playable).field[6][6], "At (6/6) should be null");
        assertEquals(PlayerType.BLACK_QUEEN, ((CheckersGame) playable).field[7][7], "At (6/6) should be a Black Queen Player");
    }
}
