package game;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class CheckersGameTest {
    
    @Test
    public void testQueen() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame = checkersGame.setField(0, 5, PlayerType.WHITE_QUEEN);
        ArrayList<Move> availableMoves = checkersGame.getAvaiableMoves(5, 5);
        assertTrue(availableMoves.isEmpty());
    }

    @Test
    public void testMonteCarlo() {
        CheckersGame game = new CheckersGame();
        game.setupNewGame();
        game.field
    }
}