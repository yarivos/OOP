import java.util.Random;

/**
 * Represents a player in a Tic-Tac-Toe game that makes random moves without any specific strategy.
 */
public class WhateverPlayer implements Player {
    WhateverPlayer() {
    }

    /**
     * Plays a turn for the WhateverPlayer by making a random move on the given game board.
     * The player continues to generate random coordinates until it finds an empty (BLANK) cell,
     * and then places its mark on that cell.
     *
     * @param board The current game board.
     * @param mark  The mark (X or O) of the WhateverPlayer.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        Random rand = new Random();
        int row, col;
        while (true) {
            row = rand.nextInt(board.getSize());
            col = rand.nextInt(board.getSize());
            if (board.getMark(row, col) == Mark.BLANK) {
                board.putMark(mark, row, col);
                return;
            }
        }
    }
}
