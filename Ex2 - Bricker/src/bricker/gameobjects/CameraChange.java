package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;

/**
 * A game object representing a camera change effect.
 */
public class CameraChange extends GameObject {
    /**
     * The starting collision counter value of the ball.
     */
    private int startingBallCounter;

    /**
     * The Bricker game manager.
     */
    private BrickerGameManager brickerGameManager;

    /**
     * The ball object.
     */
    private Ball ball;

    /**
     * The dimensions of the game window.
     */
    private final Vector2 windowDimensions;

    /**
     * The collection of game objects.
     */
    private final GameObjectCollection gameObjects;
    /**
     * The number of hits left before setting the camera back to null.
     */
    private final int setCameraBackHits;

    /**
     * Constructs a new CameraChange instance.
     *
     * @param topLeftCorner      Position of the object, in window coordinates (pixels).
     * @param dimensions         Width and height in window coordinates.
     * @param ball               The ball object.
     * @param brickerGameManager The Bricker game manager.
     * @param windowDimensions   The dimensions of the game window.
     * @param gameObjects        The collection of game objects.
     * @param setCameraBackHits  The amount of hits until resetting camera
     */
    public CameraChange(Vector2 topLeftCorner, Vector2 dimensions,
                        Ball ball, BrickerGameManager brickerGameManager,
                        Vector2 windowDimensions, GameObjectCollection gameObjects,
                        int setCameraBackHits) {
        super(topLeftCorner, dimensions, null);
        this.ball = ball;
        this.startingBallCounter = ball.getCollisionCounter();
        this.brickerGameManager = brickerGameManager;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        this.setCameraBackHits = setCameraBackHits;
        setTag("Camera Change");
        setCamera();
    }

    /**
     * Updates the camera change effect.
     * Checks if the number of hits left before setting the camera back to null is reached.
     * Removes the camera change object if necessary.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (ball.getCollisionCounter() >= startingBallCounter + setCameraBackHits) {
            brickerGameManager.setCamera(null);
            gameObjects.removeGameObject(this);
        }
    }

    /**
     * Sets up the camera to follow the ball.
     */
    private void setCamera() {
        brickerGameManager.setCamera(
                new Camera(
                        ball, //object to follow
                        Vector2.ZERO, //follow the center of the object
                        windowDimensions.mult(.8f), //widen the frame a bit
                        windowDimensions //share the window dimensions
                )
        );
    }
}
