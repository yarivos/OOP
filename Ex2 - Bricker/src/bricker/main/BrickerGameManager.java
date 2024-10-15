package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.SpecialEffectFactory;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The BrickerGameManager class manages the Bricker Breaker game.
 * It handles game initialization, updates, object creation, and user input.
 */
public class BrickerGameManager extends GameManager {
    // Constants for game messages and asset paths
    /**
     * Message displayed when the player loses the game and is prompted to play again.
     */

    public static final String YOU_LOSE_PLAY_AGAIN = "You Lose! Play Again?";
    /**
     * Message displayed when the player wins the game and is prompted to play again.
     */

    public static final String YOU_WIN_PLAY_AGAIN = "You Win! Play Again?";
    /**
     * Path to the image file for the paddle.
     */

    public static final String PADDLE_IMG_PATH = "assets/paddle.png";
    /**
     * Path to the image file for the brick.
     */

    public static final String BRICK_IMG_PATH = "assets/brick.png";
    /**
     * Path to the image file for the heart.
     */

    public static final String HEART_IMG_PATH = "assets/heart.png";
    /**
     * Path to the image file for the ball.
     */

    public static final String BALL_IMG_PATH = "assets/ball.png";
    /**
     * Path to the sound file for the ball collision.
     */

    public static final String BALL_SOUND_PATH = "assets/blop.wav";
    /**
     * Path to the image file for the wall.
     */

    public static final String WALL_IMG_PATH = "assets/DARK_BG2_small.jpeg";

    // Constants for game parameters
    private static final int BRICKS_PARAMETERS_INSERTED = 2;

    /**
     * The radius of the ball.
     */

    public static final int BALL_RADIUS = 20;
    /**
     * The initial speed of the ball.
     */

    public static final int BALL_INITIAL_SPEED = 250;
    /**
     * The dimensions of the paddle.
     */

