package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Move;

import java.awt.*;
import java.util.*;

public class Board {

    private static Position[][] positions;
    private Map<Position, Piece> pieces;
    private int size;
    private int rowsOfPieces;

    private Set<Piece> capturedBlack;
    private Set<Piece> capturedWhite;

    private ArrayList<Position> validPositions;

    public Board(int size, int rowsOfPieces) {
        this.size = size;
        this.rowsOfPieces = rowsOfPieces;
        this.capturedBlack = new HashSet<>();
        this.capturedWhite = new HashSet<>();

        validPositions = new ArrayList<>();
        pieces = new HashMap<>();
        positions = makePositions(size, rowsOfPieces);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (positions[row][col].isBlack()) {
                    if (row >= 0 && row < rowsOfPieces) {
                        pieces.put(positions[row][col], new Piece(0, positions[row][col]));
                    }

                    if (row >= (size - rowsOfPieces) && row < size) {
                        pieces.put(positions[row][col], new Piece(1, positions[row][col]));
                    }
                }
            }
        }
    }

    public void addPiece(Piece piece, Position position) {
        pieces.put(position, piece);
    }

    public void removePiece(Position position) {
        pieces.remove(position);
    }

    public void addCaptured(int player, Piece piece) {
        if (player == 1) {
            capturedBlack.add(piece);
        } else {
            capturedWhite.add(piece);
        }
    }

    public void removeCaptured(int player, Piece piece) {
        if (player == 1) {
            capturedBlack.remove(piece);
        } else {
            capturedWhite.remove(piece);
        }
    }

    public void removeHighlights() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                positions[row][col].setHighlightTile(false);
            }
        }
    }

    public void checkPromoted(Piece piece) {
        if (piece.getPlayer() == 1) {
            if (piece.getPosition().getRow() == 0) {
                piece.setPromoted(true);
            }
        } else {
            if (piece.getPosition().getRow() == size - 1) {
                piece.setPromoted(true);
            }
        }
    }

    public void getValidPositions(Piece piece, boolean mustJump) {
        for (Position p : validPositions) {
            p.setHighlightTile(false);
        }
        validPositions = new ArrayList<>();
        Move nextMove = null;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position nextPosition = positions[row][col];
                Piece takePiece = pieces.get(positions[row][col]);
                // Determine if able to take a piece
                if (takePiece != null) {
                    nextPosition = findJumpPosition(piece, takePiece);
                    if (nextPosition != null) {
                        nextMove = new Jump(piece, takePiece, nextPosition, pieceAt(nextPosition));
                    }
                    // Determine if standard movement available
                } else {
                    nextMove = new Forward(piece, nextPosition, pieceAt(nextPosition));
                }
                // Add it to available moves
                if (nextMove != null && nextMove.isValid()) {
                    if (mustJump) {
                        if (nextMove.getClass().equals(Jump.class)) {
                            validPositions.add(nextPosition);
                        }
                    } else {
                        validPositions.add(nextPosition);
                    }
                    nextMove = null;
                }
            }
        }

        for (Position p: validPositions) {
            p.setHighlightTile(true);
        }
    }

    public ArrayList<Move> getValidMoves(Piece piece, boolean mustJump) {
        ArrayList<Move> validMoves = new ArrayList<>();
        Move nextMove = null;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position nextPosition = positions[row][col];
                Piece takePiece = pieces.get(positions[row][col]);
                // Determine if able to take a piece
                if (takePiece != null) {
                    nextPosition = findJumpPosition(piece, takePiece);
                    if (nextPosition != null) {
                        nextMove = new Jump(piece, takePiece, nextPosition, pieceAt(nextPosition));
                    }
                    // Determine if standard movement available
                } else {
                    nextMove = new Forward(piece, nextPosition, pieceAt(nextPosition));
                }
                // Add it to available moves
                if (nextMove != null && nextMove.isValid()) {
                    if (mustJump) {
                        if (nextMove.getClass().equals(Jump.class)) {
                            validMoves.add(nextMove);
                        }
                    } else {
                        validMoves.add(nextMove);
                    }
                    nextMove = null;
                }
            }
        }
        return validMoves;
    }

    // Jump Move
    public Piece findTakePiece(Piece current, Position nextPosition) {
        Position currentPosition = current.getPosition();
        int newRow = 0;
        int newCol = 0;

        // Determine Column after Taken Piece
        if (currentPosition.getCol() < nextPosition.getCol()) {
            newCol = nextPosition.getCol() - 1;
        } else {
            newCol = nextPosition.getCol() + 1;
        }

        // Determine Row after Taken Piece
        if (currentPosition.getRow() < nextPosition.getRow()) {
            newRow = nextPosition.getRow() - 1;
        } else {
            newRow = nextPosition.getRow() + 1;
        }

        if (newRow >= size - 1 || newCol >= size) {
            return null;
        }
        if (newRow < 0 || newCol < 0) {
            return null;
        }

        // Return if valid position
        if (pieces.get(positions[newRow][newCol]) != null && pieces.get(positions[newRow][newCol]) != current) {
            return pieces.get(positions[newRow][newCol]);
        }
        return null;
    }

    public Position findJumpPosition(Piece current, Piece take) {
        Position currentPosition = current.getPosition();
        Position takePosition = take.getPosition();
        int newRow = 0;
        int newCol = 0;

        // Determine Column after Taken Piece
        if (currentPosition.getCol() < takePosition.getCol()) {
            newCol = takePosition.getCol() + 1;
        } else {
            newCol = takePosition.getCol() - 1;
        }
        // Determine Row after Taken Piece
        if (currentPosition.getRow() < takePosition.getRow()) {
            newRow = takePosition.getRow() + 1;
        } else {
            newRow = takePosition.getRow() - 1;
        }

        if (newRow >= size || newCol >= size) {
            return null;
        }
        if (newRow < 0 || newCol < 0) {
            return null;
        }

        // Return if valid position
        if (positions[newRow][newCol] != null) {
            return positions[newRow][newCol];
        }
        return null;
    }

    // Getters

    public boolean pieceAt(int row, int col) {
        if (row >= size || col >= size) {
            return false;
        }

        if (row < 0 || col < 0) {
            return false;
        }
        return (pieces.get(positions[row][col]) != null);
    }

    public boolean pieceAt(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return (pieceAt(row, col));
    }

    public Piece getPieceAt(int row, int col) {
        if (pieceAt(row, col)) {
            return pieces.get(positions[row][col]);
        }
        return null;
    }

    public Piece getPieceAt(Position position) {
        if (pieceAt(position)) {
            int row = position.getRow();
            int col = position.getCol();
            return pieces.get(positions[row][col]);
        }
        return null;
    }

    public Position getPositionAt(int row, int col) {
        if (positions[row][col] != null) {
            return positions[row][col];
        } else {
            return null;
        }
    }

    /**
     * Intialise the Checkers Board through a Positions 2d Array
     * Remains constant throughout every Board object
     * @param size
     * @param rowsOfPieces
     * @return
     */
    private static Position[][] makePositions(int size, int rowsOfPieces) {
        Position[][] positions = new Position[size][size];
        boolean stagger = true;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int color;
                if (stagger) {
                    if ((col + 1) % 2 == 0) {
                        color = 1;
                    } else {
                        color = 0;
                    }
                } else {
                    if ((col + 1)% 2 == 0) {
                        color = 0;
                    } else {
                        color = 1;
                    }
                }
                positions[row][col] = new Position(row, col, color, null);
            }
            if (stagger) {
                stagger = false;
            } else {
                stagger = true;
            }
        }

        if (rowsOfPieces*2 > size) {
            throw new Error("Invalid number full rows for pieces");
        }
        return positions;
    }

    public void paint(Graphics g, int rectSize) {
        for (int row = 0; row < positions.length; row++) {
            for (int col = 0; col < positions[0].length; col++) {
                positions[row][col].paint(g, rectSize);
            }
        }

        // Draw Board Pieces
        for (Position pos : pieces.keySet()) {
            pieces.get(pos).paint(g, rectSize);
        }
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < positions.length; row++) {
            for (int col = 0; col < positions[0].length; col++) {
                boardString.append(positions[row][col].toString());
            }
            boardString.append("|\n");
        }
        boardString.append("\n");

        for (Position pos : pieces.keySet()) {
            boardString.append(pieces.get(pos));
        }

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (pieces.get(positions[row][col]) != null) {
                    boardString.append(pieces.get(positions[row][col].toString()));
                } else {
                    boardString.append("|_");
                }
            }
            boardString.append("|\n");
        }
        return boardString.toString();
    }
}
