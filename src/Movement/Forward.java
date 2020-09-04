package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public class Forward implements Move {
    private final Piece movingPiece;
    private final Position currentPosition;
    private final Position nextPosition;

    private final boolean pieceAtNext;


    public Forward(Piece piece, Position nextPosition, boolean pieceAtNext) {
        this.movingPiece = piece;
        this.currentPosition = piece.getPosition();
        this.nextPosition = nextPosition;
        this.pieceAtNext = pieceAtNext;
    }

    @Override
    public boolean isValid() {
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();
        int nextRow = nextPosition.getRow();
        int nextCol = nextPosition.getCol();

        // Can't move on to same position
        if (currentPosition.equals(nextPosition)) {
            return false;
        }
        // Only allow forward movement and one place movement unless promoted
        if (!movingPiece.getIsPromoted()) {
            if (movingPiece.isBlack()) {
                if (nextRow > currentPosition.getRow()) {
                    return false;
                }
            } else {
                if (nextRow < currentPosition.getRow()) {
                    return false;
                }
            }
        }

        // Can only move forward one position
        if (Math.abs(nextCol - currentCol) != 1) {
            return false;
        }
        // Can only move forward
        if (Math.abs(nextRow - currentRow) != 1) {
            return false;
        }

        // As diagonal Movement, dy/dy = 1
        if (Math.abs(nextRow - currentRow) != Math.abs(nextCol - currentCol)) {
            return false;
        }

        // Cannot move directly forward
        if (nextCol - currentPosition.getCol() == 0) {
            return false;
        }
        // Cannot move directly to the side
        if (nextRow - currentPosition.getRow() == 0) {
            return false;
        }

        // Can only move onto Dark tiles
        if (!nextPosition.isBlack()) {
            return false;
        }
        // There is currently a piece at the given location
        if (pieceAtNext) {
            return false;
        }

        return true;
    }

    @Override
    public boolean apply(Board board) {
        if (!isValid()) {
            return false;
        }
        movingPiece.setPosition(nextPosition);
        board.removePiece(currentPosition);
        board.addPiece(movingPiece, nextPosition);
        return true;
    }

    @Override
    public void undo(Board board) {
        movingPiece.setPosition(currentPosition);
        board.removePiece(nextPosition);
        board.addPiece(movingPiece, currentPosition);
    }

    public Position getNextPosition() {
        return this.nextPosition;
    }
}
