package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A game object representing a brick.
 */
public class Brick extends GameObject {

    /**
     * The collision strategy for the brick.
     */
    private final CollisionStrategy collisionStrategy;

    /**
     * Counter for tracking the number of bricks.
     */
    private final Counter brickCounter;

    /**
     * Constructs a new Brick instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param collisionStrategy The collision strategy for the brick.
     * @param brickCounter      Counter for tracking the number of bricks.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter brickCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.brickCounter = brickCounter;
        setTag("Brick");
    }

    /**
     * Handles the collision with another game object.
     * Decrements the brick counter and triggers the collision strategy.
     *
     * @param other     The game object collided with.
     * @param collision The collision data.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        brickCounter.decrement();
        collisionStrategy.onCollision(this, other);
    }

}
