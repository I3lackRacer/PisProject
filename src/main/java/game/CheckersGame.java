package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

import javax.lang.model.util.ElementScanner14;

public class CheckersGame implements Playable{

    PlayerType[][] field = new PlayerType[8][8];
    static final Logger logger = LogManager.getLogger(CheckersGame.class);
    static Random rmd = new Random();

    public CheckersGame() {
    }

    public CheckersGame(CheckersGame game) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                field[x][y] = game.field[x][y];
            }
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
            field[x][5] = PlayerType.WHITE;
            field[x][7] = PlayerType.WHITE;
            field[x][1] = PlayerType.BLACK;
        }
    }

    ArrayList<Move> getAvaiableMoves(int x, int y) {
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

    private ArrayList<Move> getAvaiableMovesDirection(int x, int y, int xMultiplicator, int yMultiplicator) {
        ArrayList<Move> moves = new ArrayList<>();
        PlayerType p = field[x][y];
        
        System.out.printf("Player (%d/%d) %d - %d %n", x, y, xMultiplicator, yMultiplicator);
        for (int i = 1; i < field.length; i++) {
            System.out.printf("(%d/%d)%n", x + i*xMultiplicator, y + i*yMultiplicator);
            if (x + i*xMultiplicator < 0 || x + i * xMultiplicator > field.length || y + i*yMultiplicator < 0 || y + i*yMultiplicator > field.length)
                break;
            if (field[x + i*xMultiplicator][y + i*yMultiplicator] == null) {
                moves.add(Move.of(x, y, x + i * xMultiplicator, y + i * yMultiplicator));
            } else {
                if ( x + i*xMultiplicator + 1 >= 0 && x + i*xMultiplicator + 1 < 8 && y + i*yMultiplicator + 1 >= 0 && y + i*yMultiplicator + 1 < 8 && field[x + i*xMultiplicator + 1][y + i*yMultiplicator + 1] == null && isEnemy(p, x + i * xMultiplicator, y + i * yMultiplicator)) {
                    moves.add(Move.of(x, y, x + i * xMultiplicator + 1, y + i * yMultiplicator + 1, true));
                }
                break;
            }
        }
        return moves;
    }

    boolean isEnemy(PlayerType type, int x, int y) {
        if (type == null) 
            return false;
        return ((type == PlayerType.WHITE || type == PlayerType.WHITE_QUEEN) && (field[x][y] == PlayerType.BLACK || field[x][y] == PlayerType.BLACK_QUEEN)) || ((type == PlayerType.BLACK || type == PlayerType.BLACK_QUEEN) && (field[x][y] == PlayerType.WHITE || field[x][y] == PlayerType.WHITE_QUEEN));
    }

    public Move bestMove() {
        ArrayList<Location> playersPos = new ArrayList<>();
        Move bestMove = null;
        int bestMoveEval = Integer.MAX_VALUE;
        for (Location playerPos : playersPos) {
            ArrayList<Move> avaiableMoves = getAvaiableMoves(playerPos.x(), playerPos.y());
            for (Move move : avaiableMoves) {
                int eval = minMax(this.play(move), false, 0);
                if (eval < bestMoveEval) {
                    bestMove = move;
                    bestMoveEval = eval;
                }
            }
        }
        
        logger.info("Found best move via alpha-beta-pruning from (%d/%d) to (%d/%d) - Evaluation: %d".formatted(bestMove.xStart(), bestMove.yStart(), bestMove.xEnd(), bestMove.yEnd(), bestMoveEval));
        return bestMove;
    }

    
    private static int minMax(CheckersGame instance, boolean isMaximizing, int depth) {
        ArrayList<Location> playersPos = new ArrayList<>();
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (Location playerPos : playersPos) {
            possibleMoves.addAll(instance.getAvaiableMoves(playerPos.x(), playerPos.y()));
        }
        int value = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        if (instance.whoWon() != null || possibleMoves.isEmpty())
            return instance.evaluateBoard();
        for (Move currentMove: possibleMoves) {
            CheckersGame nextPlay = instance.play(currentMove);
            int resultingValue = value;
            if (depth < 10 && depth > -10)
                 resultingValue = minMax(nextPlay, !isMaximizing, depth - 1);
            if (isMaximizing) {
                value = Math.max(resultingValue, value);
                if (value == 1000 - depth)
                    return value;
            } else {
                value = Math.min(resultingValue, value);
                if (value == -1000 + depth)
                    return value;
            }
        }
        return value;
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
            Location knockout = getKnockout(move);
            newGame.field[knockout.x()][knockout.y()] = null;
        }
        if(player == PlayerType.WHITE && move.yEnd() == 0) {
            newGame.field[move.xEnd()][move.yEnd()] = PlayerType.WHITE_QUEEN;
        }
        if(player == PlayerType.BLACK && move.yEnd() == 7) {
            newGame.field[move.xEnd()][move.yEnd()] = PlayerType.BLACK_QUEEN;
        }
        return newGame;
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
    public CheckersGame setField(int x, int y, PlayerType type) {
        CheckersGame checkersGame = new CheckersGame(this);
        checkersGame.field[x][y] = type;
        return checkersGame;
    }

    @Override
    public PlayerType whoWon() {
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
        int bestMoveWins = Integer.MIN_VALUE;
        for (Location playerPos : playersPos) {
            for (Move move : getAvaiableMoves(playerPos.x(), playerPos.y())) {
                CheckersGame nextPlay = new CheckersGame(this).play(move);
                int wins = 0;
                for(int i = 0; i < 1000; i++) {
                    wins += playGameRandom(whiteTurn, nextPlay);
                }
                if (bestMoveWins < wins) {
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
                moves.addAll(getAvaiableMoves(x, y));
            }
        }
        return moves;
    }

    private static int playGameRandom(boolean whiteTurn, CheckersGame instance) {
        if (instance.whoWon() != null) {
            if (whiteTurn && instance.whoWon() == PlayerType.BLACK || !whiteTurn && instance.whoWon() == PlayerType.WHITE) {
                return -1;
            } 
            return 1;
        }

        ArrayList<Move> allMoves = instance.getAllMoves(whiteTurn);
        return playGameRandom(whiteTurn, instance.play(allMoves.get(rmd.nextInt(allMoves.size()))));
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (PlayerType[] row : field) {
            for (PlayerType cell : row) {
                switch (cell) {
                    case BLACK:
                        output.append("o");
                        break;
                    case WHITE:
                        output.append("x");
                        break;
                    case WHITE_QUEEN:
                        output.append("X");
                    case BLACK_QUEEN:
                        output.append("O");
                    default:
                        output.append(" ");
                        break;
                }
            }
            output.append("\n");
        }

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
