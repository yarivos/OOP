package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A game object representing a ball.
 */
public class Ball extends GameObject {

    /**
     * The sound played on collision.
     */
    private Sound collisionSound;

    /**
     * Counter for tracking the number of collisions.
     */
    private final Counter collisionCounter = new Counter(0);

    /**
     * Constructs a new Ball instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound The sound played on collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        setTag("Ball");
    }

    /**
     * Handles the collision with another game object.
     * Reflects the ball's velocity and plays the collision sound.
     *
     * @param other     The game object collided with.
     * @param collision The collision data.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCounter.increment();
    }

    /**
     * Gets the number of collisions the ball has experienced.
     *
     * @return The number of collisions.
     */
    public int getCollisionCounter() {
        return collisionCounter.value();
    }
}
