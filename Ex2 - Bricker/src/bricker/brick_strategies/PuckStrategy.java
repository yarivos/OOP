/**
 * A strategy for handling collisions that trigger the creation of multiple Puck objects.
 * Extends AbstractStrategy to provide collision handling functionality.
 */
package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A strategy for handling collisions involving Puck objects.
 * This strategy creates multiple Puck objects upon collision and adds them to the game objects collection.
 */
public class PuckStrategy extends AbstractStrategy {

    /**
     * The ratio of size for the Puck objects compared to the original ball.
     */
    private static final float SIZE_OF_BALL_RATIO = 0.75f;

    /**
     * The number of Puck balls to be created.
     */
    private static final int NUMBER_OF_PUCK_BALLS = 2;

    /**
     * The path to the image file for the Puck object.
     */
    public static final String PUCK_IMG_PATH = "assets/mockBall.png";

    /**
     * The path to the sound file for collision with the Puck object.
     */
    public static final String PUCK_SOUND_PATH = "assets/blop.wav";

    /**
     * The collection of game objects where collision effects are applied.
     */
    private final GameObjectCollection gameObjects;

    /**
     * The reader for loading images.
     */
    private final ImageReader imageReader;

    /**
     * The reader for loading sounds.
     */
    private final SoundReader soundReader;

    /**
     * Constructs a PuckStrategy with the specified parameters.
     *
     * @param gameObjects      The collection of game objects where collision effects are applied.
     * @param imageReader      The reader for loading images.
     * @param soundReader      The reader for loading sounds.
     * @param insideStrategy   The strategy to be executed when a collision occurs.
     */
    PuckStrategy(GameObjectCollection gameObjects,
                 ImageReader imageReader,
                 SoundReader soundReader,
                 CollisionStrategy insideStrategy) {
        super(insideStrategy);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * Handles the collision between two game objects.
     * Creates multiple Puck objects and adds them to the game objects collection.
     *
     * @param obj1 The first game object involved in the collision.
     * @param obj2 The second game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        super.onCollision(obj1, obj2);
        int ballRadius = BrickerGameManager.BALL_RADIUS;
        int ballInitialSpeed = BrickerGameManager.BALL_INITIAL_SPEED;
        Renderable puckImage =
                imageReader.readImage(PUCK_IMG_PATH, true);
        Sound collisionSound = soundReader.readSound(PUCK_SOUND_PATH);
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_PUCK_BALLS; i++) {
            Puck puck = new Puck(
                    obj1.getCenter(),
                    new Vector2(ballRadius, ballRadius).mult(SIZE_OF_BALL_RATIO),
                    puckImage, collisionSound);
            double angle = random.nextDouble() * Math.PI;
            float velocityX = (float) Math.cos(angle) * ballInitialSpeed;
            float velocityY = (float) Math.sin(angle) * ballInitialSpeed;
            puck.setVelocity(new Vector2(velocityX, velocityY));
            gameObjects.addGameObject(puck);
        }
    }
}
