package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public interface Action {

    enum Direction {
        TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    boolean isValid();

    boolean apply(Board board);

    void undo(Board board);

    boolean equals(Action a);
}
