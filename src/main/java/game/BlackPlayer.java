package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class BlackPlayer implements Player {

    protected int x, y;
    protected boolean isSelected = false;
    protected ArrayList<Move> moves;

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
        canvas.circle(35 + x * 70, 35 + y * 70, 40);
        if (isSelected) {
            drawMoves(canvas);
            canvas.strokeWeight(1);
        }
    }

    private void drawMoves(PApplet canvas) {
        for (Move move : moves) {
            canvas.noFill();
            canvas.strokeWeight(3);
            canvas.rect(move.xEnd()*70, move.yEnd()*70, 70, 70);
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

    @Override
    public boolean isWhite() {
        return false;
    }
}
