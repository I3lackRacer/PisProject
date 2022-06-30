package game;

import java.io.File;

import processing.core.PApplet;

public class BlackQueen extends BlackPlayer {

    public BlackQueen(int x, int y) {
        super(x, y);
    }
    
    public BlackQueen(Player p) {
        super(p.getX(), p.getY());
        moves = p.getMoves();
    }

    @Override
    public void draw(PApplet canvas) {
        super.draw(canvas);
        canvas.fill(canvas.color(200, 0, 0));
        canvas.circle(25 + x * 50, 25 + y * 50, 5);
    }
}
