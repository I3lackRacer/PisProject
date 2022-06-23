package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class App extends PApplet {

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{""}, new App());
    }

    ArrayList<BlackPlayer> blackPlayers = new ArrayList<>();
    ArrayList<WhitePlayer> whitePlayers = new ArrayList<>();
    private Screen currentScreen = Screen.MENU;
    private Button startGame2PlayerButton = new Button("2 Player", 250 , 400, 140, 50, true);
    private Button startGameEasyButton = new Button("Bot Easy", 250 , 500, 140, 50, true);
    private Button startGameHardButton = new Button("Bot Hard", 250 , 600, 140, 50, true);
    private GameMode gameMode = GameMode.TWOPLAYER;


    @Override
    public void setup() {
    }

    @Override
    public void settings() {
        size(500, 700);
    }

    @Override
    public void draw() {
        background(0);
        switch(currentScreen) {
            case MENU -> {
                fill(100);
                textSize(50);
                textAlign(CENTER);
                text("Start Game", width/ 2, 200);
                startGame2PlayerButton.draw(this);
                startGameEasyButton.draw(this);
                startGameHardButton.draw(this);
            }
            case GAME -> {
                background(255);
                drawGameField();
                for (WhitePlayer whitePlayer : whitePlayers) {
                    whitePlayer.draw(this);
                }
                for (BlackPlayer blackPlayer : blackPlayers) {
                    blackPlayer.draw(this);
                }
            }
            case GAMEOVER -> {

            }
        }
    }

    private void setupNewGame() {
        blackPlayers.clear();
        whitePlayers.clear();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                whitePlayers.add(new WhitePlayer(x, y));
                blackPlayers.add(new BlackPlayer(x, 7-y));
            }
        }
    }

    private void drawGameField() {
        boolean white = true;
        stroke(color(223,215,200));
        for (int x = 0; x < 500; x+=50) {
            for (int y = 0; y < 500; y+=50) {
                if(white) fill(color(223,215,200));
                else fill(color(101,56,24));
                rect(x, y, 50, 50);
                white = !white;
            }
            white = !white;
        }
    }

    @Override
    public void keyPressed() {
    }

    @Override
    public void mouseClicked() {
        switch(currentScreen) {
            case MENU -> {
                if (startGame2PlayerButton.collision(mouseX, mouseY)) {
                    gameMode = GameMode.TWOPLAYER;
                    currentScreen = Screen.GAME;
                    setupNewGame();
                }
                if (startGameEasyButton.collision(mouseX, mouseY)) {
                    gameMode = GameMode.EASY;
                    currentScreen = Screen.GAME;
                    setupNewGame();
                }
                if (startGameHardButton.collision(mouseX, mouseY)) {
                    gameMode = GameMode.HARD;
                    currentScreen = Screen.GAME;
                    setupNewGame();
                }
            }
            case GAME -> {

            }
            case GAMEOVER -> {

            }
        }
    }
}
