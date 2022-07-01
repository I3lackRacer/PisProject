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
        canvas.image(App.heart, 25 + x * 70, 25 + y * 70, 21, 21); 
    }
}
