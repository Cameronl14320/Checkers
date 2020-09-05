package Movement;

import Game.Board;
import Game.Piece;
import Game.Position;

import java.util.ArrayList;
import java.util.Collections;

public class MultiJump implements Move{
    ArrayList<Jump> jumps;

    public MultiJump(ArrayList<Jump> jumps) {
        this.jumps = new ArrayList<>();
        for (Jump j : jumps) {
            this.jumps.add(j);
        }
    }

    @Override
    public boolean isValid() {
        for (Jump j : jumps) {
            if (!j.isValid()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean apply(Board board) {
        if (!isValid()) {
            return false;
        }
        for (Jump j : jumps) {
            j.apply(board);
        }
        return true;
    }

    @Override
    public void undo(Board board) {
        Collections.reverse(jumps);
        for (Jump j : jumps) {
            j.undo(board);
        }
    }

    @Override
    public Position getNextPosition() {
        return null;
    }

    @Override
    public Piece getCurrentPiece() {
        return null;
    }
}
