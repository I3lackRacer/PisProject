package game;

import processing.core.PApplet;

public class BlackPlayer implements Player {

    private int x, y;
    private boolean isSelected = false;

    public BlackPlayer(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(Move move) {
        this.x = move.getX();
        this.y = move.getY();
    }

    @Override
    public void draw(PApplet canvas) {
        
    }

    @Override
    public List<Move> getValidMoves() {
        // TODO Auto-generated method stub
        return null;
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
}
