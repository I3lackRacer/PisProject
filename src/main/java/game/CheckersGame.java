package game;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckersGame implements Playable{

    PlayerType[][] field = new PlayerType[8][8];
    private static final Logger logger = LogManager.getLogger(CheckersGame.class);

    public CheckersGame() {
        setupNewGame();
    }

    public CheckersGame(CheckersGame game) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                field[x][y] = game.field[x][y];
            }
        }
        logger.info("Backend generated new checkers game");
    }

    public CheckersGame copy(CheckersGame game) {
        return new CheckersGame(game);
    }

    private void setupNewGame() {
        for (int x = 1; x < 8; x += 2) {
            field[x][0] = PlayerType.BLACK;
            field[x][2] = PlayerType.BLACK;
            field[x][6] = PlayerType.WHITE;
        }
        for (int x = 0; x < 8; x += 2) {
            field[x][5] = PlayerType.WHITE;
            field[x][7] = PlayerType.WHITE;
            field[x][1] = PlayerType.BLACK;
        }
    }

    public ArrayList<Move> getAvaiableMoves(int x, int y) {
        ArrayList<Move> moves = new ArrayList<>();
        PlayerType type = field[x][y];
        if (x > 0) {
            if (y > 0) {
                PlayerType corner = field[x - 1][y - 1];
                if (corner == null) {
                    moves.add(Move.of(x, y, x - 1, y - 1));
                }
                if (isEnemy(type, x - 1, y - 1) && x > 1 && y >= 2 && field[x - 2][y - 2] == null) {
                    moves.add(Move.of(x, y, x - 2, y - 2, true));
                }
            }
            if (y < 7) {
                PlayerType corner = field[x - 1][y + 1];
                if (corner == null) {
                    moves.add(Move.of(x, y, x - 1, y + 1));
                }
                if (isEnemy(type, x - 1, y + 1) && x > 1 && y < 6 && field[x - 2][y + 2] == null) {
                    moves.add(Move.of(x, y, x - 2, y + 2, true));
                }
            }
        }
        if (x < 7) {
            if (y > 0) {
                PlayerType corner = field[x + 1][y - 1];
                if (corner == null) {
                    moves.add(Move.of(x, y, x + 1, y - 1));
                }
                if (isEnemy(type, x + 1, y - 1) && x < 6 && y >= 2 && field[x + 2][y - 2] == null) {
                    moves.add(Move.of(x, y, x + 2, y - 2, true));
                }
            }
            if (y < 7) {
                PlayerType corner = field[x + 1][y + 1];
                if (corner == null) {
                    moves.add(Move.of(x, y, x + 1, y + 1));
                }
                if (isEnemy(type, x + 1, y + 1) && x < 6 && y < 6 && field[x + 2][y + 2] == null) {
                    moves.add(Move.of(x, y, x + 2, y + 2, true));
                }
            }
        }
        return moves;
    }

    private boolean isEnemy(PlayerType type, int x, int y) {
        return ((type == PlayerType.WHITE || type == PlayerType.WHITE_QUEEN) && (field[x][y] == PlayerType.BLACK || field[x][y] == PlayerType.BLACK_QUEEN)) || ((type == PlayerType.BLACK || type == PlayerType.BLACK_QUEEN) && (field[x][y] == PlayerType.WHITE || field[x][y] == PlayerType.WHITE_QUEEN));
    }

    @Override
    public Playable play(Move move) {
        CheckersGame newGame = new CheckersGame(this);
        newGame.field[move.xEnd()][move.yEnd()] = newGame.field[move.xStart()][move.yStart()];
        newGame.field[move.xStart()][move.yStart()] = null;
        
        return newGame;
    }

    public void undo(Move move) {
        field[move.yStart()][move.yStart()] = field[move.xEnd()][move.yEnd()];
        field[move.xEnd()][move.yEnd()] = null;
    }

    @Override
    public Location getKnockout(Move move) {
        int xDirection = move.xStart() > move.xEnd() ? -1 : 1;
        int yDirection = move.yStart() > move.yEnd() ? -1 : 1;
        for(int offset = 1; offset <  Math.abs(move.xStart() - move.xEnd()); offset++) {
            System.out.println("X: " + move.xStart() + offset * xDirection + "Y: " + move.yStart() + offset * yDirection);
            if (field[move.xStart() + offset * xDirection][move.yStart() + offset * yDirection] != null) {
                return new Location(move.xStart() + offset * xDirection, move.yStart() + offset * yDirection);
            }
        }

        return null;
    }

    @Override
    public void setField(int x, int y, PlayerType type) {
        field[x][y] = type;
    }
}

record Location(int x, int y) {
}

enum PlayerType {
    WHITE,
    BLACK,
    WHITE_QUEEN,
    BLACK_QUEEN,
}

record Move(int xStart, int yStart, int xEnd, int yEnd, boolean knockout) {

    public static Move of(int xStart, int yStart, int xEnd, int yEnd, boolean knockout) {
        return new Move(xStart, yStart, xEnd, yEnd, knockout);
    }

    public static Move of(int xStart, int yStart, int xEnd, int yEnd) {
        return new Move(xStart, yStart, xEnd, yEnd, false);
    }
}
