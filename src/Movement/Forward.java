package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public class Forward implements Action {

    private final Piece piece;
    private final Position currentPosition;
    private final Position nextPosition;
    private final boolean pieceAt;
    private final boolean previousPromotionStatus;


    public Forward(Piece piece, Position currentPosition, Position nextPosition, boolean pieceAt) {
        this.piece = piece;
        this.currentPosition = currentPosition;
        this.nextPosition = nextPosition;
        this.pieceAt = pieceAt;
        this.previousPromotionStatus = piece.isPromoted();
    }

    @Override
    public boolean isValid() {

        if (piece == null) {
            return false;
        }

        if (currentPosition.getRow() == nextPosition.getRow()) {
            return false;
        }

        if (currentPosition.getCol() == nextPosition.getCol()) {
            return false;
        }

        if (!piece.isPromoted()) {
            if (piece.isBlack()) {
                if (nextPosition.getRow() - currentPosition.getRow() > 0) {
                    return false;
                }
            } else {
                if (nextPosition.getRow() - currentPosition.getRow() < 0) {
                    return false;
                }
            }
        }

        if (nextPosition.equals(currentPosition)) {
            return false;
        }

        if (Math.abs(currentPosition.getRow() - nextPosition.getRow()) > 1) {
            return false;
        }

        if (Math.abs(currentPosition.getCol() - nextPosition.getCol()) > 1) {
            return false;
        }

        return true;
    }

    @Override
    public boolean apply(Board board) {
        if (!isValid()) {
            return false;
        }
        board.removePiece(piece, currentPosition);
        board.addPiece(piece, nextPosition);
        return true;
    }

    @Override
    public void undo(Board board) {
        board.removePiece(piece, nextPosition);
        board.addPiece(piece, currentPosition);
        piece.setPromoted(previousPromotionStatus);
    }

    @Override
    public boolean equals(Action a) {
        if (!a.getClass().equals(Forward.class)) {
            return false;
        }
        Forward compare = (Forward) a;
        return (this.currentPosition == compare.currentPosition && this.nextPosition == compare.nextPosition &&
                this.piece == compare.piece);
    }

    @Override
    public Position nextPosition() {
        return this.nextPosition;
    }

    @Override
    public Piece getPiece() {
        return this.piece;
    }

    @Override
    public String toString() {
        return "Forward{" +
                "piece=" + piece +
                ", currentPosition=" + currentPosition +
                ", nextPosition=" + nextPosition +
                '}';
    }
}
