package ascii_art;

/**
 * An exception indicating that the resolution exceeds a boundary.
 */
public class ResolutionExceedsBoundary extends Exception {
    private static final String BOUNDARIES_EXCEED_ERROR_MSG = "Did not change resolution due to exceeding " +
            "boundaries.";
    ResolutionExceedsBoundary() {
        super(BOUNDARIES_EXCEED_ERROR_MSG);
    }
}
