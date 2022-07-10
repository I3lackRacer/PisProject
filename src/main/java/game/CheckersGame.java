package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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
    public void setupNewGame() {
        field = new PlayerType[8][8];
        for (int x = 1; x < 8; x += 2) {
            field[x][0] = PlayerType.BLACK;
            field[x][2] = PlayerType.BLACK;
            field[x][6] = PlayerType.WHITE;
        }
        for (int x = 0; x < 8; x += 2) {
            field[x][1] = PlayerType.BLACK;
            field[x][5] = PlayerType.WHITE;
            field[x][7] = PlayerType.WHITE;
        }
    }

    public ArrayList<Move> getAvaiableMoves(int x, int y) {
        ArrayList<Move> moves = new ArrayList<>();
        PlayerType type = field[x][y];
        if (type == PlayerType.BLACK_QUEEN || type == PlayerType.WHITE_QUEEN) {
            moves.addAll(getAvaiableMovesDirection(x, y, 1, 1));
            moves.addAll(getAvaiableMovesDirection(x, y, -1, -1));
            moves.addAll(getAvaiableMovesDirection(x, y, 1, -1));
            moves.addAll(getAvaiableMovesDirection(x, y, -1, 1));
            return moves;
        }
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

    @Override
    public Move bestMove() {
        if (whoWon() != null) {
            return null;
        }
        ArrayList<Move> allMoves = getAllMoves(false);
        int bestMoveEval = Integer.MAX_VALUE;
        Move bestMove = null;
        System.out.println("Started Evaluation");
        for (Move m: allMoves) {
            System.out.println("Evaluating move (%d/%d) to (%d/%d)".formatted(m.xStart(), m.yStart(), m.xEnd(), m.yEnd()));
            int eval = minMax(this.play(m), false, 5, Integer.MIN_VALUE, Integer.MAX_VALUE);
            System.out.println("Evaluated move: " + eval);
            if (bestMoveEval > eval) {
                bestMove = m;
                bestMoveEval = eval;
            }
        }
        assert bestMove != null: "Best Move should never be zero";
        System.out.println("Finished Evaluation - Best Move: (%d/%d) to (%d/%d) eval: %d".formatted(bestMove.xStart(), bestMove.yStart(), bestMove.xEnd(), bestMove.yEnd(), bestMoveEval));
        return bestMove;
    }

    private Move randomMove(int x, int y) {
        ArrayList<Move> avaiableMoves = getAvaiableMoves(x, y);
        return avaiableMoves.get(rmd.nextInt(avaiableMoves.size()));
    }

    private ArrayList<Move> getAvaiableMovesDirection(int x, int y, int xMultiplicator, int yMultiplicator) {
        ArrayList<Move> moves = new ArrayList<>();
        PlayerType p = field[x][y];

        for (int i = 1; i < field.length; i++) {
            if (x + i * xMultiplicator < 0 || x + i * xMultiplicator >= field.length || y + i * yMultiplicator < 0 || y + i * yMultiplicator >= field.length)
                break;
            if (field[x + i * xMultiplicator][y + i * yMultiplicator] == null) {
                moves.add(Move.of(x, y, x + i * xMultiplicator, y + i * yMultiplicator));
            } else {
                if (x + (i + 1) * xMultiplicator >= 0 && x + (i + 1) * xMultiplicator < 8 && y + (i + 1) * yMultiplicator >= 0 && y + (i + 1) * yMultiplicator < 8 && field[x + (i + 1) * xMultiplicator][y + (i + 1) * yMultiplicator] == null && isEnemy(p, x + i * xMultiplicator, y + i * yMultiplicator)) {
                    moves.add(Move.of(x, y, x + i * xMultiplicator + xMultiplicator, y + i * yMultiplicator + yMultiplicator, true));
                }
                break;
            }
        }
        return moves;
    }

    boolean isEnemy(PlayerType type, int x, int y) {
        if (type == null || field[x][y] == null)
            return false;
        return ((type == PlayerType.WHITE || type == PlayerType.WHITE_QUEEN) && (field[x][y] == PlayerType.BLACK || field[x][y] == PlayerType.BLACK_QUEEN)) || ((type == PlayerType.BLACK || type == PlayerType.BLACK_QUEEN) && (field[x][y] == PlayerType.WHITE || field[x][y] == PlayerType.WHITE_QUEEN));
    }
    
    private static int minMax(CheckersGame instance, boolean isMaximizing, int depth, int alpha, int beta) {
        if (instance.whoWon() != null) {
            return instance.evaluateBoard();
        }
        if (depth == 0) {
            return instance.play(instance.monteCarlo(isMaximizing)).evaluateBoard();
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move m : instance.getAllMoves(true)) {
                int eval = minMax(instance.play(m), false, depth - 1, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        }

        int minEval = Integer.MAX_VALUE;
        for (Move m : instance.getAllMoves(false)) {
            int eval = minMax(instance.play(m), true, depth - 1, alpha, beta);
            minEval = Math.min(minEval, eval);
            alpha = Math.min(alpha, eval);
            if (beta <= alpha) {
                break;
            }
        }
        return minEval;
    }

    private int evaluateBoard() {
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
        CheckersGame newGame = new CheckersGame(this);
        newGame.field[move.xEnd()][move.yEnd()] = newGame.field[move.xStart()][move.yStart()];
        newGame.field[move.xStart()][move.yStart()] = null;
        PlayerType player = newGame.field[move.xEnd()][move.yEnd()];
        if (move.knockout()) {
            Location knockout = newGame.getKnockout(move);
            newGame = newGame.setField(knockout.x(), knockout.y(), null);
        }
        if(player == PlayerType.WHITE && move.yEnd() == 0) {
            newGame.field[move.xEnd()][move.yEnd()] = PlayerType.WHITE_QUEEN;
        }
        if (player == PlayerType.BLACK && move.yEnd() == 7) {
            newGame.field[move.xEnd()][move.yEnd()] = PlayerType.BLACK_QUEEN;
        }
        return newGame;
    }

    @Override
    public Location getKnockout(Move move) {
        assert move.knockout() : "There is no knockout to be found";
        int xDirection = move.xStart() > move.xEnd() ? -1 : 1;
        int yDirection = move.yStart() > move.yEnd() ? -1 : 1;
        for (int offset = 1; offset < Math.abs(move.xStart() - move.xEnd()); offset++) {
            if (field[move.xStart() + offset * xDirection][move.yStart() + offset * yDirection] != null) {
                logger.debug("Found knockout player for move (%d/%d) to (%d/%d) at (%d/%d)".formatted( move.xStart(), move.yStart(), move.xEnd(), move.yEnd(), move.xStart() + offset * xDirection, move.yStart() + offset * yDirection));
                return new Location(move.xStart() + offset * xDirection, move.yStart() + offset * yDirection);
            }
        }

        return null;
    }

    @Override
    public CheckersGame setField(int x, int y, PlayerType type) {
        CheckersGame checkersGame = new CheckersGame(this);
        checkersGame.field[x][y] = type;
        return checkersGame;
    }

    @Override
    public PlayerType whoWon() {
        boolean whitePlayers = false;
        boolean blackPlayers = false;
        for (PlayerType[] row : field)
            for (PlayerType cell : row) {
                if (cell == PlayerType.BLACK || cell == PlayerType.BLACK_QUEEN)
                    blackPlayers = true;
                else if (cell == PlayerType.WHITE || cell == PlayerType.WHITE_QUEEN)
                    whitePlayers = true;
            }
        if (!blackPlayers) {
            return PlayerType.WHITE;
        }
        if (!whitePlayers) {
            return PlayerType.BLACK;
        }

        return null;
    }

    private Move monteCarlo(boolean whiteTurn) {
        ArrayList<Location> playersPos = new ArrayList<>();
        for (int x = 0; x < field.length; x++) {
            for(int y = 0; y < field.length; y++) {
                if (whiteTurn && (field[x][y] == PlayerType.WHITE || field[x][y] == PlayerType.WHITE_QUEEN)) {
                    playersPos.add(new Location(x, y));
                } else if (!whiteTurn && (field[x][y] == PlayerType.BLACK || field[x][y] == PlayerType.BLACK_QUEEN)) {
                    playersPos.add(new Location(x, y));
                }
            }
        }
        Move bestMove = null;
        int bestMoveWins = Integer.MAX_VALUE;
        for (Location playerPos : playersPos) {
            for (Move move : getAvaiableMoves(playerPos.x(), playerPos.y())) {
                CheckersGame nextPlay = new CheckersGame(this).play(move);
                int wins = 0;
                for(int i = 0; i < 100; i++) {
                    wins += playGameRandom(whiteTurn, nextPlay);
                }
                if (bestMoveWins > wins) {
                    bestMoveWins = wins;
                    bestMove = move;
                }
            }
        }

        return bestMove;
    }

    private ArrayList<Move> getAllMoves(boolean whiteTurn) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < field.length; x++) {
            for(int y = 0; y < field.length; y++) {
                if (field[x][y] != null && (whiteTurn && !isEnemy(PlayerType.WHITE, x, y) || !whiteTurn && !isEnemy(PlayerType.BLACK, x, y)))
                    moves.addAll(getAvaiableMoves(x, y));
            }
        }
        return moves;
    }

    private static int playGameRandom(boolean whiteTurn, CheckersGame instance) {
        if (instance.whoWon() != null) {
            System.out.println("Finished");
            if (whiteTurn && instance.whoWon() == PlayerType.BLACK || !whiteTurn && instance.whoWon() == PlayerType.WHITE) {
                return -1;
            } 
            return 1;
        }

        ArrayList<Move> allMoves = instance.getAllMoves(whiteTurn);
        return playGameRandom(!whiteTurn, instance.play(allMoves.get(rmd.nextInt(allMoves.size()))));
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(" -  -  -  -  -  -  -  -\n");
        for (PlayerType[] row : field) {
            for (PlayerType cell : row) {
                if (cell == null) {
                    output.append("   ");
                    continue;
                }
                switch (cell) {
                    case BLACK:
                        output.append(" o ");
                        break;
                    case WHITE:
                        output.append(" x ");
                        break;
                    case WHITE_QUEEN:
                        output.append(" X ");
                    case BLACK_QUEEN:
                        output.append(" O ");
                    default:
                        output.append("   ");
                        break;
                }
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

    public static Move of(int xStart, int yStart, int xEnd, int yEnd) {
        return new Move(xStart, yStart, xEnd, yEnd, false);
    }
}

interface Playable {
    void setupNewGame();
    Playable setField(int x, int y, PlayerType type);
    Location getKnockout(Move move);
    ArrayList<Move> getAvaiableMoves(int x, int y);
    Playable play(Move move);
    PlayerType whoWon();
    Move bestMove();
}
