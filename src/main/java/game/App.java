package game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.Optional;

public class App extends PApplet {


    private boolean loading = false;

    private PFont font;
    private PImage backgroundImage;

    public static void main(String[] args) {
        PApplet.runSketch(new String[] { "" }, new App());
    }

    public static PImage heart;
    private Board board;
    private Screen currentScreen = Screen.MENU;
    private final Button startGame2PlayerButton = new Button("2 Player", 280, 100, 140, 50, true);
    private final Button startBotButton = new Button("Bot Game", 280, 200, 140, 50, true);
    private GameMode gameMode = GameMode.TWOPLAYER;
    private final Button restartButton = new Button("Restart Game", 280, 300, 150, 50, true);
    private boolean isWhiteTurn;
    private PlayerType winner = null;
    private Player selected = null;
    private static final Logger logger = LogManager.getLogger(App.class);

    @Override
    public void setup() {
        board = new Board(new CheckersGame());
        loadResources();
        isWhiteTurn = true;
    }

    private void loadResources() {
        heart = loadImage("./src/main/resources/heart.png");
        backgroundImage = loadImage("./src/main/resources/background.png");
        backgroundImage.resize(560, 560);
        font = createFont("./src/main/resources/font.ttf", 128);
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
                background(backgroundImage);
                textFont(font);
                noStroke();
                fill(color(0, 0, 0, 180));
                rect(0, 300, 560, 220);
                textAlign(CENTER);
                textSize(50);
                fill(color(223, 215, 200));
                text("Lets play some", width / 2, 350);
                textSize(100);
                text("CHECKERS!", width / 2, 450);
                startGame2PlayerButton.draw(this);
                startBotButton.draw(this);
            }
            case GAME -> {
                if (loading) {
                    this.text("Loading", height/2, width/2);
                }
                background(255);
                drawGameField();
                board.draw(this);
            }
            case GAMEOVER -> {
                background(0);
                textAlign(CENTER);
                color(100);
                text("GAME OVER", width / 2, 100);
                text(winner.toString() + " WON!", width/ 2, 400);
                restartButton.draw(this);
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
                    setupNewGame();
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
                if (selected != null && selected.getMoves().stream().anyMatch((move) -> move.xEnd() == x && move.yEnd() == y)) {
                    Optional<Move> move = selected.getMoves().stream().filter((m) -> m.xEnd() == x && m.yEnd() == y).findAny();
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
                    if (board.isGameOver()) {
                        winner = board.whoWon();
                        currentScreen = Screen.GAMEOVER;
                    }
                    if (gameMode == GameMode.BOTGAME && currentScreen == Screen.GAME) {
                        loading = true;
                        draw();
                        board.botPlay();
                        loading = false;
                        if (winner != null) {
                            winner = board.whoWon();
                            currentScreen = Screen.GAMEOVER;
                        }
                        isWhiteTurn = !isWhiteTurn;
                    }
                } else {
                    Player p = board.getPlayer(x, y);
                    if (p == null) {
                        selected = null;
                        board.deselectPlayer();
                        return;
                    }
                    if ((p.getPlayerSide() == PlayerType.WHITE && isWhiteTurn)
                            || (p.getPlayerSide() == PlayerType.BLACK && !isWhiteTurn)) {
                        selected = board.select(x, y);
                    }
                }
            }
            case GAMEOVER -> {
                if (restartButton.collision(mouseX, mouseY)) {
                    restart();
                }
            }
        }
    }

    private void restart() {

    }
}
