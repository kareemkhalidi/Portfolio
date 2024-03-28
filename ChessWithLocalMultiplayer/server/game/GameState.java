package server.game;

import base.*;
import client.game.PlayingUI;
import game.Board;
import game.Player;
import game.pieces.*;
import server.game.gamemodes.GameMode;
import server.game.gamemodes.Standard;

import java.util.*;

/**
 * Class that stores the state information for the game
 *
 * @author Alexander Cooper
 * @author Logan Sandlin
 * @author John Gauci
 */
public class GameState extends State {

    private final List<Piece> activePieces;
    private final List<Player> players;
    private final Board board;
    private final GameMode gameMode;
    private ChessColor turnColor;
    private final boolean[] castlingRights;
    private Vector2 enPassantTarget;
    private int halfMoves;
    private int fullMoves;
    private long whiteTurnClock;
    private long blackTurnClock;
    private long turnClockTimer;
    private boolean gameOver;

    /**
     * Constructor for a new game data object
     *
     * @param pieces the game's pieces
     * @param turnColor the active players turn
     * @param castlingRights the castling rights
     * @param enPassantTarget the En Passant target
     * @param halfMoves the half moves
     * @param fullMoves the full moves
     * @param gameMode  the game mode for the game
     * @author John Gauci
     */
    public GameState(final List<Piece> pieces, ChessColor turnColor, final boolean[] castlingRights, Vector2 enPassantTarget,
	    int halfMoves, int fullMoves, final GameMode gameMode) {
        super();
        this.activePieces = pieces;
        this.players = new ArrayList<>(2);
        this.board = new Board();
        this.gameMode = gameMode;
        this.turnColor = turnColor;
        this.castlingRights = castlingRights;
        this.enPassantTarget = enPassantTarget;
        this.halfMoves = halfMoves;
        this.fullMoves = fullMoves;
        this.whiteTurnClock = gameMode.getTimeLimitMinutes() * 60L * 1000L;
        this.blackTurnClock = gameMode.getTimeLimitMinutes() * 60L * 1000L;
        this.turnClockTimer = System.currentTimeMillis();
        this.gameOver = false;
    }


    /**
     * Overloaded constructor that initializes all values to default values
     *
     * @author John Gauci
     */
    public GameState() {
        super();
        this.activePieces = new ArrayList<>(0);
        this.players = new ArrayList<>(0);
        this.board = new Board();
        this.gameMode = new Standard();
        this.turnColor = ChessColor.WHITE;
        this.castlingRights = new boolean[4];
        this.enPassantTarget = null;
        this.halfMoves = 0;
        this.fullMoves = 0;
        this.whiteTurnClock = this.gameMode.getTimeLimitMinutes() * 60L * 1000L;
        this.blackTurnClock = this.gameMode.getTimeLimitMinutes() * 60L * 1000L;
        this.turnClockTimer = 0L;
        this.gameOver = false;
    }

    /**
     * Returns the board of the game state
     *
     * @return board of game state
     * @author John Gauci
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Adds a player to the game state
     *
     * @param player player to be added
     * @author John Gauci
     */
    public void addPlayer(final Player player) {
        this.players.add(player);
    }

    /**
     * Removes a player from the game state
     *
     * @param player player to be removed
     * @author John Gauci
     */
    public void removePlayer(final Player player) {
        this.players.remove(player);
    }

