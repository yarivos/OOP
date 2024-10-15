package bricker.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A game object representing a puck.
 */
public class Puck extends Ball {

    /**
     * Constructs a new Puck instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object.
     * @param collisionSound   The collision sound.

     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        setTag("Puck");
    }
}
