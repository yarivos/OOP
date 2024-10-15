public interface Player {
    /**
     * Represents a player's turn in a Tic-Tac-Toe game.
     * This method allows a player to make a move on the game board.
     *
     * @param board The current game board on which the player is making a move.
     * @param mark  The player's mark (X or O) indicating the symbol to be placed on the board.
     */
    void playTurn(Board board, Mark mark);
}