    /**
     * Returns the players in the game
     *
     * @return arraylist of all players player stats objects
     * @author John Gauci
     */
    public Iterable<User> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    /**
     * Returns the player with the given playerID
     *
     * @param playerID playerID
     * @return player with playerID
     * @author John Gauci
     */
    public Optional<Player> getPlayer(final long playerID) {
        for (final Player player : this.players) {
            if (player.getId() == playerID) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the piece at the position if present
     *
     * @param position position of piece
     * @return piece at position
     * @author John Gauci
     */
    public Optional<Piece> getPiece(final Vector2 position) {
        final var row = position.y();
        final var column = position.x();
        if (row > -1 && row < 9 && column > -1 && column < 9) {
            for (final Piece piece : this.activePieces) {
                if (piece.getBoardRow() == row && piece.getBoardColumn() == column)
                    return Optional.of(piece);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the player for whose turn it is, if present
     *
     * @return player with active turn
     * @author John Gauci
     */
    public Optional<Player> getActivePlayer() {
        for (final Player player : this.players) {
            if (player.getChessColor() == this.turnColor)
                return Optional.of(player);
        }
        return Optional.empty();
    }

    /**
     * Returns the active color for the player whose turn it is
     *
     * @return active player color, white if no players
     * @author John Gauci
     */
    public ChessColor getActiveColor() {
        return this.turnColor;
    }

    /**
     * Returns the active pieces in the board
     *
     * @return active pieces
     * @author John Gauci
     */
    public List<Piece> getActivePieces() {
        return this.activePieces;
    }

    /**
     * Determines if a position on the board is in check.
     *
     * @param position the position on the board.
     * @return a value determining if the position is in check.
     * @author Alexander Cooper
     */
    public boolean positionInCheck(final Vector2 position) {
        for (final Piece piece : this.activePieces) {
            if (piece.getChessColor() != this.turnColor) {
                final List<Vector2> threats = piece.getPotentialThreats(this);
                for (final var threat : threats) {
                    if (threat.equals(position))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Cycles active player turn
     *
     * @author Alex Cooper
     * @author John Gauci
     */
    public void cycleTurn() {
        switch (this.turnColor) {
            case WHITE -> this.turnColor = ChessColor.BLACK;
            case BLACK -> this.turnColor = ChessColor.WHITE;
        }
        if (this.turnColor == ChessColor.WHITE)
            // black made a move, increment fullMoves
            this.fullMoves++;
        this.removeExpiredEnPassantTarget();
        this.turnClockTimer = System.currentTimeMillis();
    }

    /**
     * Draws the GameState entities to the given window
     *
     * @param window the window to be drawn to
     * @author John Gauci
     */
    public void draw(final Window window) {
        this.board.draw(window);
        this.activePieces.forEach(piece -> piece.draw(window));
        if (this.gameMode.isTimed()) {
            if(this.getActivePlayer().isEmpty()){
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS - 256, "Black: " + this.getBlackTurnClockDisplayValue(), 20, 20, 0, 1, 0, 0, "Times");
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, 0, "White: " + this.getWhiteTurnClockDisplayValue(), 20, 20, 0, 1, 0, 0, "Times");
            }
            else if (this.getActivePlayer().get().getChessColor() == ChessColor.WHITE) {
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS - 256, "Black: " + this.getBlackTurnClockDisplayValue(), 20, 20, 0, 1, 0, 0, "Times");
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, 0, "White: " + this.getWhiteTurnClockDisplayValue(), 20, 20, 0, 5, 0, 0, "Times");
            } else {
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS - 256, "Black: " + this.getBlackTurnClockDisplayValue(), 20, 20, 0, 5, 0, 0, "Times");
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, 0, "White: " + this.getWhiteTurnClockDisplayValue(), 20, 20, 0, 1, 0, 0, "Times");
            }
        }
        else if(this.getActivePlayer().isPresent()){
            if (this.getActivePlayer().get().getChessColor() == ChessColor.WHITE) {
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS - 94, "Black", 20, 20, 0, 1, 0, 0, "Times");
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, 0, "White", 20, 20, 0, 5, 0, 0, "Times");
            } else {
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS - 94, "Black", 20, 20, 0, 5, 0, 0, "Times");
                window.drawText(PlayingUI.CANVAS_DIMENSIONS, 0, "White", 20, 20, 0, 1, 0, 0, "Times");
            }
        }
    }

