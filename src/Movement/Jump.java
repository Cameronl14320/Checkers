package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

import java.util.ArrayList;
import java.util.List;

public class Jump implements Move {
    private final Piece movingPiece;
    private final ArrayList<Piece> takePieces;
    private final Position currentPosition;
    private final Position nextPosition;

    private final boolean pieceAtNext;


    public Jump(Piece piece, List<Piece> takePieces, Position nextPosition, boolean pieceAtNext) {
        this.movingPiece = piece;
        this.takePieces = new ArrayList<>();
        this.currentPosition = piece.getPosition();
        this.nextPosition = nextPosition;
        this.pieceAtNext = pieceAtNext;

        for (Piece p : takePieces) {
            this.takePieces.add(p);
        }
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

        // Can only move forward two position
        if (Math.abs(nextCol - currentCol) != 2*takePieces.size()) {
            return false;
        }
        if (Math.abs(nextRow - currentRow) != 2*takePieces.size()) {
            return false;
        }

        for (Piece p : takePieces) {
            if (movingPiece.getPlayer() == p.getPlayer()) {
                return false;
            }
        }


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
        for (Piece p : takePieces) {
            board.removePiece(p.getPosition());
            board.addCaptured(p.getPlayer(), p);
        }
        board.addPiece(movingPiece, nextPosition);
        return true;
    }

    @Override
    public void undo(Board board) {
        movingPiece.setPosition(currentPosition);
        board.removePiece(nextPosition);
        for (Piece p : takePieces) {
            board.addPiece(p, p.getPosition());
            board.removeCaptured(p.getPlayer(), p);
        }
        board.addPiece(movingPiece, currentPosition);
    }

    public Position getNextPosition() {
        return this.nextPosition;
    }
}
