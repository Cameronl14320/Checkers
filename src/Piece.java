public class Piece {

    private boolean promoted = false;
    private final int player;
    private Position position;

    public Piece(int player, Position position) {
        this.player = player;
        this.position = position;
    }

}