    /**
     * Determines if the game is over.
     *
     * @return a boolean determining if the game is over
     * @author Alexander Cooper
     */
    public boolean isGameOver() {
        if (this.gameMode != null && this.players.size() == 2) {
            final GameOverType gameOverType = this.gameMode.isOver(this);
            this.gameOver = gameOverType != GameOverType.NOT_OVER;
        }
        return this.gameOver;
    }

    /**
     * Returns true if the game has ended.
     *
     * @return A boolean representing the game ended state.
     * @author Alexander Cooper
     */
    public boolean hasGameEnded() {
        return this.gameOver;
    }

    /**
     * Removes the given piece from the active pieces
     *
     * @param piece piece to be removed
     * @author John Gauci
     */
    public void removeActivePiece(final Piece piece) {
        this.activePieces.remove(piece);
    }

    /**
     * Adds the given piece to the active pieces
     *
     * @param activePiece piece to be added
     * @author John Gauci
     */
    public void addActivePiece(final Piece activePiece) {
        this.activePieces.add(activePiece);

    }

    /**
     * Decrements the turn clock.
     *
     * @author Alexander Cooper
     */
    public void decrementTurnClock() {
        final long timePassed = System.currentTimeMillis() - this.turnClockTimer;
        switch (this.turnColor) {
            case WHITE -> this.whiteTurnClock -= timePassed;
            case BLACK -> this.blackTurnClock -= timePassed;
        }
        this.turnClockTimer = System.currentTimeMillis();
    }

    /**
     * Gets the turn clock in seconds.
     *
     * @return the turn clock.
     * @author Alexander Cooper
     */
    public int getTurnClock() {
        if (this.turnColor == ChessColor.WHITE)
            return (int) (this.whiteTurnClock / 1000);
        return (int) (this.blackTurnClock / 1000);
    }

    /**
     * Gets the white player's turn clock display value.
     *
     * @return the white player's turn clock display value.
     * @author Alexander Cooper
     */
    private String getWhiteTurnClockDisplayValue() {
        final int seconds = (int) (this.whiteTurnClock / 1000);
        return GameState.getTimeDisplayValue(seconds);
    }

    /**
     * Gets the black player's turn clock display value.
     *
     * @return the black player's turn clock display value.
     * @author Alexander Cooper
     */
    private String getBlackTurnClockDisplayValue() {
        final int seconds = (int) (this.blackTurnClock / 1000);
        return GameState.getTimeDisplayValue(seconds);
    }

    /**
     * Converts seconds into am hh:mm:ss formatted string.
     *
     * @param totalSecs the total amount of seconds
     * @return the formatted string.
     * @author Alexander Cooper
     */
    private static String getTimeDisplayValue(final int totalSecs) {
        final int hours = totalSecs / 3600;
        final int minutes = (totalSecs % 3600) / 60;
        final int seconds = totalSecs % 60;
        final StringBuilder displayValue = new StringBuilder(9);
        if (hours < 10)
            displayValue.append('0');
        displayValue.append(hours);
        displayValue.append(':');
        if (minutes < 10)
            displayValue.append('0');
        displayValue.append(minutes);
        displayValue.append(':');
        if (seconds < 10)
            displayValue.append('0');
        displayValue.append(seconds);
        return displayValue.toString();
    }

    /**
     * Revokes castling rights foa player.
     *
     * @param side the side that the rights are being revoked from.
     * @author Alexander Cooper
     */
    public void revokeCastlingRights(final CastlingSide side) {
        if (this.turnColor == ChessColor.WHITE && side == CastlingSide.KING)
            this.castlingRights[0] = false;
        else if (this.turnColor == ChessColor.WHITE && side == CastlingSide.QUEEN)
            this.castlingRights[1] = false;
        else if (this.turnColor == ChessColor.BLACK && side == CastlingSide.KING)
            this.castlingRights[2] = false;
        else if (this.turnColor == ChessColor.BLACK && side == CastlingSide.QUEEN)
            this.castlingRights[3] = false;
    }

