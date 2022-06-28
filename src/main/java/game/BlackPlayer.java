package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class BlackPlayer implements Player {

    private int x, y;
    private boolean isSelected = false;
    private ArrayList<Move> moves;

    public BlackPlayer(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(Move move) {
        this.x = move.xEnd();
        this.y = move.yEnd();
        moves = null;
    }

    @Override
    public void draw(PApplet canvas) {
        canvas.fill(0);
        canvas.stroke(0);
        if (isSelected) {
            canvas.strokeWeight(3);
            canvas.stroke(canvas.color(255, 0, 0));
        }
        canvas.circle(25 + x * 50, 25 + y * 50, 35);
        if (isSelected) {
            drawMoves(canvas);
            canvas.strokeWeight(1);
        }
    }

    private void drawMoves(PApplet canvas) {
        for (Move move : moves) {
            canvas.noFill();
            canvas.strokeWeight(3);
            canvas.rect(move.xEnd()*50, move.yEnd()*50, 50, 50);
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.BLACK;
    }

    @Override
    public void avaiableMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    @Override
    public ArrayList<Move> getMoves() {
        return moves;
    }
}
