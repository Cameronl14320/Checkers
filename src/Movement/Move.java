package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public interface Move {

    public boolean isValid();

    public Board apply(Board board, Piece piece, Position nextPosition);
}