    /**
     * Returns a list of the present castling rights
     *
     * @return current castling rights
     * @author Alexander Cooper
     */
    public List<CastlingSide> getCastlingRights() {
        final var rights = new ArrayList<CastlingSide>(2);
        if ((this.turnColor == ChessColor.WHITE && this.castlingRights[0]) ||
                (this.turnColor == ChessColor.BLACK && this.castlingRights[2]))
            rights.add(CastlingSide.KING);
        if ((this.turnColor == ChessColor.WHITE && this.castlingRights[1]) ||
                (this.turnColor == ChessColor.BLACK && this.castlingRights[3]))
            rights.add(CastlingSide.QUEEN);
        return rights;
    }

    /**
     * Gets the string format of the castling rights.
     *
     * @return the string format of castling rights.
     * @author Alexander Cooper
     */
    public String getCastlingRightsString() {
	StringBuilder rights = new StringBuilder();
	boolean allFalse = true;
	for (int i = 0; i < this.castlingRights.length; i++) {
            if (this.castlingRights[i]) {
                allFalse = false;
                switch (i) {
                    case 0 -> rights.append('K');
                    case 1 -> rights.append('Q');
                    case 2 -> rights.append('k');
                    case 3 -> rights.append('q');
                }
            }
        }

	if (allFalse)
	    return "-";
	return rights.toString();

    }

    /**
     * Adds an En Passant target.
     *
     * @param target the board position of the targeted pawn
     * @author Alexander Cooper
     */
    public void addEnPassantTarget(final Vector2 target) {
	    this.enPassantTarget = target;
    }

    /**
     * Removes the En Passant target.
     *
     * @author Alexander Cooper
     */
    public void removeEnPassantTarget() {
	this.enPassantTarget = null;
    }

    /**
     * Removes all En Passant targets that have expired. To be called
     * at the end of every turn.
     *
     * @author Alexander Cooper
     */
    private void removeExpiredEnPassantTarget() {
        final int rowToFilterOut = this.turnColor == ChessColor.WHITE ? 2 : 5;
        if (this.enPassantTarget != null && this.enPassantTarget.y() == rowToFilterOut)
            this.enPassantTarget = null;

    }

    /**
     * Gets the En Passant targets.
     *
     * @return the En Passant targets.
     * @author Alexander Cooper
     */
    public Optional<Vector2> getEnPassantTarget() {
        if (this.enPassantTarget == null)
            return Optional.empty();
        return Optional.of(this.enPassantTarget);
    }

    /**
     * Increments half moves by 1.
     *
     * @author Alexander Cooper
     */
    public void incrementHalfMoves() {
        this.halfMoves++;
    }

    /**
     * resets half moves.
     *
     * @author Alexander Cooper
     */
    public void resetHalfMoves() {
        this.halfMoves = 0;
    }

    /**
     * Gets half moves.
     *
     * @return the half moves
     * @author Alexander Cooper
     */
    public int getHalfMoves() {
        return this.halfMoves;
    }

    /**
     * Gets full moves.
     *
     * @return the full moves
     * @author Alexander Cooper
     */
    public int getFullMoves() {
        return this.fullMoves;
    }

    /**
     * Returns whether the king is in check
     *
     * @return whether the king is in check
     * @author Logan Sandlin
     */
    public boolean isKingInCheck() {
        King currentKing = null; // there should always be an instance of
        // each king on the board so this should
        // never stay null
        final Collection<Piece> opponents = new ArrayList<>(5);
        final Optional<Player> possiblePlayer = this.getActivePlayer();
        if (possiblePlayer.isPresent()) {
            final ChessColor color = possiblePlayer.get().getChessColor();
            for (final Piece piece : this.activePieces) {
                if (piece.getChessColor() == color) {
                    if (piece instanceof King) {
                        currentKing = (King) piece;
                    }
                } else {
                    opponents.add(piece);
                }
            }
        }
        for (final Piece opp : opponents) {
            if (currentKing != null && opp.getPotentialThreats(this).contains(currentKing.getPosition())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the game mode
     * @return game mode
     * @author Kareem Khalidi
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }


}
