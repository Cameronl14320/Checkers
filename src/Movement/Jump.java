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
    private final Position nextPosition;


    public Jump(Piece piece, Piece take, Position currentPosition, Position nextPosition) {
        this.piece = piece;
        this.take = take;
        this.currentPosition = currentPosition;
        this.nextPosition = nextPosition;
    }

    @Override
    public boolean isValid() {
        if (piece.matchingPlayer(take.getPlayer())) {
            return false;
        }


        return true;
    }

    @Override
    public boolean apply(Board board) {
        if (!isValid()) {
            return false;
        }

        return true;
    }

    @Override
    public void undo(Board board) {

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
