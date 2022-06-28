package game;

import java.util.ArrayList;

public interface Playable {
    void setField(int x, int y, PlayerType type);
    Location getKnockout(Move move);
    ArrayList<Move> getAvaiableMoves(int x, int y);
    Playable play(Move move);
}
