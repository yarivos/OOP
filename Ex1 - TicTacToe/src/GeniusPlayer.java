import java.util.Random;

/**
 * Represents a genius-level player in a Tic-Tac-Toe game.
 * The genius player attempts to make strategic moves by checking for vacant slots near its own marks,
 * both orthogonally and diagonally.
 */
public class GeniusPlayer implements Player {
    /**
     * Plays a turn for the genius player on the given game board.
     * The player attempts to make a strategic move by checking for vacant slots near its own marks.
     * If no strategic move is possible, the player makes a random move.
     *
     * @param board The current game board.
     * @param mark  The mark (X or O) of the genius player.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getMark(row, col) == mark) {
                    int[] markNearAMark = findVacantSlotGenius(board, row, col);
                    if (markNearAMark[0] == -1) {
                        continue;
                    }
                    board.putMark(mark, markNearAMark[0], markNearAMark[1]);
                    return;
                }
            }
        }
        // Play randomly only if cant find a vacant slot near a mark.
        int[] coordinates = getRandomCoordinates(board);
        int row = coordinates[0];
        int col = coordinates[1];
        board.putMark(mark, row, col);
    }

    /**
     * Finds a vacant slot near a given position on the game board in a strategic manner.
     * The method checks orthogonally and diagonally for vacant slots.
     *
     * @param board The current game board.
     * @param row   The row index of the position.
     * @param col   The column index of the position.
     * @return An array containing the row and column indices of the vacant slot,
     * or {-1, -1} if no vacant slot is found.
     */
    private int[] findVacantSlotGenius(Board board, int row, int col) {
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
        // diagonals
        if (isSquareInBoard(board, row - 1, col - 1)
                && board.getMark(row - 1, col - 1) == Mark.BLANK) {
            return new int[]{row - 1, col - 1};
        }
        if (isSquareInBoard(board, row + 1, col - 1)
                && board.getMark(row + 1, col - 1) == Mark.BLANK) {
            return new int[]{row + 1, col - 1};
        }
        if (isSquareInBoard(board, row + 1, col + 1)
                && board.getMark(row + 1, col + 1) == Mark.BLANK) {
            return new int[]{row + 1, col + 1};
        }
        if (isSquareInBoard(board, row - 1, col + 1)
                && board.getMark(row - 1, col + 1) == Mark.BLANK) {
            return new int[]{row - 1, col + 1};
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
