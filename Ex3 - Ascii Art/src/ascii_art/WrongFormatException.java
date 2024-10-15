package ascii_art;

/**
 * Exception thrown when the format of a command is incorrect.
 */
public class WrongFormatException extends Exception {
    /**
     * Constructs a new WrongFormatException with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public WrongFormatException(String message) {
        super(message);
    }
}
