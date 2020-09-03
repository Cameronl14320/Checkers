package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public class Jump implements Move {
    private Piece movingPiece;
    private Piece takePiece;
    private Position currentPosition;
    private Position nextPosition;

    private boolean pieceAtNext;


    public Jump(Piece piece, Piece takePiece, Position nextPosition, boolean pieceAtNext) {
        this.movingPiece = piece;
        this.takePiece = takePiece;
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
        // Can only move onto Dark tiles
        if (!nextPosition.isBlack()) {
            return false;
        }

        // Can only move forward two position
        if (Math.abs(nextCol - currentCol) != 2) {
            return false;
        }
        if (Math.abs(nextRow - currentRow) != 2) {
            return false;
        }

        if (movingPiece.getPlayer() == takePiece.getPlayer()) {
            return false;
        }

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
        movingPiece.setPosition(nextPosition);
        board.removePiece(currentPosition);
        board.removePiece(takePiece.getPosition());
        board.addCaptured(takePiece.getPlayer(), takePiece);
        board.addPiece(movingPiece, nextPosition);
    }

    @Override
    public void undo(Board board) {

    }
}
