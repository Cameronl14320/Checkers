package Movement;

import Game.Board;
import Game.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {

    List<Action> actions;

    public Move(List<Action> actions) {
        this.actions = new ArrayList<>();
        for (Action a : actions) {
            this.actions.add(a);
        }
    }

    public void addAction(Action a) {
        this.actions.add(a);
    }

    public boolean apply(Board board) {
        for (Action a : actions) {
            a.apply(board);
        }
        return true;
    }

    public boolean undo(Board board) {
        ArrayList<Action> tempActions = new ArrayList<>();
        for (Action a : actions) {
            tempActions.add(a);
        }
        Collections.reverse(tempActions);
        for (Action a : tempActions) {
            a.undo(board);
        }
        return true;
    }

    public boolean equals(Move m) {
        if (m.actions.size() != this.actions.size()) {
            return false;
        }
        for (int n = 0; n < this.actions.size(); n++) {
            if (!this.actions.get(n).equals(m.actions.get(n))) {
                return false;
            }
        }
        return true;
    }

    public List<Position> getNextPositions() {
        ArrayList<Position> positions = new ArrayList<>();
        for (Action a : actions) {
            positions.add(a.nextPosition());
        }
        return positions;
    }
}
