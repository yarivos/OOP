/**
 * Factory class responsible for creating instances of different renderer types in a Tic-Tac-Toe game.
 */
public class RendererFactory {
    RendererFactory() {
    }

    /**
     * Builds and returns an instance of a renderer based on the provided renderer type and board size.
     *
     * @param type The type of renderer to be created (e.g., "console", "none").
     * @param size The size of the game board, used for initializing certain types of renderers.
     * @return An instance of the corresponding renderer type, or null if the type is not recognized.
     */
    public Renderer buildRenderer(String type, int size) {
        type = type.toLowerCase();
        switch (type) {
            case "console":
                return new ConsoleRenderer(size);
            case "none":
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
