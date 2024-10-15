/**
 * Represents a Tic-Tac-Toe game with two players and a game board.
 * The game can be configured with different board sizes and win streaks.
 */
public class Game {
    // Default win streak for the game
    private static final int DEFAULT_WIN_STREAK = 3;

    // Players participating in the game
    private Player playerX;
    private Player playerO;

    // Renderer for displaying the game board
    private Renderer renderer;

    // Game board
    private Board board;

    // Win streak required for a player to win
    private int winStreak;

    /**
     * Constructs a Game instance with default win streak (3).
     *
     * @param playerX  The player representing 'X'.
     * @param playerO  The player representing 'O'.
     * @param renderer The renderer for displaying the game board.
     */
    Game(Player playerX, Player playerO, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.board = new Board();
        this.winStreak = DEFAULT_WIN_STREAK;
    }

    /**
     * Constructs a Game instance with specified board size and win streak.
     *
     * @param playerX   The player representing 'X'.
     * @param playerO   The player representing 'O'.
     * @param size      The size of the game board.
     * @param winStreak The win streak required for a player to win.
     * @param renderer  The renderer for displaying the game board.
     */
    Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.board = new Board(size);
        this.winStreak = Math.min(winStreak, size);
    }

    /**
     * Gets the win streak required for a player to win.
     *
     * @return The win streak.
     */
    public int getWinStreak() {
        return winStreak;
    }

    /**
     * Gets the size of the game board.
     *
     * @return The size of the game board.
     */
    public int getBoardSize() {
        return board.getSize();
    }

    /**
     * Runs the Tic-Tac-Toe game until there is a winner or the board is filled.
     *
     * @return The mark of the winning player (X or O), or BLANK if there is no winner.
     */
    public Mark run() {
        int turnsPlayed = 0;
        while (turnsPlayed < getBoardSize() * getBoardSize()) {
            if (turnsPlayed % 2 == 0) {
                playerX.playTurn(board, Mark.X);
                renderer.renderBoard(board);
                if (turnsPlayed >= winStreak * 2 - 2) {
                    if (isThereAWinner(Mark.X)) {
                        return Mark.X;
                    }
                }
            } else {
                playerO.playTurn(board, Mark.O);
                renderer.renderBoard(board);
                if (turnsPlayed >= winStreak * 2 - 2) {
                    if (isThereAWinner(Mark.O)) {
                        return Mark.O;
                    }
                }
            }
            turnsPlayed++;
        }
        return Mark.BLANK;
    }

    /**
     * Checks if a player with the given mark has achieved a win streak on the game board.
     *
     * @param mark The mark of the player (X or O) to check for a win.
     * @return True if the player has won, false otherwise.
     */
    private boolean isThereAWinner(Mark mark) {
        // Check rows and columns for a win
        for (int row = 0; row < getBoardSize(); row++) {
            int rowStreak = 0;
            int colStreak = 0;
            for (int col = 0; col < getBoardSize(); col++) {
                if (board.getMark(row, col) == mark) {
                    rowStreak += 1;
                    if (rowStreak == winStreak) return true;
                } else {
                    rowStreak = 0;
                }
                if (board.getMark(col, row) == mark) {
                    colStreak += 1;
                    if (colStreak == winStreak) return true;
                } else {
                    colStreak = 0;
                }
            }
        }

        // Check diagonals for a win
        for (int row = 0; row <= getBoardSize() - winStreak; row++) {
            for (int col = 0; col <= getBoardSize() - winStreak; col++) {
                boolean mainDiagonalStreak = true;
                boolean antiDiagonalStreak = true;
                for (int diagonalInterval = 0; diagonalInterval < winStreak; diagonalInterval++) {
                    // Check main diagonal
                    if (board.getMark(row + diagonalInterval, col + diagonalInterval) != mark) {
                        mainDiagonalStreak = false;
                    }
                    // Check anti-diagonal
                    if (board.getMark(row + diagonalInterval,
                            col + winStreak - 1 - diagonalInterval)
                            != mark) {
                        antiDiagonalStreak = false;
                    }
                }
                // If any diagonal is a win, return true
                if (mainDiagonalStreak || antiDiagonalStreak) {
                    return true;
                }
            }
        }
        return false;
    }
}
