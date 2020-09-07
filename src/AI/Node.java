package AI;

import Game.Game;
import Movement.Move;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node {
    Map<Move, Node> moveToChildMap;
    List<Move> validMoves;
    Move move;
    int wins;
    int n;


    public Node(List<Move> validMoves, Move move) {
        this.validMoves = validMoves;
        this.move = move;
        this.wins = 0;
        this.n = 0;
        this.moveToChildMap = new HashMap<>();
    }

    public void search(Game game, Game.gameStates targetState) {
    }

    public Move expSelectMove() {
        return null;
    }
}
