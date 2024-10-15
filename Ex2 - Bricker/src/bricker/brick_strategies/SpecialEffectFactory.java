package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A factory class for creating special effect collision strategies.
 * Allows for the creation of various special effects such as PuckEffect, ExtraPaddle, CameraChange,
 * HeartPickup, and DoubleEffect.
 */
public class SpecialEffectFactory {

    /**
     * Constants representing different types of special effects.
     */
    private static final int PUCK_EFFECT = 0;
    private static final int EXTRA_PADDLE = 1;
    private static final int CAMERA_CHANGE = 2;
    private static final int HEART_PICKUP = 3;
    private static final int DOUBLE_EFFECT = 4;

    /**
     * Total number of available special effects.
     */
    private static final int NUMBER_OF_EFFECTS_TOTAL = 5;

    /**
     * Maximum number of double effects allowed.
     */
    private static final int MAXIMUM_DOUBLE_EFFECTS = 2;

    /**
     * Counter for tracking double effects.
     */
    private int doubleEffects;

    /**
     * The reader for loading images.
     */
    private ImageReader imageReader;

    /**
     * The listener for user input.
     */
    private final UserInputListener inputListener;

    /**
     * The manager for the Bricker game.
     */
    private final BrickerGameManager brickerGameManager;

    /**
     * Counter for tracking hearts.
     */
    private final Counter heartsCounter;

    /**
     * The collection of game objects where collision effects are applied.
     */
    private final GameObjectCollection gameObjects;

    /**
     * The reader for loading sounds.
     */
    private SoundReader soundReader;

    /**
     * The dimensions of the game window.
     */
    private final Vector2 windowDimensions;

    /**
     * Constructs a SpecialEffectFactory with the specified parameters.
     *
     * @param gameObjects        The collection of game objects where collision effects are applied.
     * @param soundReader        The reader for loading sounds.
     * @param imageReader        The reader for loading images.
     * @param windowController   The controller for managing the game window.
     * @param inputListener      The listener for user input.
     * @param brickerGameManager The manager for the Bricker game.
     * @param heartsCounter      Counter for tracking hearts.
     */
    public SpecialEffectFactory(GameObjectCollection gameObjects, SoundReader soundReader,
                                ImageReader imageReader, WindowController windowController,
                                UserInputListener inputListener, BrickerGameManager brickerGameManager,
                                Counter heartsCounter) {
        this.gameObjects = gameObjects;
        this.soundReader = soundReader;
        this.imageReader = imageReader;
        this.windowDimensions = windowController.getWindowDimensions();
        this.inputListener = inputListener;
        this.brickerGameManager = brickerGameManager;
        this.heartsCounter = heartsCounter;
        this.doubleEffects = 0;
    }

    /**
     * Builds a special effect collision strategy.
     *
     * @param insideStrategy The strategy to be executed when a collision occurs.
     * @return The built special effect collision strategy.
     */
    public CollisionStrategy buildSpecialEffects(CollisionStrategy insideStrategy) {
        doubleEffects = 0;
        return pickStrategies(NUMBER_OF_EFFECTS_TOTAL, insideStrategy);
    }

    /**
     * Picks a collision strategy based on the specified number of effects and an inside collision strategy.
     *
     * @param numberOfEffects The number of effects to choose from.
     * @param insideCollision The inside collision strategy.
     * @return The chosen collision strategy.
     */
    public CollisionStrategy pickStrategies(int numberOfEffects,
                                            CollisionStrategy insideCollision) {
        Random rand = new Random();
        switch (rand.nextInt(numberOfEffects)) {
        case PUCK_EFFECT:
            return new PuckStrategy(gameObjects, imageReader,
                    soundReader, insideCollision);

        case EXTRA_PADDLE:
            return new ExtraPaddleStrategy(gameObjects, imageReader,
                    inputListener, windowDimensions, insideCollision);

        case CAMERA_CHANGE:
            return new ChangeCameraStrategy(gameObjects, brickerGameManager,
                    windowDimensions, insideCollision);

        case HEART_PICKUP:
            return new FallingHeartStrategy(gameObjects, imageReader,
                    heartsCounter, insideCollision);

        case DOUBLE_EFFECT:
            if (doubleEffects < MAXIMUM_DOUBLE_EFFECTS) {
                doubleEffects++;
                return pickStrategies(NUMBER_OF_EFFECTS_TOTAL,
                        pickStrategies(NUMBER_OF_EFFECTS_TOTAL - 1,
                                insideCollision));
            } else {
                return pickStrategies(numberOfEffects - 1, insideCollision);
            }
        }
        return null;
    }
}
