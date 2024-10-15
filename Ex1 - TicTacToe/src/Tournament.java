/**
 * Represents a Tic-Tac-Toe tournament between two players over multiple rounds.
 * Tracks the results of each round and displays the final tournament results.
 */
public class Tournament {
    // Number of rounds in the tournament
    private int rounds;

    // Renderer for displaying the game board
    private Renderer renderer;

    // Players participating in the tournament
    private Player player1;
    private Player player2;

    /**
     * Constructs a Tournament instance with the specified number of rounds, renderer, and players.
     *
     * @param rounds   The number of rounds in the tournament.
     * @param renderer The renderer for displaying the game board.
     * @param player1  The first player participating in the tournament.
     * @param player2  The second player participating in the tournament.
     */
    Tournament(int rounds, Renderer renderer, Player player1, Player player2) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Conducts a Tic-Tac-Toe tournament with the specified board size, win streak, and player names.
     * Displays the results of each round and the final tournament results.
     *
     * @param size        The size of the game board.
     * @param winStreak   The win streak required for a player to win.
     * @param playerName1 The name of the first player.
     * @param playerName2 The name of the second player.
     */
    public void playTournament(int size, int winStreak, String playerName1, String playerName2) {
        int playerWinCounter1 = 0;
        int playerWinCounter2 = 0;
        int tieCounter = 0;
        Game game;
        for (int round = 0; round < rounds; round++) {
            if (round % 2 == 0) {
                game = new Game(player1, player2, size, winStreak, renderer);
            } else {
                game = new Game(player2, player1, size, winStreak, renderer);
            }
            Mark winnerMark = game.run();
            switch (winnerMark) {
                case BLANK:
                    tieCounter++;
                    break;
                case X:
                    if (round % 2 == 0) {
                        playerWinCounter1++;
                    } else {
                        playerWinCounter2++;
                    }
                    break;
                case O:
                    if (round % 2 == 0) {
                        playerWinCounter2++;
                    } else {
                        playerWinCounter1++;
                    }
                    break;
            }
        }
        // Summary of the score
        System.out.println("######### Results #########");
        System.out.printf("Player 1, %s won: %d rounds\n", playerName1, playerWinCounter1);
        System.out.printf("Player 2, %s won: %d rounds\n", playerName2, playerWinCounter2);
        System.out.printf("Ties: %d\n", tieCounter);
    }

    /**
     * The main method to run the Tic-Tac-Toe tournament.
     * Accepts command line arguments for the number of rounds, board size, win streak, renderer type,
     * and player names. Creates instances of the tournament components and initiates the tournament.
     *
     * @param args Command line arguments specifying the tournament parameters.
     */
    public static void main(String[] args) {
        int rounds = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);

        // Build renderer based on command line arguments
        Renderer renderer = new RendererFactory().buildRenderer(args[3], size);
        if (renderer == null) {
            System.out.println("Choose a renderer, and start again.\n" +
                    "Please choose one of the following [console, none]");
            return;
        }

        // Extract player names from command line arguments
        String playerName1 = args[4];
        String playerName2 = args[5];

        // Build players based on command line arguments
        Player player1 = new PlayerFactory().buildPlayer(playerName1);
        Player player2 = new PlayerFactory().buildPlayer(playerName2);
        if (player1 == null || player2 == null) {
            System.out.println("Choose a player, and start again.\n" +
                    "The players: [human, clever, whatever, genius]");
            return;
        }

        // Create and run the tournament
        Tournament tournament = new Tournament(rounds, renderer, player1, player2);
        tournament.playTournament(size, winStreak, playerName1, playerName2);
    }
}
