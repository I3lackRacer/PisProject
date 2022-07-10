package game;

import processing.core.PApplet;

import java.util.ArrayList;

public interface Player {

    void move(Move move);
    int getX();
    int getY();
    void draw(PApplet canvas);
    void setSelected(boolean isSelected);
    void avaiableMoves(ArrayList<Move> moves);
    PlayerType getPlayerSide();
    PlayerType getPlayerType();
    ArrayList<Move> getMoves();
    boolean isWhite();
}
