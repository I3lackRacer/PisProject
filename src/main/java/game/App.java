package game;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import processing.core.PApplet;
import processing.core.PImage;

public class App extends PApplet {


    public static void main(String[] args) {
        PApplet.runSketch(new String[] { "" }, new App());
    }

    public static PImage heart;
    private Board board;
    private Screen currentScreen = Screen.MENU;
    private Button startGame2PlayerButton = new Button("2 Player", 200, 100, 140, 50, true);
    private Button startBotButton = new Button("Bot Easy", 200, 200, 140, 50, true);
    private Button startGameHardButton = new Button("Bot Hard", 200, 300, 140, 50, true);
    private GameMode gameMode = GameMode.TWOPLAYER;
    private boolean isWhiteTurn;
    private Player selected = null;
    private static Logger logger = LogManager.getLogger(App.class);

    @Override
    public void setup() {
        board = new Board(new CheckersGame());
        loadResources();
        isWhiteTurn = true;
    }

    private void loadResources() {
        heart = loadImage("./src/main/resources/heart.png");
    }

    @Override
    public void settings() {
        size(560, 560);
    }

    @Override
    public void draw() {

        background(0);
        switch (currentScreen) {
            case MENU -> {
                fill(100);
                textSize(50);
                textAlign(CENTER);
                text("Start Game", width / 2, 200);
                startGame2PlayerButton.draw(this);
                startBotButton.draw(this);
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
        logger.info("Initialized new game");
    }

    private void drawGameField() {
        boolean white = true;
        stroke(color(223, 215, 200));
        for (int x = 0; x < 560; x += 70) {
            for (int y = 0; y < 560; y += 70) {
                if (white)
                    fill(color(223, 215, 200));
                else
                    fill(color(101, 56, 24));
                rect(x, y, 70, 70);
                white = !white;
            }
            white = !white;
        }
    }

    @Override
    public void mouseClicked() {
        switch (currentScreen) {
            case MENU -> {
                if (startGame2PlayerButton.collision(mouseX, mouseY)) {
                    gameMode = GameMode.TWOPLAYER;
                    currentScreen = Screen.GAME;
                    //setupNewGame();
                    board.addPlayer(new WhitePlayer(2, 5));
                    board.addPlayer(new BlackPlayer(4, 5));
                }
                if (startBotButton.collision(mouseX, mouseY)) {
                    gameMode = GameMode.BOTGAME;
                    currentScreen = Screen.GAME;
                    setupNewGame();
                }
            }
            case GAME -> {
                int x = mouseX / 70;
                int y = mouseY / 70;
                if (selected != null && selected.getMoves().stream().anyMatch((move) -> {
                    return move.xEnd() == x && move.yEnd() == y;
                })) {
                    Optional<Move> move = selected.getMoves().stream().filter((m) -> {
                        return m.xEnd() == x && m.yEnd() == y;
                    }).findAny();
                    if (move.isEmpty()) {
                        return;
                    }
                    Move playMove = move.get();
                    board.play(playMove);
                    logger.info("Moved player from (" + playMove.xStart() + "/" + playMove.yStart() + ") to ("
                            + playMove.xEnd() + "/" + playMove.yEnd() + ")");
                    if (playMove.knockout()) {
                        logger.info("Knocked out an enemy with the last move");
                    }
                    selected = null;
                    isWhiteTurn = !isWhiteTurn;
                } else {
                    Player p = board.getPlayer(x, y);
                    if (p == null) {
                        selected = null;
                        board.deselectPlayer();
                        return;
                    }
                    if ((p.getPlayerType() == PlayerType.WHITE && isWhiteTurn)
                            || (p.getPlayerType() == PlayerType.BLACK && !isWhiteTurn)) {
                        selected = board.select(x, y);
                    }
                }
            }
            case GAMEOVER -> {

            }
        }
    }
}
