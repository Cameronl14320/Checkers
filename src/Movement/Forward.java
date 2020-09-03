package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public class Forward implements Move {
    private Piece movingPiece;
    private Position currentPosition;
    private Position nextPosition;

    private boolean pieceAtNext;


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
        // Only allow forward movement unless promoted
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
        // Cannot move directly forward
        if (nextCol - currentPosition.getCol() == 0) {
            return false;
        }
        // Can only move forward one position
        if (Math.abs(nextCol - currentCol) != 1) {
            return false;
        }
        if (Math.abs(nextRow - currentRow) != 1) {
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
    public void apply(Board board) {
        if (!isValid()) {
            return;
        }
        return;
    }
}
