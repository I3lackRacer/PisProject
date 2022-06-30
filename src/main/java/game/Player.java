package game;

import java.util.ArrayList;

import processing.core.PApplet;

public interface Player {

    void move(Move move);
    int getX();
    int getY();
    void draw(PApplet canvas);
    void setSelected(boolean isSelected);
    void avaiableMoves(ArrayList<Move> moves);
    PlayerType getPlayerType();
    ArrayList<Move> getMoves();
    boolean isWhite();
}
