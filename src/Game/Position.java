package Game;

import Game.Piece;

import java.awt.*;

public class Position {

    private final int color; // 1 for BLACK, 0 for WHITE
    private boolean isHighlighted = false;
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

    public boolean isHighlighted() {
        return this.isHighlighted;
    }

    public void setHighlightTile(boolean isHighlight) {
        isHighlighted = isHighlight;
    }

    @Override
    public String toString() {
        if (isBlack()) {
            return "|B";
        } else {
            return "|W";
        }
    }
}
