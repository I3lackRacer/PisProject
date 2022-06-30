package game;

import processing.core.PApplet;

public class WhiteQueen extends WhitePlayer{
    
    
    public WhiteQueen(int x, int y) {
        super(x, y);
    }

    public WhiteQueen(Player p) {
        super(p.getX(), p.getY());
        this.moves = p.getMoves();
    }

    @Override
    public void draw(PApplet canvas) {
        super.draw(canvas);
        canvas.fill(canvas.color(200, 0, 0));
        canvas.circle(25 + x * 50, 25 + y * 50, 5);
    }
}
