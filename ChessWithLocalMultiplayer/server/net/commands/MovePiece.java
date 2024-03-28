package server.net.commands;

import base.Command;
import base.Communicator;
import base.Vector2;
import server.game.ServerGameHandler;


/**
 * Command for moving player piece
 *
 * @author John Gauci
 */

public class MovePiece extends Command {

    private final Vector2 startPosition;
    private final Vector2 endPosition;

    /**
     * Initializes a move piece command with the given start and end piece positions
     *
     * @param startPosition start position of piece
     * @param endPosition   end position of piece
     * @uauthor John Gauci
     */
    public MovePiece(final Vector2 startPosition, final Vector2 endPosition) {
        super();
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * Handles the moving of a player's piece
     *
     * @param communicator the ServerGameHandler to operate upon
     * @author John Gauci
     * @author Logan Sandlin
     */
    @Override
    public void execute(final Communicator communicator) {
        final var serverGameHandler = (ServerGameHandler) communicator;
        final var possibleServerPiece = serverGameHandler.getState().getPiece(this.startPosition);
        if (possibleServerPiece.isPresent() &&
                possibleServerPiece.get().getValidMoves(serverGameHandler.getState()).contains(this.endPosition)) {
            possibleServerPiece.get().move(serverGameHandler.getState(), this.endPosition);
            serverGameHandler.getState().cycleTurn();
        }
    }


}
