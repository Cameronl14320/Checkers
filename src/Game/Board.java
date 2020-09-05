package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Move;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {

    private static Position[][] positions;
    private Map<Position, Piece> pieces;
    private final int size;
    private final int rowsOfPieces;

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
                    if (row < rowsOfPieces) {
                        pieces.put(positions[row][col], new Piece(0, positions[row][col]));
                    }

                    if (row >= (size - rowsOfPieces)) {
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

    public void removePositionHighlights() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                positions[row][col].setHighlightTile(false);
            }
        }
    }

    public void highlightMovable(int player) {
        for (Position pos : pieces.keySet()) {
            Piece p = pieces.get(pos);
            if (p.matchingPlayer(player)) {
                ArrayList<Move> validMoves = getValidMoves(p, false);
                if (!validMoves.isEmpty()) {
                    p.setMovable(true);
                }
            }
        }
    }

    public void removePieceHighlights() {
        for (Position pos : pieces.keySet()) {
            Piece p = pieces.get(pos);
            p.setMovable(false);
            p.setSelected(false);
        }
    }

    public boolean canAnyJump(int player) {
        for (Position pos : pieces.keySet()) {
            Piece p = pieces.get(pos);
            if (p.matchingPlayer(player)) {
                if (canJump(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canJump(Piece piece) {
        ArrayList<Move> validMoves = getValidMoves(piece, true);
        for (Move m : validMoves) {
            if (m.getClass().equals(Jump.class)) {
                return true;
            }
        }
        return false;
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

        ArrayList<Move> validMoves = getValidMoves(piece, mustJump);

        for (Move m : validMoves) {
            Position p = m.getNextPosition();
            p.setHighlightTile(true);
        }
    }

    public ArrayList<Move> getValidMoves(Piece piece, boolean mustJump) {
        ArrayList<Move> validMoves = new ArrayList<>();
        Move nextMove = null;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position nextPosition = positions[row][col];
                ArrayList<Piece> takePieces = findTakePiece(piece, nextPosition);
                // Determine if able to take a piece
                if (!takePieces.isEmpty()) {
                    //nextPosition = findJumpPosition(piece, takePieces);
                    if (nextPosition != null) {
                        nextMove = new Jump(piece, takePieces, nextPosition, pieceAt(nextPosition));
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

        if (piece.getIsPromoted()) {
            boolean canJump = false;
            ArrayList<Move> removeMoves = new ArrayList<>();
            for (Move m : validMoves) {
                if (m.getClass().equals(Jump.class)) {
                    canJump = true;
                } else {
                    removeMoves.add(m);
                }
            }
            if (canJump) {
                for (Move m : removeMoves) {
                    validMoves.remove(m);
                }
            }
        }
        return validMoves;
    }

    // Jump Move
    public ArrayList<Piece> findTakePiece(Piece current, Position goalPosition) {
        ArrayList<Piece> takePieces = new ArrayList<>();
        Position currentPosition = current.getPosition();
        Position newPosition = currentPosition;
        int newRow;
        int newCol;

        if (!properMovement(currentPosition, goalPosition)) {
            return takePieces;
        }
        boolean expectPiece = true;
        while (newPosition != goalPosition) {
            if (newPosition.getRow() < goalPosition.getRow()) {
                newRow = newPosition.getRow() + 1;
            } else {
                newRow = newPosition.getRow() - 1;
            }
            if (newPosition.getCol() < goalPosition.getCol()) {
                newCol = newPosition.getCol() + 1;
            } else {
                newCol = newPosition.getCol() - 1;
            }

            if (!checkBounds(newRow, newCol)) {
                break;
            }

            newPosition = positions[newRow][newCol];
            if (expectPiece) {
                if (pieceAt(newPosition)) {
                    if (getPieceAt(newPosition).matchingPlayer(current.getPlayer())) {
                        break;
                    } else {
                        takePieces.add(getPieceAt(newPosition));
                    }
                }
                expectPiece = false;
            } else {
                if (pieceAt(newPosition)) {
                    break;
                }
                expectPiece = true;
            }
        }
        return takePieces;
    }

    public boolean properMovement(Position startPosition, Position goalPosition) {
        if (startPosition.getCol() == goalPosition.getCol()) {
            return false;
        }

        if (startPosition.getRow() == goalPosition.getRow()) {
            return false;
        }

        if (Math.abs(startPosition.getRow() - goalPosition.getRow()) != Math.abs(startPosition.getCol() - goalPosition.getCol())) {
            return false;
        }

        if (pieceAt(goalPosition)) {
            return false;
        }

        return true;
    }

    public boolean checkBounds(int row, int col) {
        if (row < 0 || col < 0) {
            return false;
        }

        if (row >= size - 1 || col >= size - 1) {
            return false;
        }

        return true;
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
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
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
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
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
                    boardString.append(pieces.get(positions[row][col]).toString());
                } else {
                    boardString.append("|_");
                }
            }
            boardString.append("|\n");
        }
        return boardString.toString();
    }
}
