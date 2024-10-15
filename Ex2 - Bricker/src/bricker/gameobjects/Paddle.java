package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * A game object representing a paddle.
 */
public class Paddle extends GameObject {

    /**
     * The dimensions of the paddle.
     */
    private final Vector2 dimensions;

    /**
     * The input listener for controlling the paddle.
     */
    private UserInputListener inputListener;

    /**
     * The dimensions of the game window.
     */
    private final Vector2 windowDimensions;

    /**
     * The movement speed of the paddle.
     */
    private static final float MOVEMENT_SPEED = 400;

    /**
     * Constructs a new Paddle instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object.
     * @param inputListener    The input listener.
     * @param windowDimensions The dimensions of the game window.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.dimensions = dimensions;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        setTag("Paddle");
    }

    /**
     * Updates the paddle's position based on user input.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)
                && getTopLeftCorner().x() > 0) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)
                && getTopLeftCorner().x() < windowDimensions.x() - dimensions.x()) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
