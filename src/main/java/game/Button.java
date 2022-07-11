package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import processing.core.PApplet;

public class Button {
    private String text;
    private int x,y,width, height, clickTimer = 0;
    private static Logger logger = LogManager.getLogger(Button.class);

    public Button(String text, int x, int y, int width, int height, boolean centered) {
        this.text = text;
        if (centered) {
            this.x = x - width / 2;
            this.y = y - height / 2;
        } else {
            this.x = x;
            this.y = y;            
        }
        this.width = width;
        this.height = height;
    }

    public boolean collision(int coordX, int coordY) {
        boolean clicked = y < coordY && y + height > coordY && x < coordX && x + width > coordX;
        if (clicked) {
            logger.info("Button '" + text + "' has been pressed");
            clickTimer = 120;
        }
        return clicked;
    }

    public void draw(PApplet canvas) {
        canvas.textSize((float)(height * 0.5));
        canvas.stroke(255);
        canvas.fill(canvas.color(73,48,44));
        canvas.rect(x, y, width, height);
        canvas.fill(255);
        if (clickTimer != 0) {
            canvas.fill(100);
            clickTimer--;
        }
        canvas.textAlign(PApplet.CENTER, PApplet.CENTER);
        canvas.fill(canvas.color(223, 215, 200));
        canvas.text(text, x + (width / 2), y + (height / 2));
        canvas.fill(255);
    }

    public void setText(String text) {
        this.text = text;
    }
}
