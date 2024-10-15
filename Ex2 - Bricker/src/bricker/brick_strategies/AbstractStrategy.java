package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An abstract implementation of the CollisionStrategy interface.
 * Provides functionality for handling collisions between game objects.
 * Allows handling double effect on collisions
 */

public abstract class AbstractStrategy implements CollisionStrategy {

    /**
     * The inside strategy to be executed when a collision occurs.
     */
    private final CollisionStrategy insideStrategy;

    /**
     * Constructs an AbstractStrategy with the specified inside strategy.
     *
     * @param insideStrategy The strategy to be executed when a collision occurs.
     */
    AbstractStrategy(CollisionStrategy insideStrategy) {
        this.insideStrategy = insideStrategy;
    }

    /**
     * Handles the collision between two game objects by delegating to the inside strategy.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        insideStrategy.onCollision(obj1, obj2);
    }
}
