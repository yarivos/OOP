/**
 * Factory class responsible for creating instances of different player types in a Tic-Tac-Toe game.
 */
public class PlayerFactory {
    PlayerFactory() {
    }

    /**
     * Builds and returns an instance of a player based on the provided player type.
     *
     * @param type The type of player to be created (e.g., "human", "clever", "whatever", "genius").
     * @return An instance of the corresponding player type, or null if the type is not recognized.
     */
    public Player buildPlayer(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "human":
                return new HumanPlayer();
            case "clever":
                return new CleverPlayer();
            case "whatever":
                return new WhateverPlayer();
            case "genius":
                return new GeniusPlayer();
            default:
                return null;
        }
    }

}
