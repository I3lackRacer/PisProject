package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckersGame implements Playable {

    PlayerType[] field = new PlayerType[64];
    private static final Logger logger = LogManager.getLogger(CheckersGame.class);
    private static final Random rmd = new Random();

    public CheckersGame() {
    }

    public CheckersGame(CheckersGame game) {
        System.arraycopy(game.field, 0, field, 0, 64);
    }

    @Override
    public CheckersGame setupNewGame() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame.field = new PlayerType[64];
        for (int i = 0; i < 64; i += 8) {
            for (int j = 0; j < 8; j++) {
                if ((i / 8 + j) % 2 == 1) {
                    if (i < 24) checkersGame.field[i + j] = PlayerType.BLACK;
                    else if (i > 39) checkersGame.field[i + j] = PlayerType.WHITE;
                }
            }
        }
        logger.info("Setup New Game as followed:\n" + checkersGame);
        return checkersGame;
    }

    public ArrayList<Move> getAvailableMoves(int i) {
        assert i >= 0 && i < field.length : "index must be between 0 and 63";
        assert field[i] != null : "field should not be null";
        ArrayList<Move> moves = new ArrayList<>();
        PlayerType type = field[i];
        int x = i % 8;
        int y = i / 8;
        // Check moves for PlayerType WHITE and WHITE_QUEEN
        if (type != PlayerType.BLACK && type != PlayerType.BLACK_QUEEN) {
            if (y > 0) {
                if (x > 0) {
                    int targetIndex = i - 9;
                    if (field[targetIndex] == null)
                        moves.add(Move.of(i, targetIndex, false));
                    else if (isEnemy(type, targetIndex) && x > 1 && y > 1 && field[targetIndex - 9] == null)
                        moves.add(Move.of(i, targetIndex - 9, true));
                }
                if (x < 7) {
                    int targetIndex = i - 7;
                    if (field[targetIndex] == null)
                        moves.add(Move.of(i, targetIndex, false));
                    else if (isEnemy(type, targetIndex) && x < 6 && y > 1 && field[targetIndex - 7] == null)
                        moves.add(Move.of(i, targetIndex - 7, true));
                }
            }
        }
        // Check moves for PlayerType BLACK and BLACK_QUEEN
        if (type != PlayerType.WHITE && type != PlayerType.WHITE_QUEEN) {
            if (y < 7) {
                if (x > 0) {
                    int targetIndex = i + 7;
                    if (field[targetIndex] == null)
                        moves.add(Move.of(i, targetIndex, false));
                    else if (isEnemy(type, targetIndex) && x > 1 && y < 6 && field[targetIndex + 7] == null)
                        moves.add(Move.of(i, targetIndex + 7, true));
                }
                if (x < 7) {
                    int targetIndex = i + 9;
                    if (field[targetIndex] == null)
                        moves.add(Move.of(i, targetIndex, false));
                    else if (isEnemy(type, targetIndex) && x < 6 && y < 6 && field[targetIndex + 9] == null)
                        moves.add(Move.of(i, targetIndex + 9, true));
                }
            }
        }
        return moves;
    }

    private boolean isEnemy(PlayerType myType, int i) {
        return (myType == PlayerType.BLACK || myType == PlayerType.BLACK_QUEEN) && 
            (field[i] == PlayerType.WHITE || field[i] == PlayerType.WHITE_QUEEN) ||
            (myType == PlayerType.WHITE || myType == PlayerType.WHITE_QUEEN) && 
            (field[i] == PlayerType.BLACK || field[i] == PlayerType.BLACK_QUEEN);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0)
                builder.append('\n');
            if (field[i] == null)
                builder.append('.');
            else
                builder.append(field[i].getChar());
        }
        return builder.toString();
    }

    @Override
    public Playable makeMove(Move move) {
        CheckersGame game = new CheckersGame(this);
        game.field[move.from] = null;
        game.field[move.to] = field[move.from];
        if (Math.abs(move.to - move.from) == 18 || Math.abs(move.to - move.from) == 14)
            game.field[(move.to + move.from) / 2] = null;
        if (move.to < 8 && game.field[move.to] == PlayerType.WHITE)
            game.field[move.to] = PlayerType.WHITE_QUEEN;
        if (move.to > 55 && game.field[move.to] == PlayerType.BLACK)
            game.field[move.to] = PlayerType.BLACK_QUEEN;
        return game;
    }

    @Override
    public Playable[] getAllPossibleMoves(PlayerType playerType) {
        List<Playable> games = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            if (field[i] == playerType) {
                for (Move move : getAvailableMoves(i)) {
                    games.add(makeMove(move));
                }
            }
        }
        return games.toArray(new Playable[0]);
    }
}