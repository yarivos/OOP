package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Interface for defining collision strategies between game objects.
 */
public interface CollisionStrategy {

    /**
     * Defines the action to be taken when a collision occurs between two game objects.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    void onCollision(GameObject obj1, GameObject obj2);
}
