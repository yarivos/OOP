package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.gameobjects.ExtraPaddle;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A strategy for handling collisions that trigger the appearance of an extra paddle.
 * Extends AbstractStrategy to provide collision handling functionality.
 */
public class ExtraPaddleStrategy extends AbstractStrategy {

    /**
     * The collection of game objects where collision effects are applied.
     */
    private final GameObjectCollection gameObjects;

    /**
     * The reader for loading images.
     */
    private final ImageReader imageReader;

    /**
     * The listener for user input.
     */
    private final UserInputListener inputListener;

    /**
     * The dimensions of the game window.
     */
    private final Vector2 windowDimensions;

    /**
     * The maximum number of strikes allowed for triggering the appearance of the extra paddle.
     */
    private static final int MAXIMUM_NUMBER_OF_STRIKES = 4;

    /**
     * Counter to track the remaining hits before the extra paddle appears.
     */
    private final static Counter extraPaddleHitsRemain = new Counter(0);

    /**
     * Constructs an ExtraPaddleStrategy with the specified parameters.
     *
     * @param gameObjects       The collection of game objects where collision effects are applied.
     * @param imageReader       The reader for loading images.
     * @param userInputListener The listener for user input.
     * @param windowDimensions  The dimensions of the game window.
     * @param insideStrategy    The strategy to be executed when a collision occurs.
     */
    ExtraPaddleStrategy(GameObjectCollection gameObjects,
                        ImageReader imageReader,
                        UserInputListener userInputListener,
                        Vector2 windowDimensions, CollisionStrategy insideStrategy) {
        super(insideStrategy);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.inputListener = userInputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Handles the collision between two game objects.
     * Triggers the appearance of an extra paddle if the number of hits remaining reaches zero.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        super.onCollision(obj1, obj2);
        if (extraPaddleHitsRemain.value() == 0) {
            extraPaddleHitsRemain.increaseBy(MAXIMUM_NUMBER_OF_STRIKES);
            Renderable paddleImage = imageReader.readImage(BrickerGameManager.PADDLE_IMG_PATH,
                    true);
            ExtraPaddle extraPaddle = new ExtraPaddle(
                    Vector2.ZERO,
                    BrickerGameManager.PADDLE_DIMENSIONS,
                    paddleImage,
                    inputListener,
                    windowDimensions, gameObjects, extraPaddleHitsRemain);
            extraPaddle.setCenter(windowDimensions.mult(0.5f));
            gameObjects.addGameObject(extraPaddle);
        }
    }
}
