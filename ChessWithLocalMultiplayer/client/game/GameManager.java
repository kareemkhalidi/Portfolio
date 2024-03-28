package client.game;

import base.GameSaveSystem;
import base.RobustSocket;
import game.Player;
import game.pieces.ChessColor;
import server.game.GameState;
import server.game.ServerGameHandler;
import server.game.gamemodes.GameMode;
import server.game.gamemodes.Standard;
import server.net.ConnectionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of a Game Manager to manage the initialization, pausing, and shutdown of required
 * game objects.
 *
 * @author John Gauci
 */
public class GameManager {
    private static final int PORT = 18000;
    private Optional<Game> game;
    private GameState gameState;
    private boolean paused;

    /**
     * Initializes a new Game Manager
     *
     * @author John Gauci
     */
    public GameManager() {
        super();
        this.game = Optional.empty();
        this.paused = false;
        this.gameState = new GameState();
    }

    /**
     * Gets the game state.
     *
     * @return the game state.
     * @author Alexander Cooper
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Initializes a local game with the given parameters
     *
     * @param gameUI           the GameUI
     * @param firstPlayerName  the name of the first player
     * @param secondPlayerName the name of the second player
     * @param gameState        the game state
     * @author John Gauci
     */
    public void newLocalGame(final GameUI gameUI, final String firstPlayerName,
                             final String secondPlayerName, final GameState gameState) {
        final Game partialGame = this.initializeGame(gameUI, firstPlayerName, ChessColor.WHITE,
                secondPlayerName,
                ChessColor.BLACK, true, gameState);
        partialGame.mainGameHandler().addObserver(partialGame.playingUI());
        partialGame.connectionHandler = Optional.of(new ConnectionHandler(GameManager.PORT,
                partialGame.serverGameHandler()));
        partialGame.serverGameHandler().start();
        partialGame.connectionHandler.ifPresent(ConnectionHandler::start);
        partialGame.mainGameHandler().start();
        final RobustSocket mainSocket = new RobustSocket(partialGame.mainGameHandler(), "localhost",
                GameManager.PORT, partialGame.mainGameHandler.getPlayer().getId());
        final RobustSocket secondSocket = new RobustSocket(partialGame.secondGameHandler(),
                "localhost", GameManager.PORT, partialGame.secondGameHandler.getPlayer().getId());
        partialGame.mainGameHandler().addSocket(mainSocket);
        partialGame.secondGameHandler().addSocket(secondSocket);
        partialGame.playingUI().start();
        partialGame.mainGameHandler().connect();
        partialGame.secondGameHandler().connect();
        mainSocket.start();
        secondSocket.start();
        partialGame.sockets().add(mainSocket);
        partialGame.sockets().add(secondSocket);
        partialGame.playingUI.setTitleLocal(firstPlayerName, secondPlayerName);
        this.game = Optional.of(partialGame);
    }

    /**
     * Joins a network game with the given parameters
     *
     * @param gameUI     the GameUI
     * @param playerName the name of the player
     * @param host       the host address to connect to
     * @author John Gauci
     */
    public void joinNetworkGame(final GameUI gameUI, final String playerName, final String host) {
        String updatedHost = host;
        if (updatedHost.isBlank())
            updatedHost = "127.0.0.1";
        final Game partialGame = this.initializeGame(gameUI, playerName, ChessColor.BLACK,
                "Empty",
                ChessColor.BLACK, false, GameSaveSystem.getNewGame(new Standard()));
        partialGame.mainGameHandler().addObserver(partialGame.playingUI());
        partialGame.mainGameHandler().start();
        final RobustSocket mainSocket = new RobustSocket(partialGame.mainGameHandler(), updatedHost,
                GameManager.PORT, partialGame.mainGameHandler.getPlayer().getId());
        partialGame.mainGameHandler().addSocket(mainSocket);
        partialGame.playingUI().start();
        partialGame.mainGameHandler().connect();
        mainSocket.start();
        partialGame.sockets().add(mainSocket);
        partialGame.playingUI.setTitleClient(playerName, host);
        this.game = Optional.of(partialGame);
    }

    /**
     * Hosts a network game with the given parameters
     *
     * @param gameUI     the GameUI
     * @param playerName the name of the player
     * @param gameState  the game state
     * @author John Gauci
     */
    public void hostNetworkGame(final GameUI gameUI, final String playerName, GameState gameState) {
        final Game partialGame = this.initializeGame(gameUI, playerName, ChessColor.WHITE,
                "Empty",
                ChessColor.BLACK, false, gameState);
        partialGame.mainGameHandler().addObserver(partialGame.playingUI());
        partialGame.connectionHandler = Optional.of(new ConnectionHandler(GameManager.PORT,
                partialGame.serverGameHandler()));
        partialGame.serverGameHandler().start();
        partialGame.connectionHandler.ifPresent(ConnectionHandler::start);
        partialGame.mainGameHandler().start();
        final RobustSocket mainSocket = new RobustSocket(partialGame.mainGameHandler(), "localhost",
                GameManager.PORT, partialGame.mainGameHandler.getPlayer().getId());
        partialGame.mainGameHandler().addSocket(mainSocket);
        partialGame.playingUI().start();
        partialGame.mainGameHandler().connect();
        mainSocket.start();
        partialGame.sockets().add(mainSocket);
        partialGame.playingUI.setTitleHost(playerName);
        this.game = Optional.of(partialGame);
    }


