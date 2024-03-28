package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.pieces.Bishop;
import game.pieces.ChessColor;
import game.pieces.King;
import game.pieces.Knight;
import game.pieces.Pawn;
import game.pieces.Piece;
import game.pieces.Queen;
import game.pieces.Rook;
import server.game.GameState;
import server.game.gamemodes.GameMode;

/**
 * Contains utility functions for interacting with the save system.
 *
 * @author Alexander Cooper
 */
public class GameSaveSystem {

    // change this path to one of the test fens before starting a new game to boot up test positions.
    private final static String newGamePath = "games/new_game.fen";

    /**
     * Loads a game from a save file.
     *
     * @param saveSlot the slot number to load from
     * @param gameMode the game mode
     * @return the loaded game
     * @author Alexander Cooper
     */
    public static GameState loadFromSaveFile(final int saveSlot, final GameMode gameMode) {
        final String slot = saveSlot < 10 ? "0" + saveSlot : String.valueOf(saveSlot);
        final File file = new File("games/save_" + slot + ".fen");
        try (final Scanner fileReader = new Scanner(file)) {
            if (fileReader.hasNextLine())
        	    return GameSaveSystem.loadFromFenString(fileReader.nextLine(), gameMode);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return GameSaveSystem.getNewGame(gameMode);
    }

    /**
     * Gets the game state of a new game.
     *
     * @param gameMode the game mode.
     * @return the new game.
     * @author Alexander Cooper
     */
    public static GameState getNewGame(GameMode gameMode) {
        final File file = new File(GameSaveSystem.newGamePath);
        try (final Scanner fileReader = new Scanner(file)) {
            if (fileReader.hasNextLine())
        	    return GameSaveSystem.loadFromFenString(fileReader.nextLine(), gameMode);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Writes a FEN string to a save file.
     *
     * @param saveSlot  the save slot to write to
     * @param gameState the game state to save
     * @author Alexander Cooper
     */
    public static void saveToFile(final int saveSlot, final GameState gameState) {
        final String slot = saveSlot < 10 ? "0" + saveSlot : String.valueOf(saveSlot);
        try (final FileWriter myWriter = new FileWriter("games/save_" + slot + ".fen")) {
            myWriter.write(GameSaveSystem.saveToFenString(gameState));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of all slots that contain a file that contains a saved game.
     *
     * @return list of all slots that contain a saved game
     * @author Alexander Cooper
     */
    public static List<Integer> getSavedGameSlots() {
        final List<Integer> slots = new ArrayList<>(10);
        final File gamesDir = new File("games");
        for (final File file : gamesDir.listFiles()) {
            if (file.isFile()) {
                final String name = file.getName();
                if (name.startsWith("save")) {
                    try (final Scanner fileReader = new Scanner(file)) {
                        if (fileReader.hasNextLine())
                            slots.add(Integer.parseInt(name.split("_")[1].replace(".fen", "")));
                    } catch (final FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return slots;
    }

    /**
     * Gets a list of all available slots that can be saved to.
     *
     * @return list of available slots to save to
     * @author Alexander Cooper
     */
    public static List<Integer> getAvailableGameSlots() {
        final var slots = new ArrayList<Integer>(10);
        final File gamesDir = new File("games");
        for (final File file : gamesDir.listFiles()) {
            if (file.isFile()) {
                final String name = file.getName();
                if (name.startsWith("save"))
                    slots.add(Integer.parseInt(name.split("_")[1]));
            }
        }
        return slots;
    }
    
    /**
     * Loads a game from a fen string.
     * 
     * @param fenString the fen string.
     * @param gameMode the game mode.
     * @return the game.
     * @author Alexander Cooper
     */
    private static GameState loadFromFenString(final String fenString, GameMode gameMode) {
        final String[] fields = fenString.split(" ");
        final String[] ranks = fields[0].split("/");
        final String turn = fields[1];
        final String castlingRightsString = fields[2];
        final String possibleEnPassantTargetString = fields[3];
        final int halfMoves = Integer.parseInt(fields[4]);
        final int fullMoves = Integer.parseInt(fields[5]);
        
        final List<Piece> pieces = new ArrayList<>();
        
        int row = 7;
        int col = 0;
        for (final String rank : ranks) {
            for (int i = 0; i < rank.length(); i++) {
                char c = rank.charAt(i);
                if (Character.isDigit(c)) {
                    col += Integer.parseInt(String.valueOf(c));
                } else if (Character.isLetter(c)) {
                    final ChessColor color = Character.isLowerCase(c) ? ChessColor.BLACK : ChessColor.WHITE;
                    c = Character.toLowerCase(c);
                    switch (c) {
                        case 'p' -> {
                            final var piece = Pawn.newPawn(row, col, color);
                            if ((color == ChessColor.WHITE && row != 1) || (color == ChessColor.BLACK && row != 6))
                                piece.setMoved();
                            pieces.add(piece);
                        }
                        case 'r' -> pieces.add(Rook.newRook(row, col, color));
                        case 'n' -> pieces.add(Knight.newKnight(row, col, color));
                        case 'b' -> pieces.add(Bishop.newBishop(row, col, color));
                        case 'q' -> pieces.add(Queen.newQueen(row, col, color));
                        case 'k' -> pieces.add(King.newKing(row, col, color));
                    }
                    col++;
                }
                if (col == 8) {
                    row--;
                    col = 0;
                }
            }
        }

        final ChessColor turnColor;
        if ("w".equals(turn))
            turnColor = ChessColor.WHITE;
        else
            turnColor = ChessColor.BLACK;

        boolean[] castlingRights = new boolean[]{false, false, false, false};
        for (int i = 0; i < castlingRightsString.length(); i++) {
            switch (castlingRightsString.charAt(i)) {
                case 'K' -> castlingRights[0] = true;
                case 'Q' -> castlingRights[1] = true;
                case 'k' -> castlingRights[2] = true;
                case 'q' -> castlingRights[3] = true;
            }
        }

        final Vector2 enPassantTarget;
        if (!"-".equals(possibleEnPassantTargetString))
            enPassantTarget = Vector2.positionStringToVector(possibleEnPassantTargetString);
        else
            enPassantTarget = null;
        
        return new GameState(pieces, turnColor, castlingRights, enPassantTarget, halfMoves, fullMoves, gameMode);
    }

    /**
     * Saves the state of the game into a fen string.
     *
     * @param gameState the game state.
     * @return the fen string.
     * @author Alexander Cooper
     */
    private static String saveToFenString(GameState gameState) {
        final var boardPieces = new Piece[8][8];
        for (final var piece : gameState.getActivePieces())
            boardPieces[piece.getBoardRow()][piece.getBoardColumn()] = piece;

        final StringBuilder fen = new StringBuilder(100);
        for (int rank = 7; rank >= 0; rank--) {
            int emptySpaces = 0;
            for (int col = 0; col < 8; col++) {
                final var piece = boardPieces[rank][col];
                if (piece == null) {
                    emptySpaces++;
                } else {
                    if (emptySpaces > 0) {
                        fen.append(emptySpaces);
                        emptySpaces = 0;
                    }
                    fen.append(piece.getPieceLetter());
                }
            }
            if (emptySpaces > 0)
                fen.append(emptySpaces);
            if (rank > 0)
                fen.append('/');
        }

        fen.append(' ');
        if (gameState.getActivePlayer().isPresent()) {
            switch (gameState.getActivePlayer().get().getChessColor()) {
                case WHITE -> fen.append('w');
                case BLACK -> fen.append('b');
            }
        }

        fen.append(' ');
        fen.append(gameState.getCastlingRightsString());

        fen.append(' ');
        if (gameState.getEnPassantTarget().isPresent())
            fen.append(gameState.getEnPassantTarget().get().vectorToPositionString());
        else
            fen.append('-');

        fen.append(' ');
        fen.append(gameState.getHalfMoves());

        fen.append(' ');
        fen.append(gameState.getFullMoves());

        return fen.toString();
    }
}
