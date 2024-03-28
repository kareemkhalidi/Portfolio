package client.game;

import base.Controller;
import base.KeyInput;
import base.MouseInput;
import base.Vector2;
import game.pieces.Piece;

import java.util.Optional;

/**
 * Implements an abstract strategy class for communicating between a ClientGameHandler and a GameUI.
 *
 * @author John Gauci
 */

public class GameController extends Controller {
    private final GameManager gameManager;
    private final ClientGameHandler mainHandler;
    private final ClientGameHandler secondHandler;
    private final boolean isLocal;
    private boolean paused;
    private Optional<Piece> pieceSelected;

    /**
     * Initializes a GameController with a subject to communicate to
     *
     * @param gameManager   the Game Manager for this controller
     * @param mainHandler   the main ClientGameHandler for this controller
     * @param secondHandler the second ClientGameHandler for this controller
     * @param isLocal       whether the game is a local game
     * @author John Gauci
     */
    public GameController(final GameManager gameManager, final ClientGameHandler mainHandler,
                          final ClientGameHandler secondHandler, final boolean isLocal) {
        super();
        this.gameManager = gameManager;
        this.mainHandler = mainHandler;
        this.secondHandler = secondHandler;
        this.paused = false;
        this.isLocal = isLocal;
        this.pieceSelected = Optional.empty();
    }

    /**
     * Set the paused state of the controller to the given state
     *
     * @param paused whether the game is paused
     * @author John Gauci
     */
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }

    /**
     * Processes the given keyInput from the GameUI
     *
     * @param keyInput the key input
     * @author John Gauci
     */
    @Override
    public void processKeyInput(final KeyInput keyInput) {
        switch (keyInput.getCharacter()) {
            case '\t' -> this.gameManager.pauseResumeGameOver();
            case '`' -> {
                if (GameUI.DEBUG) GameUI.printDebugInfo();
            }
        }
        if (keyInput.getKeycode() == KeyInput.ESCAPE) {
            this.gameManager.pauseResumeGameOver();
        }
    }

    /**
     * Processes the given mouseInput from the GameUI
     *
     * @param mouseInput the key input
     * @author Kareem Khalidi
     */
    @Override
    public void processMouseInput(final MouseInput mouseInput) {
        if (!this.paused && this.mainHandler.getState().getActivePlayer().isPresent()) {
            final var boardRow = -(mouseInput.getRow() / (PlayingUI.CANVAS_DIMENSIONS >>> 3)) + 7;
            final var boardColumn = mouseInput.getColumn() / (PlayingUI.CANVAS_DIMENSIONS >>> 3);
            if (this.pieceSelected.isEmpty()) {
                final var maybePiece = this.mainHandler.getState().getPiece(new Vector2(boardColumn, boardRow));
                if (maybePiece.isPresent()) {
                    if (maybePiece.get().getChessColor() == this.mainHandler.getState().getActiveColor() && ((this.mainHandler.getState().getActiveColor() == this.mainHandler.getPlayer().getChessColor()) || this.isLocal)) {
                        this.pieceSelected = maybePiece;
                        for (final Vector2 move : this.pieceSelected.get().getValidMoves(this.mainHandler.getState())) {
                            this.mainHandler.highlightSquare(move);
                        }
                    } else {
                        this.pieceSelected = Optional.empty();
                    }
                }
            } else {
                if (this.mainHandler.getState().getPiece(new Vector2(boardColumn, boardRow)).isPresent() && this.mainHandler.getState().getActivePlayer().get().getChessColor() == this.mainHandler.getState().getPiece(new Vector2(boardColumn, boardRow)).get().getChessColor()) {
                    this.pieceSelected = this.mainHandler.getState().getPiece(new Vector2(boardColumn, boardRow));
                    this.mainHandler.clearHighlightedSquares();
                    for (final Vector2 move : this.pieceSelected.get().getValidMoves(this.mainHandler.getState())) {
                        this.mainHandler.highlightSquare(move);
                    }
                } else {
                    this.attemptMove(this.pieceSelected.get().getPosition(), new Vector2(boardColumn, boardRow));
                    this.mainHandler.clearHighlightedSquares();
                    this.pieceSelected = Optional.empty();
                }
            }
        }
    }

    private void attemptMove(final Vector2 startPosition, final Vector2 endPosition) {
        if (this.mainHandler.getState().getActivePlayer().isPresent()) {
            final var activePlayer = this.mainHandler.getState().getActivePlayer().get();
            final var firstPlayer = this.mainHandler.getPlayer();
            final var secondPlayer = this.secondHandler.getPlayer();
            if (activePlayer.equals(firstPlayer)) {
                this.mainHandler.movePiece(startPosition, endPosition);
            } else if (this.isLocal && activePlayer.equals(secondPlayer)) {
                this.secondHandler.movePiece(startPosition, endPosition);
            }
        }
    }


}
