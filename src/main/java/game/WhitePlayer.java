package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class WhitePlayer implements Player {

    protected int x, y;
    protected boolean isSelected = false;
    protected ArrayList<Move> moves;

    public WhitePlayer(int x, int y) {
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
        canvas.stroke(255);
        if (isSelected) {
            canvas.strokeWeight(3);
            canvas.stroke(canvas.color(255, 0, 0));
        }
        canvas.fill(255);
        canvas.circle(25 + x*50, 25 + y*50, 35);
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
        return PlayerType.WHITE;
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
        return true;
    }
}
