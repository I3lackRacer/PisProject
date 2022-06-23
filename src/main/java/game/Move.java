package game;

public class Move {
    
    private int x, y;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Move of(int x, int y) {
        return new Move(x ,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