    private Game initializeGame(final GameUI gameUI, final String mainPlayerName,
                                final ChessColor mainPlayerColor,
                                final String secondPlayerName,
                                final ChessColor secondPlayerColor,
                                final boolean isLocal, final GameState gameState) {
        this.gameState = gameState;
        final PauseUI pauseUI = new PauseUI(this, gameUI, gameUI.getShell(), new GameState());
        pauseUI.clear();
        final ClientGameHandler mainGameHandler =
                new ClientGameHandler(new Player(mainPlayerName, mainPlayerColor), this);
        final ClientGameHandler secondGameHandler =
                new ClientGameHandler(new Player(secondPlayerName, secondPlayerColor), this);
        final GameController gameInputController =
                new GameController(this, mainGameHandler,
                        secondGameHandler, isLocal);
        final PlayingUI playingUI = new PlayingUI(mainGameHandler, gameUI.getShell(),
                gameInputController, gameState.getGameMode());
        final ServerGameHandler serverGameHandler = new ServerGameHandler(gameState);
        gameUI.clear();
        return new Game(mainGameHandler, secondGameHandler, new ArrayList<>(2),
                gameInputController, playingUI, pauseUI, serverGameHandler, Optional.empty());

    }

    /**
     * Switches the game to be paused or over if it is not already so
     *
     * @author John Gauci
     */
    public void pauseResumeGameOver() {
        if (this.game.isPresent()) {
            final Game currentGame = this.game.get();
            if (this.paused) {
                this.paused = false;
                currentGame.pauseUI().clear();
                currentGame.playingUI().set();
                currentGame.playingUI().start();
                currentGame.gameInputController().setPaused(false);
            } else {
                this.paused = true;
                currentGame.gameInputController().setPaused(true);
                currentGame.pauseUI().getDisplay().syncExec(() -> {
                    currentGame.playingUI().stop();
                    currentGame.playingUI().clear();
                    currentGame.pauseUI().set(currentGame.mainGameHandler.getState());
                });
            }
        }

    }

    /**
     * Shuts down the game and returns to the main menu
     *
     * @param gameUI  the GameUI
     * @param pauseUI the PauseUI
     * @author John Gauci
     */
    public void mainMenu(final GameUI gameUI, final PauseUI pauseUI) {
        if (this.game.isPresent()) {
            pauseUI.clear();
            this.shutDown(gameUI);
        }
    }


    /**
     * Shuts down the game
     *
     * @param gameUI the GameUI
     * @author John Gauci
     */
    public void shutDown(final GameUI gameUI) {
        if (this.game.isPresent()) {
            this.paused = false;
            final Game currentGame = this.game.get();
            currentGame.mainGameHandler().disconnect();
            currentGame.secondGameHandler().disconnect();
            currentGame.sockets().forEach(RobustSocket::stop);
            currentGame.gameInputController().setPaused(false);
            currentGame.playingUI().stop();
            currentGame.connectionHandler().ifPresent(ConnectionHandler::stop);
            currentGame.serverGameHandler().stop();
            this.game = Optional.empty();
            gameUI.set();
        }
    }

    private static final class Game {
        private final ClientGameHandler mainGameHandler;
        private final ClientGameHandler secondGameHandler;
        private final List<RobustSocket> sockets;
        private final GameController gameInputController;
        private final PlayingUI playingUI;
        private final PauseUI pauseUI;
        private final ServerGameHandler serverGameHandler;
        private Optional<ConnectionHandler> connectionHandler;

        private Game(final ClientGameHandler mainGameHandler, final ClientGameHandler secondGameHandler,
                     final List<RobustSocket> sockets,
                     final GameController gameInputController,
                     final PlayingUI playingUI, final PauseUI pauseUI, final ServerGameHandler serverGameHandler,
                     final Optional<ConnectionHandler> connectionHandler) {
            super();
            this.mainGameHandler = mainGameHandler;
            this.secondGameHandler = secondGameHandler;
            this.sockets = sockets;
            this.gameInputController = gameInputController;
            this.playingUI = playingUI;
            this.pauseUI = pauseUI;

            this.serverGameHandler = serverGameHandler;
            this.connectionHandler = connectionHandler;
        }

        ClientGameHandler mainGameHandler() {
            return this.mainGameHandler;
        }

        ClientGameHandler secondGameHandler() {
            return this.secondGameHandler;
        }

        List<RobustSocket> sockets() {
            return this.sockets;
        }

        GameController gameInputController() {
            return this.gameInputController;
        }

        PlayingUI playingUI() {
            return this.playingUI;
        }

        PauseUI pauseUI() {
            return this.pauseUI;
        }

        ServerGameHandler serverGameHandler() {
            return this.serverGameHandler;
        }

        Optional<ConnectionHandler> connectionHandler() {
            return this.connectionHandler;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            final var other = (Game) obj;
            return Objects.equals(this.mainGameHandler, other.mainGameHandler) &&
                    Objects.equals(this.secondGameHandler, other.secondGameHandler) &&
                    Objects.equals(this.sockets, other.sockets) &&
                    Objects.equals(this.gameInputController, other.gameInputController) &&
                    Objects.equals(this.playingUI, other.playingUI) &&
                    Objects.equals(this.pauseUI, other.pauseUI) &&
                    Objects.equals(this.serverGameHandler, other.serverGameHandler) &&
                    Objects.equals(this.connectionHandler, other.connectionHandler);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.mainGameHandler, this.secondGameHandler, this.sockets, this.gameInputController, this.playingUI, this.pauseUI, this.serverGameHandler, this.connectionHandler);
        }


    }
}
