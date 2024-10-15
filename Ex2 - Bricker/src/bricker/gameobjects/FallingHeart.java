package bricker.gameobjects;

import bricker.main.BrickerGameManager;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A game object representing a falling heart.
 */
public class FallingHeart extends GameObject {

    /**
     * The collection of game objects.
     */
    private final GameObjectCollection gameObjects;

    /**
     * Counter for tracking the number of hearts.
     */
    private final Counter heartsCounter;

    /**
     * Constructs a new FallingHeart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object.
     * @param gameObjects   The collection of game objects.
     * @param heartsCounter Counter for tracking the number of hearts.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        GameObjectCollection gameObjects, Counter heartsCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.heartsCounter = heartsCounter;
        setTag("Falling Heart");
    }

    /**
     * Updates the falling heart.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * Specifies whether the falling heart should collide with another game object.
     *
     * @param other The game object to check collision with.
     * @return True if the falling heart should collide, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("Paddle");
    }

    /**
     * Handles the collision of the falling heart with another game object.
     * Increments the number of hearts and adds a graphic heart counter.
     * Removes the falling heart object.
     *
     * @param other     The game object collided with.
     * @param collision The collision data.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (heartsCounter.value() < BrickerGameManager.MAX_HEARTS) {
            heartsCounter.increment();
        }
        gameObjects.removeGameObject(this);
    }
}
