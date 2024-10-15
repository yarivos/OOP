package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A game object representing an extra paddle.
 */
public class ExtraPaddle extends Paddle {

    /**
     * The collection of game objects.
     */
    private final GameObjectCollection gameObject;

    /**
     * Counter for tracking the number of hits remaining for the extra paddle.
     */
    private final Counter extraPaddleHitsRemain;

    /**
     * Maximum number of strikes allowed for the extra paddle.
     */
    private static final int MAXIMUM_NUMBER_OF_STRIKES = 4;

    /**
     * Constructs a new ExtraPaddle instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    The input listener.
     * @param windowDimensions The dimensions of the game window.
     * @param gameObject       The collection of game objects.
     * @param extraPaddleHits  Counter for tracking the number of hits remaining for the extra paddle.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                       Renderable renderable, UserInputListener inputListener,
                       Vector2 windowDimensions, GameObjectCollection gameObject,
                       Counter extraPaddleHits) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.gameObject = gameObject;
        this.extraPaddleHitsRemain = extraPaddleHits;
        setTag("Extra Paddle");
    }

    /**
     * Handles the exit of collision with another game object.
     * Decrements the number of hits remaining for the extra paddle and removes it if necessary.
     *
     * @param other The game object exited collision with.
     */
    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);
        if (other.getTag().equals("Puck") || other.getTag().equals("Ball")) {
            extraPaddleHitsRemain.decrement();
        }
        if (extraPaddleHitsRemain.value() == 0) {
            gameObject.removeGameObject(this);
        }
    }
}
