package Movement;

import Game.Board;

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
}
