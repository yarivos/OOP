package bricker.gameobjects;

import bricker.main.BrickerGameManager;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A game object representing a numeric heart counter.
 */
public class NumericHeartCounter extends GameObject {

    private final Counter heartsCounter;
    private final Vector2 windowDimensions;
    private final GameObjectCollection gameObjects;
    private GameObject numericHeartDisplay;
    private int currentNumberHearts;

    /**
     * Constructs a new NumericHeartCounter instance.
     *
     * @param heartsCounter    The counter representing the number of hearts
     * @param windowDimensions The dimensions of the game window
     * @param gameObjects      The collection of game objects to add numeric heart display to
     */
    public NumericHeartCounter(Counter heartsCounter, Vector2 windowDimensions,
                               GameObjectCollection gameObjects) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.heartsCounter = heartsCounter;
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        currentNumberHearts = heartsCounter.value();
        setHeartsNumericDisplay();
        setTag("Numeric Hearts Counter");
    }

    /**
     * Chooses the color for the numeric hearts display based on the remaining hearts count.
     *
     * @param heartsNumericRender The text renderable for the numeric hearts display
     */
    private void chooseNumericHeartsColor(TextRenderable heartsNumericRender) {
        switch (heartsCounter.value()) {
        case 1:
            heartsNumericRender.setColor(Color.red);
            break;
        case 2:
            heartsNumericRender.setColor(Color.yellow);
            break;
        default:
            heartsNumericRender.setColor(Color.green);
        }
    }

    /**
     * Updates the numeric heart display based on the counter value.
     *
     * @param deltaTime The time elapsed since the last frame
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (heartsCounter.value() != currentNumberHearts) {
            gameObjects.removeGameObject(numericHeartDisplay, Layer.BACKGROUND);
            currentNumberHearts = heartsCounter.value();
            setHeartsNumericDisplay();
        }
    }

    /**
     * Sets the display for the numeric representation of remaining hearts.
     */
    private void setHeartsNumericDisplay() {
        TextRenderable heartsNumericRender = new TextRenderable(
                String.valueOf(heartsCounter.value()));
        chooseNumericHeartsColor(heartsNumericRender);
        numericHeartDisplay = new GameObject(
                new Vector2(BrickerGameManager.HEART_NUMERIC_POSITION_X,
                        windowDimensions.y() - BrickerGameManager.HEARTS_Y_POSITION),
                new Vector2(BrickerGameManager.HEART_NUMERIC_FONT_SIZE,
                        BrickerGameManager.HEART_NUMERIC_FONT_SIZE),
                heartsNumericRender);
        gameObjects.addGameObject(numericHeartDisplay, Layer.BACKGROUND);
    }
}
