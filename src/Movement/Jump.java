package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

public class Jump implements Action {

    private final Piece piece;
    private final Piece take;
    private final Position currentPosition;
    private final Position takePosition;
    private final Position nextPosition;
    private final boolean pieceAt;
    private final boolean wasPromoted;


    public Jump(Piece piece, Piece take, Position currentPosition, Position takePosition, Position nextPosition, boolean pieceAt) {
        this.piece = piece;
        this.take = take;
        this.currentPosition = currentPosition;
        this.takePosition = takePosition;
        this.nextPosition = nextPosition;
        this.pieceAt = pieceAt;
        this.wasPromoted = piece.isPromoted();
    }

    @Override
    public boolean isValid() {

        if (currentPosition.getRow() == nextPosition.getRow()) {
            return false;
        }

        if (currentPosition.getCol() == nextPosition.getCol()) {
            return false;
        }

        if (piece == null || take == null) {
            return false;
        }

        if (piece.matchingPlayer(take.getPlayer())) {
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

        if (piece.equals(take)) {
            return false;
        }

        if (pieceAt) {
            return false;
        }

        if (nextPosition.equals(currentPosition)) {
            return false;
        }

        if (Math.abs(currentPosition.getRow() - nextPosition.getRow()) > 2) {
            return false;
        }

        if (Math.abs(currentPosition.getCol() - nextPosition.getCol()) > 2) {
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
        board.removePiece(take, takePosition);
        board.addPiece(piece, nextPosition);
        return true;
    }

    @Override
    public void undo(Board board) {
        board.removePiece(piece, nextPosition);
        board.addPiece(take, takePosition);
        board.addPiece(piece, currentPosition);
        piece.setPromoted(wasPromoted);
    }

    @Override
    public Position nextPosition() {
        return this.nextPosition;
    }

    @Override
    public boolean equals(Action a) {
        if (!a.getClass().equals(Jump.class)) {
            return false;
        }
        Jump compare = (Jump) a;
        return (this.currentPosition == compare.currentPosition && this.nextPosition == compare.nextPosition &&
                this.piece == compare.piece && this.take == compare.take);
    }




}
