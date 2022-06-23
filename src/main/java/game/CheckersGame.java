package game;

public class CheckersGame {

    FieldStatus[][] field = new FieldStatus[8][8];

    public CheckersGame() {
        
    }
}



enum FieldStatus {
    WHITE,
    BLACK,
    WHITE_QUEEN,
    BLACK_QUEEN,
    FREE,
}