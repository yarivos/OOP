/**
 * A strategy for handling collisions that trigger the appearance of falling hearts.
 * Extends AbstractStrategy to provide collision handling functionality.
 */
package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.gameobjects.FallingHeart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A strategy for handling collisions involving falling hearts.
 * This strategy creates a FallingHeart object upon collision and adds it to the game objects collection.
 */
public class FallingHeartStrategy extends AbstractStrategy {

    /**
     * The initial speed of the falling hearts.
     */
    private static final int INITIAL_SPEED = 100;

    /**
     * The speed vector for the falling hearts.
     */
    private static final Vector2 SPEED_VECTOR = new Vector2(0, INITIAL_SPEED);

    /**
     * The collection of game objects where collision effects are applied.
     */
    private final GameObjectCollection gameObjects;


    /**
     * The reader for loading images.
     */
    private final ImageReader imageReader;

    /**
     * Counter to track the number of hearts.
     */
    private Counter heartsCounter;

    /**
     * The dimensions of the game window.
     */

    /**
     * Constructs a FallingHeartStrategy with the specified parameters.
     *
     * @param gameObjects    The collection of game objects where collision effects are applied.
     * @param imageReader    The reader for loading images.
     * @param heartsCounter  Counter to track the number of hearts.
     * @param insideStrategy The strategy to be executed when a collision occurs.
     */
    FallingHeartStrategy(GameObjectCollection gameObjects, ImageReader imageReader,
                         Counter heartsCounter, CollisionStrategy insideStrategy) {
        super(insideStrategy);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.heartsCounter = heartsCounter;
    }

    /**
     * Handles the collision between two game objects.
     * Creates a FallingHeart object and adds it to the game objects collection.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        super.onCollision(obj1, obj2);
        Renderable heartImage = imageReader.readImage(BrickerGameManager.HEART_IMG_PATH,
                true);
        FallingHeart fallingHeart = new FallingHeart(
                obj1.getCenter(),
                new Vector2(BrickerGameManager.HEART_SIZE, BrickerGameManager.HEART_SIZE),
                heartImage, gameObjects, heartsCounter);
        gameObjects.addGameObject(fallingHeart);
        fallingHeart.setVelocity(SPEED_VECTOR);
    }
}
