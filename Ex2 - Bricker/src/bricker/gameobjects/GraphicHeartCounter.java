package bricker.gameobjects;

import bricker.main.BrickerGameManager;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A game object representing a graphic heart counter.
 */
public class GraphicHeartCounter extends GameObject {

    private final GameObject[] heartsArray;
    private final Counter heartsCounter;
    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;
    private int currentNumberHearts;
    private Renderable heartImage;

    /**
     * Constructs a GraphicHeartCounter instance.
     *
     * @param heartsCounter    The counter representing the number of hearts
     * @param windowDimensions The dimensions of the game window
     * @param imageReader      The image reader for loading heart images
     * @param gameObjects      The collection of game objects to add hearts to
     */
    public GraphicHeartCounter(Counter heartsCounter, Vector2 windowDimensions,
                               ImageReader imageReader, GameObjectCollection gameObjects) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.heartsArray = new GameObject[BrickerGameManager.MAX_HEARTS];
        this.heartsCounter = heartsCounter;
        this.currentNumberHearts = heartsCounter.value();
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        setTag("Graphic Hearts Counter");
        this.heartImage = imageReader.readImage(
                BrickerGameManager.HEART_IMG_PATH, true);
        createInitialHearts();
    }

    /**
     * Updates the amount of hearts displayed based on the counter.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (heartsCounter.value() < currentNumberHearts) {
            reduceHeart();
        }
        if (heartsCounter.value() > currentNumberHearts) {
            addHeart();
        }
    }

    /**
     * Adds a heart to the display.
     */
    private void addHeart() {
        currentNumberHearts++;
        GameObject heart = new GameObject(
                new Vector2(BrickerGameManager.HEARTS_X_POSITION *
                        currentNumberHearts,
                        windowDimensions.y() - BrickerGameManager.HEARTS_Y_POSITION),
                new Vector2(BrickerGameManager.HEART_SIZE, BrickerGameManager.HEART_SIZE),
                heartImage);
        gameObjects.addGameObject(heart, Layer.BACKGROUND);
        heartsArray[currentNumberHearts - 1] = heart;
    }

    /**
     * Removes a heart from the display.
     */
    private void reduceHeart() {
        currentNumberHearts--;
        gameObjects.removeGameObject(heartsArray[currentNumberHearts], Layer.BACKGROUND);
        heartsArray[currentNumberHearts] = null;
    }

    /**
     * Creates the initial heart objects based on the starting hearts count.
     */
    private void createInitialHearts() {
        for (int i = 1; i <= currentNumberHearts; i++) {
            GameObject heart = new GameObject(
                    new Vector2(BrickerGameManager.HEARTS_X_POSITION *
                            i,
                            windowDimensions.y() - BrickerGameManager.HEARTS_Y_POSITION),
                    new Vector2(BrickerGameManager.HEART_SIZE, BrickerGameManager.HEART_SIZE),
                    heartImage);
            gameObjects.addGameObject(heart, Layer.BACKGROUND);
            heartsArray[i - 1] = heart;
        }
    }
}
