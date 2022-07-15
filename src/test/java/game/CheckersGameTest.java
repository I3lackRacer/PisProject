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
        assertEquals(4, availableMoves.size(), "The size should be 4");
        assertTrue(m(4, 4, availableMoves), "(4/4) should be an allowed move");
        assertTrue(m(6, 6, availableMoves), "(6/6) should be an allowed move");
        assertTrue(m(4, 6, availableMoves), "(4/6) should be an allowed move");
        assertTrue(m(6, 4, availableMoves), "(6/4) should be an allowed move");
    }

    @Test
    public void testQueenFiveFiveWithBlockedByEnemy() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame = checkersGame.setField(5, 5, PlayerType.WHITE_QUEEN);
        checkersGame = checkersGame.setField(4, 4, PlayerType.BLACK);
        ArrayList<Move> availableMoves = checkersGame.getAvaiableMoves(5, 5);
        assertEquals(4, availableMoves.size());
        assertTrue(m(6, 6, availableMoves), "(6/6) should be an allowed move");
        assertTrue(m(3, 3, availableMoves), "(3/3) should be an allowed move");
        assertTrue(m(4, 6, availableMoves), "(4/6) should be an allowed move");
        assertTrue(m(6, 4, availableMoves), "(6/4) should be an allowed move");
    }

    @Test
    public void testIsEnemy() {
        CheckersGame game = new CheckersGame();
        game = game.setField(0, 0, PlayerType.BLACK);
        game = game.setField(0, 7, PlayerType.WHITE);
        game = game.setField(7, 0, PlayerType.WHITE_QUEEN);
        game = game.setField(7, 7, PlayerType.BLACK_QUEEN);
        assertTrue(game.isEnemy(PlayerType.WHITE, 0, 0), "(0/0) should be an enemy");
        assertTrue(game.isEnemy(PlayerType.WHITE_QUEEN, 0, 0), "(0/0) should be an enemy");
        assertFalse(game.isEnemy(PlayerType.BLACK, 0, 0), "(0/0) should not be an enemy");
        assertFalse(game.isEnemy(PlayerType.BLACK_QUEEN, 0, 0), "(0/0) should not be an enemy");
        assertFalse(game.isEnemy(PlayerType.WHITE, 0, 7), "(0/7) should not be an enemy");
        assertFalse(game.isEnemy(PlayerType.WHITE_QUEEN, 0, 7), "(0/7) should not be an enemy");
        assertTrue(game.isEnemy(PlayerType.BLACK, 0, 7), "(0/7) should be an enemy");
        assertTrue(game.isEnemy(PlayerType.BLACK_QUEEN, 0, 7), "(0/7) should be an enemy");
        assertFalse(game.isEnemy(PlayerType.WHITE, 7, 0), "(7/0) should not be an enemy");
        assertFalse(game.isEnemy(PlayerType.WHITE_QUEEN, 7, 0), "(7/0) should not be an enemy");
        assertTrue(game.isEnemy(PlayerType.BLACK, 7, 0), "(7/0) should be an enemy");
        assertTrue(game.isEnemy(PlayerType.BLACK_QUEEN, 7, 0), "(7/0) should be an enemy");
        assertTrue(game.isEnemy(PlayerType.WHITE, 7, 7), "(7/7) should be an enemy");
        assertTrue(game.isEnemy(PlayerType.WHITE_QUEEN, 7, 7), "(7/7) should be an enemy");
        assertFalse(game.isEnemy(PlayerType.BLACK, 7, 7), "(7/7) should not be an enemy");
        assertFalse(game.isEnemy(PlayerType.BLACK_QUEEN, 7, 7), "(7/7) should not be an enemy");
        assertFalse(game.isEnemy(PlayerType.BLACK_QUEEN, 5, 5), "(7/7) should not be an enemy");
    }

    @Test
    public void testGetKnockout() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame = checkersGame.setField(5, 5, PlayerType.WHITE);
        Move move = new Move(4, 4, 6, 6, true);
        Location knockout = checkersGame.getKnockout(move);
        assertEquals(5, knockout.x(), "knockout coord x should be 5");
        assertEquals(5, knockout.y(), "knockout coord y should be 5");
        move = new Move(4, 6, 6, 4, true);
        knockout = checkersGame.getKnockout(move);
        assertEquals(5, knockout.x(), "knockout coord x should be 5");
        assertEquals(5, knockout.y(), "knockout coord y should be 5");
        move = new Move(0, 0, 7, 7, true);
        knockout = checkersGame.getKnockout(move);
        assertEquals(5, knockout.x(), "knockout coord x should be 5");
        assertEquals(5, knockout.y(), "knockout coord y should be 5");
        move = new Move(0, 0, 1, 1, true);
        knockout = checkersGame.getKnockout(move);
        assertNull(knockout, "There should be no knockout found");
    }

    @Test
    public void testMonteCarlo() {
        CheckersGame game = new CheckersGame();
        game = game.setField(4, 4, PlayerType.BLACK);
        game = game.setField(5, 5, PlayerType.WHITE);
        Move move = game.monteCarlo(true);
        assertEquals(5, move.xStart(), "Start X coord should be 5");
        assertEquals(5, move.yStart(), "Start Y coord should be 5");
        assertEquals(3, move.xEnd(), "End X coord should be 5");
        assertEquals(3, move.yEnd(), "End Y coord should be 5");
    }
}