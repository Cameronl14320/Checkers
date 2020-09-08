package Movement;

import Game.Board;
import Game.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public void undo(Board board) {
        ArrayList<Action> tempActions = new ArrayList<>(actions);
        Collections.reverse(tempActions);
        for (Action a : tempActions) {
            a.undo(board);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof Move) {
            return equals((Move)obj);
        }
        return false;
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

    public List<Action> getActions() {
        return this.actions;
    }

    public List<Position> getNextPositions() {
        ArrayList<Position> positions = new ArrayList<>();
        for (Action a : actions) {
            positions.add(a.nextPosition());
        }
        return positions;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Action a : actions) {
            string.append(a);
        }
        return string.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(actions);
    }
}
