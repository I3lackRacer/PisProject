package game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersGameTest {

    boolean m(int x, int y, ArrayList<Move> moves) {
        return moves.stream().anyMatch(move -> move.xEnd() == x && move.yEnd() == y);
    }

    @Test
    public void testQueenFiveFive() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame = checkersGame.setField(5, 5, PlayerType.WHITE_QUEEN);
        ArrayList<Move> availableMoves = checkersGame.getAvaiableMoves(5, 5);
        assertEquals(4, availableMoves.size());
        assertTrue(m(4, 4, availableMoves));
        assertTrue(m(6, 6, availableMoves));
        assertTrue(m(4, 6, availableMoves));
        assertTrue(m(6, 4, availableMoves));
    }

    @Test
    public void testQueenFiveFiveWithBlockedByEnemy() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame = checkersGame.setField(5, 5, PlayerType.WHITE_QUEEN);
        checkersGame = checkersGame.setField(4, 4, PlayerType.BLACK);
        ArrayList<Move> availableMoves = checkersGame.getAvaiableMoves(5, 5);
        assertEquals(4, availableMoves.size());
        assertTrue(m(6, 6, availableMoves));
        assertTrue(m(3, 3, availableMoves));
        assertTrue(m(4, 6, availableMoves));
        assertTrue(m(6, 4, availableMoves));
    }

    @Test
    public void testIsEnemy() {
        CheckersGame game = new CheckersGame();
        game = game.setField(0, 0, PlayerType.BLACK);
        game = game.setField(0, 7, PlayerType.WHITE);
        game = game.setField(7, 0, PlayerType.WHITE_QUEEN);
        game = game.setField(7, 7, PlayerType.BLACK_QUEEN);
        assertTrue(game.isEnemy(PlayerType.WHITE, 0, 0));
        assertTrue(game.isEnemy(PlayerType.WHITE_QUEEN, 0, 0));
        assertFalse(game.isEnemy(PlayerType.BLACK, 0, 0));
        assertFalse(game.isEnemy(PlayerType.BLACK_QUEEN, 0, 0));
        assertFalse(game.isEnemy(PlayerType.WHITE, 0, 7));
        assertFalse(game.isEnemy(PlayerType.WHITE_QUEEN, 0, 7));
        assertTrue(game.isEnemy(PlayerType.BLACK, 0, 7));
        assertTrue(game.isEnemy(PlayerType.BLACK_QUEEN, 0, 7));
        assertFalse(game.isEnemy(PlayerType.WHITE, 7, 0));
        assertFalse(game.isEnemy(PlayerType.WHITE_QUEEN, 7, 0));
        assertTrue(game.isEnemy(PlayerType.BLACK, 7, 0));
        assertTrue(game.isEnemy(PlayerType.BLACK_QUEEN, 7, 0));
        assertTrue(game.isEnemy(PlayerType.WHITE, 7, 7));
        assertTrue(game.isEnemy(PlayerType.WHITE_QUEEN, 7, 7));
        assertFalse(game.isEnemy(PlayerType.BLACK, 7, 7));
        assertFalse(game.isEnemy(PlayerType.BLACK_QUEEN, 7, 7));
        assertFalse(game.isEnemy(PlayerType.BLACK_QUEEN, 5, 5));
    }

    @Test
    public void testGetKnockout() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame = checkersGame.setField(5, 5, PlayerType.WHITE);
        Move move = new Move(4, 4, 6, 6, true);
        Location knockout = checkersGame.getKnockout(move);
        assertEquals(5, knockout.x());
        assertEquals(5, knockout.y());
        move = new Move(4, 6, 6, 4, true);
        knockout = checkersGame.getKnockout(move);
        assertEquals(5, knockout.x());
        assertEquals(5, knockout.y());
        move = new Move(0, 0, 7, 7, true);
        knockout = checkersGame.getKnockout(move);
        assertEquals(5, knockout.x());
        assertEquals(5, knockout.y());
        move = new Move(0, 0, 1, 1, true);
        knockout = checkersGame.getKnockout(move);
        assertNull(knockout);
    }

    @Test
    public void testMonteCarlo() {
        CheckersGame game = new CheckersGame();
        game = game.setField(4, 4, PlayerType.BLACK);
        game = game.setField(5, 5, PlayerType.WHITE);
        Move move = game.monteCarlo(true);
        assertEquals(5, move.xStart());
        assertEquals(5, move.yStart());
        assertEquals(3, move.xEnd());
        assertEquals(3, move.yEnd());
    }
}