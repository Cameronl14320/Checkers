package Game;

import Game.Piece;

public class Position {

    private final int color; // 1 for BLACK, 0 for WHITE
    private final Piece piece;

    private int row;
    private int col;

    public Position(int row, int col, int color, Piece piece) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.piece = piece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isBlack() {
        return (color == 1);
    }

    @Override
    public String toString() {
        if (color == 1) {
            return "|B";
        } else {
            return "|W";
        }
    }
}
