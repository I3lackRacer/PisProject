package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Board {

    private Player[][] gameField = new Player[8][8];
    private ArrayList<Player> players = new ArrayList<>();
    private Playable backend;
    private Player selected = null;

    public Board(Playable backend) {
        this.backend = backend;
    }

    public void newGame() {
        gameField = new Player[8][8];
        for (int x = 1; x < 8; x += 2) {
            addPlayer(new BlackPlayer(x, 0));
            addPlayer(new BlackPlayer(x, 2));
            addPlayer(new WhitePlayer(x, 6));
        }
        for (int x = 0; x < 8; x += 2) {
            addPlayer(new WhitePlayer(x, 5));
            addPlayer(new WhitePlayer(x, 7));
            addPlayer(new BlackPlayer(x, 1));
        }
    }

    public void addPlayer(Player player) {
        assert player.getX() >= 0 && player.getX() < 8 && player.getY() >= 0 && player.getY() < 8;
        gameField[player.getX()][player.getY()] = player;
        players.add(player);
    }

    public void play(Move move) {
        Player p = getPlayer(move.xStart(), move.yStart());
        p.move(move);
        gameField[move.xEnd()][move.yEnd()] = p;
        gameField[move.xStart()][move.yStart()] = null;
        if (move.knockout()) {
            Location deadPlayerPos = backend.getKnockout(move);
            gameField[deadPlayerPos.x()][deadPlayerPos.y()] = null;
            players.removeIf((player) -> {return player.getX() == deadPlayerPos.x() && player.getY() == deadPlayerPos.y();});
            backend.setField(deadPlayerPos.x(), deadPlayerPos.y(), null);
        }
        backend = backend.play(move);
        selected.setSelected(false);
        selected = null;
    }

    private Player getPlayer(int xStart, int yStart) {
        return gameField[xStart][yStart];
    }

    public void draw(PApplet canvas) {
        for (Player player : players) {
            player.draw(canvas);
        }
    }

    public Player select(int x, int y) {
        if (selected != null)
            selected.setSelected(false);
        selected = gameField[x][y];
        if (selected != null) {
            selected.setSelected(true);
            ArrayList<Move> moves = backend.getAvaiableMoves(x, y);
            selected.avaiableMoves(moves);
        }
        
        return selected;
    }
}