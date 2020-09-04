package Movement;

import Game.Board;
import Game.Position;

public interface Move {

    boolean isValid();

    boolean apply(Board board);

    void undo(Board board);

    Position getNextPosition();

}
