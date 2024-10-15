package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.gameobjects.Ball;
import bricker.gameobjects.CameraChange;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

/**
 * A strategy for changing the camera position in response to collisions.
 * Extends AbstractStrategy to provide collision handling functionality.
 */
public class ChangeCameraStrategy extends AbstractStrategy {
    /**
     * The number of hits left before setting the camera back to null.
     */
    private final int SET_CAMERA_BACK_HITS_LEFT = 5;

    /**
     * The collection of game objects where collision effects are applied.
     */
    private final GameObjectCollection gameObjects;

    /**
     * The game manager responsible for managing the Bricker game.
     */
    private final BrickerGameManager brickerGameManager;

    /**
     * The dimensions of the game window.
     */
    private final Vector2 windowDimensions;

    /**
     * Constructs a ChangeCameraStrategy with the specified parameters.
     *
     * @param gameObjects        The collection of game objects where collision effects are applied.
     * @param brickerGameManager The game manager responsible for managing the Bricker game.
     * @param windowDimensions   The dimensions of the game window.
     * @param insideStrategy     The strategy to be executed when a collision occurs.
     */
    ChangeCameraStrategy(GameObjectCollection gameObjects,
                         BrickerGameManager brickerGameManager,
                         Vector2 windowDimensions,
                         CollisionStrategy insideStrategy) {
        super(insideStrategy);
        this.gameObjects = gameObjects;
        this.brickerGameManager = brickerGameManager;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Handles the collision between two game objects.
     * Adds a CameraChange object to the game objects collection if the collision involves a Ball
     * and there is no existing camera in the game manager.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        super.onCollision(obj1, obj2);
        if (obj2.getTag().equals("Ball") && brickerGameManager.camera() == null) {
            gameObjects.addGameObject(new CameraChange(Vector2.ZERO, Vector2.ZERO,
                    (Ball) obj2, brickerGameManager,
                    windowDimensions, gameObjects, SET_CAMERA_BACK_HITS_LEFT));
        }
    }
}
