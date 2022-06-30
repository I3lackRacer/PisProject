package game;

import java.util.Optional;

import processing.core.PApplet;

public class App extends PApplet {

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{""}, new App());
    }

    private Board board; 
    private Screen currentScreen = Screen.MENU;
    private Button startGame2PlayerButton = new Button("2 Player", 200 , 100, 140, 50, true);
    private Button startGameEasyButton = new Button("Bot Easy", 200 , 200, 140, 50, true);
    private Button startGameHardButton = new Button("Bot Hard", 200 , 300, 140, 50, true);
    private GameMode gameMode = GameMode.TWOPLAYER;
    private boolean isWhiteTurn;
    private Player selected = null;

    @Override
    public void setup() {
        board = new Board(new CheckersGame());
        isWhiteTurn = true;
    }

    @Override
    public void settings() {
        size(400, 400);
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
                board.draw(this);
            }
            case GAMEOVER -> {

            }
        }
    }

    private void setupNewGame() {
        board.newGame();
    }

    private void drawGameField() {
        boolean white = true;
        stroke(color(223,215,200));
        for (int x = 0; x < 400; x+=50) {
            for (int y = 0; y < 400; y+=50) {
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
                int x = mouseX / 50;
                int y = mouseY / 50;
                if (selected != null && selected.getMoves().stream().anyMatch((move) -> {return move.xEnd() == x && move.yEnd() == y;})) {
                    Optional<Move> move = selected.getMoves().stream().filter((m) -> {return m.xEnd() == x && m.yEnd() == y;}).findAny();
                    if (move.isEmpty()) {
                        return;
                    }
                    board.play(move.get());
                    selected = null;
                    isWhiteTurn = !isWhiteTurn;
                } else {
                    Player p = board.getPlayer(x, y);
                    if (p == null) {
                        selected = null;
                        board.deselect();
                        return;
                    }
                    if((p.getPlayerType() == PlayerType.WHITE && isWhiteTurn) || (p.getPlayerType() == PlayerType.BLACK && !isWhiteTurn)) {
                        selected = board.select(x,y);
                    }
                } 
            }
            case GAMEOVER -> {

            }
        }
    }
}
