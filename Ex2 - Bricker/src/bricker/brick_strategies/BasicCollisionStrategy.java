package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;

/**
 * A basic implementation of the CollisionStrategy interface.
 * Deletes the first game object involved in the collision from the specified game object collection.
 */
public class BasicCollisionStrategy implements CollisionStrategy {

    /**
     * The collection of game objects where collision effects are applied.
     */
    private final GameObjectCollection gameObjects;

    /**
     * Constructs a BasicCollisionStrategy with the specified game object collection.
     *
     * @param gameObjects The collection of game objects where collision effects are applied.
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * Deletes the first game object involved in the collision from the specified game object collection.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        // delete brick on collision
        gameObjects.removeGameObject(obj1, Layer.STATIC_OBJECTS);
    }
}