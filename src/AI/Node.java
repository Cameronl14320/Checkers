package AI;

import Movement.Move;

import java.util.Set;

public class Node {
    Set<Move> validMoves;
    Move move;

    public Node(Set<Move> validMoves, Move move) {
        this.validMoves = validMoves;
        this.move = move;
    }
}
