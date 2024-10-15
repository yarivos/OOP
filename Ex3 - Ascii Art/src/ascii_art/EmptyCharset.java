package ascii_art;

/**
 * An exception indicating an empty character set.
 */
public class EmptyCharset extends Exception {
    private static final String CHARSET_IS_EMPTY = "Did not execute. Charset is empty.";
    EmptyCharset() {
        super(CHARSET_IS_EMPTY);
    }
}
