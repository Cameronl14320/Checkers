package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public class Forward implements Action {

    private final Piece piece;
    private final Position currentPosition;
    private final Position nextPosition;
    private final Direction direction;


    public Forward(Piece piece, Position currentPosition, Position nextPosition, Direction direction) {
        this.piece = piece;
        this.currentPosition = currentPosition;
        this.nextPosition = nextPosition;
        this.direction = direction;
    }

    @Override
    public boolean isValid() {
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
        if (!a.getClass().equals(Forward.class)) {
            return false;
        }
        Forward compare = (Forward) a;

        return (this.currentPosition == compare.currentPosition && this.nextPosition == compare.nextPosition &&
                this.piece == compare.piece && this.direction == compare.direction);
    }

}
