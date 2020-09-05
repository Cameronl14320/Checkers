package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public interface Move {

    boolean isValid();

    boolean apply(Board board);

    void undo(Board board);

    Position getNextPosition();

    Piece getCurrentPiece();

}
