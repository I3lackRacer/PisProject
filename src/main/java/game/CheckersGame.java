package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckersGame implements Playable {

    PlayerType[][] field = new PlayerType[8][8];
    private static final Logger logger = LogManager.getLogger(CheckersGame.class);
    private static final Random rmd = new Random();

    public CheckersGame() {
    }

    public CheckersGame(CheckersGame game) {
        for (int x = 0; x < 8; x++) {
            System.arraycopy(game.field[x], 0, field[x], 0, 8);
        }
    }

    @Override
    public CheckersGame setupNewGame() {
        CheckersGame checkersGame = new CheckersGame();
        checkersGame.field = new PlayerType[8][8];
        for (int x = 1; x < 8; x += 2) {
            checkersGame.field[x][0] = PlayerType.BLACK;
            checkersGame.field[x][2] = PlayerType.BLACK;
            checkersGame.field[x][6] = PlayerType.WHITE;
        }
        for (int x = 0; x < 8; x += 2) {
            checkersGame.field[x][1] = PlayerType.BLACK;
            checkersGame.field[x][5] = PlayerType.WHITE;
            checkersGame.field[x][7] = PlayerType.WHITE;
        }
        logger.info("Setup New Game as followed:\n" + checkersGame);
        return checkersGame;
    }

    public ArrayList<Move> getAvaiableMoves(int x, int y) {
        assert x >= 0 && x < field.length && y >= 0 && y < field.length : "x and y must be between 0 and 7";
        assert field[x][y] != null : "field should not be null";
        ArrayList<Move> moves = new ArrayList<>();
        PlayerType type = field[x][y];
        if (x > 0) {
            if (y > 0 && type != PlayerType.BLACK) {
                PlayerType corner = field[x - 1][y - 1];
                if (corner == null) moves.add(Move.of(x, y, x - 1, y - 1, false));
                if (isEnemy(type, x - 1, y - 1) && x > 1 && y >= 2 && field[x - 2][y - 2] == null)
                    moves.add(Move.of(x, y, x - 2, y - 2, true));
            }
            if (y < 7 && type != PlayerType.WHITE) {
                PlayerType corner = field[x - 1][y + 1];
                if (corner == null) moves.add(Move.of(x, y, x - 1, y + 1, false));
                if (isEnemy(type, x - 1, y + 1) && x > 1 && y < 6 && field[x - 2][y + 2] == null)
                    moves.add(Move.of(x, y, x - 2, y + 2, true));
            }
        }
        if (x < 7) {
            if (y > 0 && type != PlayerType.BLACK) {
                PlayerType corner = field[x + 1][y - 1];
                if (corner == null) moves.add(Move.of(x, y, x + 1, y - 1, false));
                if (isEnemy(type, x + 1, y - 1) && x < 6 && y >= 2 && field[x + 2][y - 2] == null)
                    moves.add(Move.of(x, y, x + 2, y - 2, true));
            }
            if (y < 7 && type != PlayerType.WHITE) {
                PlayerType corner = field[x + 1][y + 1];
                if (corner == null) moves.add(Move.of(x, y, x + 1, y + 1, false));
                if (isEnemy(type, x + 1, y + 1) && x < 6 && y < 6 && field[x + 2][y + 2] == null)
                    moves.add(Move.of(x, y, x + 2, y + 2, true));
            }
        }
        return moves;
    }

    @Override
    public Move bestMove() {
        if (whoWon() != null) return null;
        ArrayList<Move> allMoves = getAllMoves(false);
        int bestMoveEval = Integer.MAX_VALUE;
        List<Move> bestMoves = new ArrayList<>();
        logger.info("Started Evaluation");
        for (Move m : allMoves) {
            logger.debug("Evaluating move (%d/%d) to (%d/%d)".formatted(m.xStart(), m.yStart(), m.xEnd(), m.yEnd()));
            int eval = minMax(this.play(m), false, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
            logger.debug("Evaluated move: " + eval);
            if (bestMoveEval > eval) {
                bestMoves.clear();
                bestMoves.add(m);
                bestMoveEval = eval;
            }
            if (bestMoveEval == eval) bestMoves.add(m);
        }
        assert bestMoves.size() != 0 : "Best Moves should never be zero";
        Move bestMove = bestMoves.get(rmd.nextInt(bestMoves.size()));
        if (bestMoves.stream().anyMatch(Move::knockout)) {
            bestMoves = bestMoves.stream().filter(Move::knockout).toList();
            bestMove = bestMoves.get(rmd.nextInt(bestMoves.size()));
        }
        logger.info("Finished Evaluation - Best Move: (%d/%d) to (%d/%d) eval: %d".formatted(bestMove.xStart(), bestMove.yStart(), bestMove.xEnd(), bestMove.yEnd(), bestMoveEval));
        return bestMove;
    }

    boolean isEnemy(PlayerType type, int x, int y) {
        if (type == null || field[x][y] == null)
            return false;
        return ((type == PlayerType.WHITE || type == PlayerType.WHITE_QUEEN) && (field[x][y] == PlayerType.BLACK || field[x][y] == PlayerType.BLACK_QUEEN)) || ((type == PlayerType.BLACK || type == PlayerType.BLACK_QUEEN) && (field[x][y] == PlayerType.WHITE || field[x][y] == PlayerType.WHITE_QUEEN));
    }

    static int minMax(CheckersGame instance, boolean isMaximizing, int depth, int alpha, int beta) {
        if (instance.whoWon() != null) return instance.evaluateBoard();
        if (depth == 0) return instance.play(instance.monteCarlo(isMaximizing)).evaluateBoard();
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move m : instance.getAllMoves(true)) {
                int eval = minMax(instance.play(m), m.knockout(), depth - 1, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        }

        int minEval = Integer.MAX_VALUE;
        for (Move m : instance.getAllMoves(false)) {
            int eval = minMax(instance.play(m), !m.knockout(), depth - 1, alpha, beta);
            minEval = Math.min(minEval, eval);
            alpha = Math.min(alpha, eval);
            if (beta <= alpha) break;
        }
        return minEval;
    }

    int evaluateBoard() {
        if (whoWon() != null) {
            return whoWon() == PlayerType.BLACK ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }

        int sum = 0;
        for (PlayerType[] row : field) {
            for (PlayerType cell : row) {
                if (cell == PlayerType.BLACK || cell == PlayerType.BLACK_QUEEN)
                    sum--;
                else if (cell == PlayerType.WHITE || cell == PlayerType.WHITE_QUEEN)
                    sum++;
            }
        }

        return sum;
    }

    @Override
    public CheckersGame play(Move move) {
        // This assert is only for playing in jshell, because this should never be able to occour with the UI.
        assert getAvaiableMoves(move.xStart(), move.yStart()).stream().anyMatch(allowedMove -> allowedMove.xEnd() == move.xEnd() && allowedMove.yEnd() == move.yEnd()) : "Move must be an allowed move";
        CheckersGame newGame = new CheckersGame(this);
        newGame.field[move.xEnd()][move.yEnd()] = newGame.field[move.xStart()][move.yStart()];
        newGame.field[move.xStart()][move.yStart()] = null;
        PlayerType player = newGame.field[move.xEnd()][move.yEnd()];
        if (move.knockout()) {
            Location knockout = newGame.getKnockout(move);
            newGame = newGame.setField(knockout.x(), knockout.y(), null);
        }
        if (player == PlayerType.WHITE && move.yEnd() == 0)
            newGame.field[move.xEnd()][move.yEnd()] = PlayerType.WHITE_QUEEN;
        if (player == PlayerType.BLACK && move.yEnd() == 7)
            newGame.field[move.xEnd()][move.yEnd()] = PlayerType.BLACK_QUEEN;
        return newGame;
    }

    @Override
    public Location getKnockout(Move move) {
        assert move != null && move.knockout() : "There is no knockout to be found";
        int xDirection = move.xStart() > move.xEnd() ? -1 : 1;
        int yDirection = move.yStart() > move.yEnd() ? -1 : 1;
        for (int offset = 1; offset < Math.abs(move.xStart() - move.xEnd()); offset++) {
            if (field[move.xStart() + offset * xDirection][move.yStart() + offset * yDirection] != null) {
                // This log is spamming so I Removed it from the important log levels. But i still needed it in for debugging purposes. :)
                logger.trace("Found knockout player for move (%d/%d) to (%d/%d) at (%d/%d)".formatted(move.xStart(), move.yStart(), move.xEnd(), move.yEnd(), move.xStart() + offset * xDirection, move.yStart() + offset * yDirection));
                return new Location(move.xStart() + offset * xDirection, move.yStart() + offset * yDirection);
            }
        }

        return null;
    }

    @Override
    public CheckersGame setField(int x, int y, PlayerType type) {
        assert x >= 0 && x < field.length && y >= 0 && y < field.length : "x and y must be between 0 and 7";
        CheckersGame checkersGame = new CheckersGame(this);
        checkersGame.field[x][y] = type;
        return checkersGame;
    }

    @Override
    public PlayerType whoWon() {
        if (getAllMoves(true).isEmpty())
            return PlayerType.BLACK;
        if (getAllMoves(false).isEmpty())
            return PlayerType.WHITE;
        return null;
    }

    Move monteCarlo(boolean whiteTurn) {
        Move bestMove = null;
        int bestMoveWins = whiteTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            for (Move move : getAllMoves(whiteTurn)) {
                CheckersGame nextPlay = new CheckersGame(this).play(move);
                int wins = 0;
                for (int i = 0; i < 1; i++) {
                    wins += playGameRandom(!whiteTurn, nextPlay);
                }
                if (whiteTurn && wins > bestMoveWins) {
                    bestMoveWins = wins;
                    bestMove = move;
                }
                if (!whiteTurn && wins < bestMoveWins) {
                    bestMoveWins = wins;
                    bestMove = move;
                }
                // This is for understanding the Monte carlo better but it is spamming so its on the trace level
                logger.trace("Monte Carlo: Move (%d/%d) to (%d/%d) - wins: %d".formatted(move.xStart(), move.yStart(), move.xEnd(), move.yEnd(), wins));
            }
        return bestMove;
    }

    private ArrayList<Move> getAllMoves(boolean whiteTurn) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                if (field[x][y] != null)
                    if (whiteTurn && isEnemy(PlayerType.BLACK, x, y) || (!whiteTurn && isEnemy(PlayerType.WHITE, x, y)))
                        moves.addAll(getAvaiableMoves(x, y));
            }
        }
        return moves;
    }

    private static int playGameRandom(boolean whiteTurn, CheckersGame instance) {
        CheckersGame randomGameInstance = new CheckersGame(instance);
        int moveCounter = 0;
        while (randomGameInstance.whoWon() == null) {
            if (moveCounter > 100) {
                return 0;
            }
            moveCounter++;
            ArrayList<Move> allMoves = randomGameInstance.getAllMoves(whiteTurn);
            Move move = allMoves.get(rmd.nextInt(allMoves.size()));
            randomGameInstance = randomGameInstance.play(move);
            whiteTurn = !whiteTurn;
        }
        if (randomGameInstance.whoWon() == PlayerType.WHITE) return 1;
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(" -  -  -  -  -  -  -  -\n");
        for (int x = 0; x < field.length; x++) {
            for (PlayerType[] playerTypes : field) {
                PlayerType cell = playerTypes[x];
                output.append("|");
                if (cell == null) {
                    output.append(" ");
                } else {
                    switch (cell) {
                        case BLACK -> output.append("o");
                        case WHITE -> output.append("x");
                        case WHITE_QUEEN -> output.append("X");
                        case BLACK_QUEEN -> output.append("O");
                        default -> output.append(" ");
                    }
                }
                output.append("|");
            }
            output.append("\n");
        }
        output.append(" -  -  -  -  -  -  -  -\n");
        return output.toString();
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
}

interface Playable {
    Playable setupNewGame();

    Playable setField(int x, int y, PlayerType type);

    Location getKnockout(Move move);

    ArrayList<Move> getAvaiableMoves(int x, int y);

    Playable play(Move move);

    PlayerType whoWon();

    Move bestMove();

    default boolean isGameOver() {
        return whoWon() != null;
    }

    default Playable play(Move... move) {
        Playable playable = this;
        for (Move m : move) {
            playable = playable.play(m);
        }
        return playable;
    }
}
