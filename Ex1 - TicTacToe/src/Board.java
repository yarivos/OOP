/**
 * Represents the game board for Tic-Tac-Toe.
 * The board contains cells where players can place their marks (X or O).
 */
public class Board {
    // Default size for the game board if not specified
    private static final int DEFAULT_BOARD_SIZE = 4;

    // Size of the game board
    private final int boardSize;

    // 2D array to represent the cells of the game board
    private Mark[][] board;

    /**
     * Constructs a game board with the default size.
     * Initializes the board with blank marks.
     */
    Board() {
        this.boardSize = DEFAULT_BOARD_SIZE;
        buildBlankBoard();
    }

    /**
     * Constructs a game board with the specified size.
     * Initializes the board with blank marks.
     *
     * @param size The size of the game board.
     */
    Board(int size) {
        this.boardSize = size;
        buildBlankBoard();
    }

    // Private method to initialize the board with blank marks
    private void buildBlankBoard() {
        this.board = new Mark[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                this.board[row][col] = Mark.BLANK;
            }
        }
    }

    /**
     * Gets the size of the game board.
     *
     * @return The size of the game board.
     */
    public int getSize() {
        return boardSize;
    }

    /**
     * Places a mark on the specified cell of the board.
     *
     * @param mark The mark (X or O) to be placed on the board.
     * @param row  The row index of the cell.
     * @param col  The column index of the cell.
     * @return True if the mark was successfully placed, false if the cell is already occupied.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (getMark(row, col) == Mark.BLANK) {
            board[row][col] = mark;
            return true;
        }
        return false;
    }

    /**
     * Gets the mark at the specified cell on the board.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return The mark at the specified cell, or BLANK if the cell is out of bounds.
     */
    public Mark getMark(int row, int col) {
        if (isSquareInBoard(row, col)) {
            return board[row][col];
        }
        return Mark.BLANK;
    }

    // Private method to check if a cell is within the board boundaries
    private boolean isSquareInBoard(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }
}
