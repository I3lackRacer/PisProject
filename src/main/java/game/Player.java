package game;

import processing.core.PApplet;

public interface Player {

    void move(Move move);
    List<Move> getValidMoves();
    int getX();
    int getY();
    void draw(PApplet canvas);
    void setSelected(boolean isSelected);
}
