import java.util.Scanner;

public class HumanPlayer implements Player {
    HumanPlayer() {}

    /**
     * Represents a player's turn in a Tic-Tac-Toe game.
     * This method allows a player to make a move on the game board.
     *
     * @param board The current game board on which the player is making a move.
     * @param mark The player's mark (X or O) indicating the symbol to be placed on the board.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        // Determine the symbol associated with the player's mark (X or O)
        String markSymbol = "X";
        if (mark == Mark.O) {
            markSymbol = "O";
        }

        // Prompt the player for input
        System.out.println(Constants.playerRequestInputString(markSymbol));

        // Variables to store player's input coordinates, row, and column
        int coordinates, row, col;

        // Continue looping until a valid move is made
        while (true) {
            // Read player input for coordinates
            coordinates = KeyboardInput.readInt();

            // Extract row and column from the coordinates
            row = coordinates / 10;
            col = coordinates % 10;

            // Check if the selected square is within the board boundaries
            if (isSquareInBoard(board, row, col)) {
                // Check if the selected square is unoccupied
                if (board.getMark(row, col) == Mark.BLANK) {
                    // Place the player's mark on the board and exit the loop
                    board.putMark(mark, row, col);
                    return;
                } else {
                    // Display message for an occupied coordinate
                    System.out.println(Constants.OCCUPIED_COORDINATE);
                }
            } else {
                // Display message for an invalid coordinate
                System.out.println(Constants.INVALID_COORDINATE);
            }
        }
    }

    private boolean isSquareInBoard(Board board, int row, int col) {
        return row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize();
    }
}