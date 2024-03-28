import client.game.GameUI;

/**
 * Main class that initializes all necessary objects and starts the game and ui.
 *
 * @author John Gauci
 */
class Main {

    public static void main(final String[] args) {
        final var gameUI = new GameUI();
        gameUI.open();
    }

}
