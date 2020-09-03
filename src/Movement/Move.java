package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

public interface Move {

    public boolean isValid();

    public void apply(Board board);

    public void undo(Board board);

}