    public static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);
    /**
     * The vertical position of the hearts.
     */

    public static final int HEARTS_Y_POSITION = 25;
    /**
     * The horizontal position of the hearts.
     */

    public static final int HEARTS_X_POSITION = 30;
    /**
     * The size of the hearts.
     */

    public static final int HEART_SIZE = 20;
    /**
     * The font size for displaying numeric hearts.
     */

    public static final int HEART_NUMERIC_FONT_SIZE = 15;
    /**
     * The horizontal position of the numeric hearts.
     */

    public static final int HEART_NUMERIC_POSITION_X = 5;
    /**
     * The maximum number of hearts the player can have.
     */

    public static final int MAX_HEARTS = 4;
    private static final int STARTING_HEARTS = 3;
    private static final int PADDLE_Y_ELEVATION = 30;
    private static final Vector2 WINDOW_DIMENSIONS = new Vector2(700, 500);
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICKS_IN_ROW = 8;
    private static final int WALL_WIDTH = 10;
    private static final int BRICK_WIDTH_Y = 15;
    private static final int DISTANCE_BETWEEN_BRICKS = 3;
    private final int numberOfBrickRows;
    private final int numberOfBricksPerRow;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;
    private WindowController windowController;
    // counts how many hearts the player has.
    private Counter heartsCounter;
    // counts the amount of bricks in the game.
    private Counter brickCounter;
    // this ball will be the same until the game ends.
    private Ball ball;

    /**
     * Constructs a BrickerGameManager instance with default brick parameters.
     *
     * @param windowTitle      The title of the game window
     * @param windowDimensions The dimensions of the game window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.numberOfBrickRows = DEFAULT_BRICK_ROWS;
        this.numberOfBricksPerRow = DEFAULT_BRICKS_IN_ROW;
    }

    /**
     * Constructs a BrickerGameManager instance with custom brick parameters.
     *
     * @param windowTitle          The title of the game window
     * @param windowDimensions     The dimensions of the game window
     * @param numberOfBrickRows    The number of rows of bricks
     * @param numberOfBricksPerRow The number of bricks per row
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int numberOfBrickRows, int numberOfBricksPerRow) {
        super(windowTitle, windowDimensions);
        this.numberOfBrickRows = numberOfBrickRows;
        this.numberOfBricksPerRow = numberOfBricksPerRow;
    }

    /**
     * Initializes the game, including objects, input, and window settings.
     *
     * @param imageReader      The image reader for loading assets
     * @param soundReader      The sound reader for loading assets
     * @param inputListener    The user input listener
     * @param windowController The window controller for managing the game window
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        // Initialization
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        brickCounter = new Counter(0);
        heartsCounter = new Counter(STARTING_HEARTS);
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(80);

        // Creating game elements
        createBackground();
        createNumericHeartsDisplay();
        createGraphicHeartDisplay();
        createBall();
        createPaddle();
        createWalls();
        createBricks();
    }

    /**
     * Creates and adds a GraphicHeartCounter GameObject to the gameObjects list.
     * GraphicHeartCounter displays the number of hearts visually as a graphic representation.
     * This method initializes and adds the GraphicHeartCounter to the gameObjects list.
     */
    private void createGraphicHeartDisplay() {
        gameObjects().addGameObject(new GraphicHeartCounter(heartsCounter, windowDimensions,
                imageReader, gameObjects()));
    }

    /**
     * Creates and adds a NumericHeartCounter GameObject to the gameObjects list.
     * NumericHeartCounter displays the number of hearts numerically.
     * This method initializes and adds the NumericHeartCounter to the gameObjects list.
     */
    private void createNumericHeartsDisplay() {
        gameObjects().addGameObject(new NumericHeartCounter(heartsCounter, windowDimensions,
                gameObjects()));
    }


    /**
     * Creates the bricks for the game grid based on the specified parameters.
     */
    private void createBricks() {
        Random rand = new Random();
        SpecialEffectFactory specialEffectFactory = new SpecialEffectFactory(gameObjects(),
                soundReader,
                imageReader,
                windowController,
                inputListener,
                this,
                heartsCounter);
        Renderable brickImage = imageReader.readImage(BRICK_IMG_PATH, false);
        // calculations of brick positions on screen
        float brickWidthX =
                (windowDimensions.x() - 2 * WALL_WIDTH - DISTANCE_BETWEEN_BRICKS * numberOfBricksPerRow)
                        / numberOfBricksPerRow;
        float brickPositionX = (windowDimensions.x() - 2 * WALL_WIDTH) / numberOfBricksPerRow;
        CollisionStrategy collisionStrategy;
        for (int row = 0; row < numberOfBrickRows; row++) {
            for (int brickNumberInRow = 0; brickNumberInRow < numberOfBricksPerRow; brickNumberInRow++) {
                brickCounter.increment();
                // chance of 50% to get a basic strategy or special effect strategy.
                if (rand.nextBoolean()) {
                    collisionStrategy = new BasicCollisionStrategy(gameObjects());
                } else {
                    collisionStrategy =
                            specialEffectFactory.buildSpecialEffects(
                                    new BasicCollisionStrategy(gameObjects()));
                }
                Brick brick = new Brick(new Vector2(
                        WALL_WIDTH + brickPositionX * brickNumberInRow,
                        WALL_WIDTH + (BRICK_WIDTH_Y + DISTANCE_BETWEEN_BRICKS) * row),
                        new Vector2(brickWidthX, BRICK_WIDTH_Y),
                        brickImage,
                        collisionStrategy, brickCounter);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * Updates the game state, including checking for game over conditions and removing
     * out-of-bounds objects.
     *
     * @param deltaTime The time elapsed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (checkIfLost()) return;
        removeOutOfBorders();
        checkIfWon();
    }

    /**
     * Checks if the player has lost the game.
     * If the ball goes out of bounds and there are no remaining hearts, prompts the player
     * to play again or quit.
     * Decrements heart count and resets ball position if there are remaining hearts.
     *
     * @return True if the player has lost, false otherwise
     */
    private boolean checkIfLost() {
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            // loss
            if (heartsCounter.value() == 1) {
                // no spare hearts - game over
                if (windowController.openYesNoDialog(YOU_LOSE_PLAY_AGAIN)) {
                    windowController.resetGame();
                    return true;
                } else {
                    windowController.closeWindow();
                }
            } else {
                heartsCounter.decrement();
            }
            ballStartingPositionAndVelocity();
        }
        return false;
    }

    /**
     * Removes game objects that have moved out of the game window bounds.
     */
    private void removeOutOfBorders() {
        for (GameObject obj : gameObjects()) {
            if (obj.getCenter().y() > windowDimensions.y()) {
                gameObjects().removeGameObject(obj);
            }
        }
    }

    /**
     * Checks if the player has won the game.
     * If all bricks are destroyed or the player presses the win key, prompts the player
     * to play again or quit.
     */
    private void checkIfWon() {
        if (brickCounter.value() == 0 ||
                inputListener.isKeyPressed(KeyEvent.VK_W)) {
            if (windowController.openYesNoDialog(YOU_WIN_PLAY_AGAIN)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * Resets the ball position and velocity when the player loses a life.
     * Randomizes initial velocity direction.
     */
    private void ballStartingPositionAndVelocity() {
        float ballVelX = BALL_INITIAL_SPEED;
        float ballVelY = BALL_INITIAL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    /**
     * Creates the paddle object at the bottom of the screen.
     */
    private void createPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMG_PATH, true);
        Paddle paddle = new Paddle(
                Vector2.ZERO,
                PADDLE_DIMENSIONS,
                paddleImage,
                inputListener,
                windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - PADDLE_Y_ELEVATION));
        gameObjects().addGameObject(paddle);
    }

    /**
     * Creates the ball object at the center of the screen.
     * Sets initial position and velocity.
     */
    private void createBall() {
        Renderable ballImage =
                imageReader.readImage(BALL_IMG_PATH, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        ballStartingPositionAndVelocity();
        gameObjects().addGameObject(ball);
    }

    /**
     * Creates the background object for the game window.
     */
    private void createBackground() {
        Renderable backgroundImage =
                imageReader.readImage(WALL_IMG_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Creates the boundary walls to prevent objects from moving out of the game window.
     */
    private void createWalls() {

        // create top wall
        GameObject topWall = new GameObject(
                Vector2.ZERO, new Vector2(windowDimensions.x(),
                WALL_WIDTH), null);
        topWall.setCenter(new Vector2(windowDimensions.x() / 2, 0));
        gameObjects().addGameObject(topWall, Layer.STATIC_OBJECTS);

        // create left wall
        GameObject leftWall = new GameObject(
                Vector2.ZERO, new Vector2(WALL_WIDTH,
                windowDimensions.y()), null);
        leftWall.setCenter(new Vector2(0, windowDimensions.y() / 2));
        gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);

        // create right wall
        GameObject rightWall = new GameObject(
                Vector2.ZERO, new Vector2(WALL_WIDTH,
                windowDimensions.y()), null);
        rightWall.setCenter(new Vector2(windowDimensions.x(), windowDimensions.y() / 2));
        gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);
    }

    /**
     * The main method to start the game.
     * Parses command-line arguments to set custom brick parameters if provided.
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {

        if (args.length == BRICKS_PARAMETERS_INSERTED) {
            int numberOfBrickRows;
            int numberOfBricksPerRow;
            numberOfBrickRows = Integer.parseInt(args[0]);
            numberOfBricksPerRow = Integer.parseInt(args[1]);
            new BrickerGameManager(
                    "Bricker Breaker",

                    WINDOW_DIMENSIONS, numberOfBrickRows, numberOfBricksPerRow).run();
        } else { // default brick parameters
            new BrickerGameManager(
                    "Bricker Breaker",
                    WINDOW_DIMENSIONS).run();
        }
    }
}
