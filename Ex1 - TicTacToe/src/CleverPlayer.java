import java.util.Random;

/**
 * Represents a clever player in a Tic-Tac-Toe game.
 * The clever player has a chance of making a strategic move and otherwise makes a random move.
 */
public class CleverPlayer implements Player {


    CleverPlayer() {
    }

    /**
     * Plays a turn for the clever player on the given game board.
     * The player has a chance to make a strategic move if a certain condition is met,
     * otherwise, a random move is made.
     *
     * @param board The current game board.
     * @param mark  The mark (X or O) of the clever player.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        Random rand = new Random();

        // Determine if the player will make a strategic move based on a random probability
        int possibilityForCleverPlay = rand.nextInt(100);
        if (possibilityForCleverPlay >= 75) {
            // Iterate through the board to find the player's mark and attempt a strategic move
            for (int row = 0; row < board.getSize(); row++) {
                for (int col = 0; col < board.getSize(); col++) {
                    if (board.getMark(row, col) == mark) {
                        int[] markNearAMark = findVacantSlotClever(board, row, col);
                        if (markNearAMark[0] == -1) {
                            continue;
                        }
                        board.putMark(mark, markNearAMark[0], markNearAMark[1]);
                        return;
                    }
                }
            }
        }

        // If no strategic move is made, make a random move
        int[] coordinates = getRandomCoordinates(board);
        int row = coordinates[0];
        int col = coordinates[1];
        board.putMark(mark, row, col);
    }

    /**
     * Finds a vacant slot near a given position on the game board in a strategic manner.
     * The method checks adjacent positions and returns the first vacant slot found.
     *
     * @param board The current game board.
     * @param row   The row index of the position.
     * @param col   The column index of the position.
     * @return An array containing the row and column indices of the vacant slot,
     * or {-1, -1} if no vacant slot is found.
     */
    private int[] findVacantSlotClever(Board board, int row, int col) {
        if (isSquareInBoard(board, row - 1, col) && board.getMark(row - 1, col) == Mark.BLANK) {
            return new int[]{row - 1, col};
        }
        if (isSquareInBoard(board, row, col - 1) && board.getMark(row, col - 1) == Mark.BLANK) {
            return new int[]{row, col - 1};
        }
        if (isSquareInBoard(board, row + 1, col) && board.getMark(row + 1, col) == Mark.BLANK) {
            return new int[]{row + 1, col};
        }
        if (isSquareInBoard(board, row, col + 1) && board.getMark(row, col + 1) == Mark.BLANK) {
            return new int[]{row, col + 1};
        }
        return new int[]{-1, -1};
    }

    /**
     * Checks if a given position is within the boundaries of the game board.
     *
     * @param board The current game board.
     * @param row   The row index of the position.
     * @param col   The column index of the position.
     * @return True if the position is within the board boundaries, false otherwise.
     */
    private boolean isSquareInBoard(Board board, int row, int col) {
        return row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize();
    }

    /**
     * Generates random coordinates for a vacant slot on the game board.
     * Continues generating until a vacant slot is found.
     *
     * @param board The current game board.
     * @return An array containing the row and column indices of the vacant slot.
     */
    private int[] getRandomCoordinates(Board board) {
        int row, col;
        Random rand = new Random();
        while (true) {
            row = rand.nextInt(board.getSize());
            col = rand.nextInt(board.getSize());
            if (board.getMark(row, col) == Mark.BLANK) {
                return new int[]{row, col};
            }
        }
    }
}
