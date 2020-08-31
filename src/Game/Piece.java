package Game;

public class Piece {

    private boolean promoted = false;
    private final int player;
    private Position position;

    public Piece(int player, Position position) {
        this.player = player;
        this.position = position;
    }

    @Override
    public String toString() {
        if (player == 0) {
            return "|B";
        } else if (player == 1) {
            return "|W";
        } else {
            return "|_";
        }
    }

}
