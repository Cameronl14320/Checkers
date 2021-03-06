package Game;

import Game.Piece;

import java.awt.*;
import java.util.Objects;

public class Position {

    private final int color; // 1 for BLACK, 0 for WHITE
    private boolean isHighlighted = false;

    private int row;
    private int col;

    public Position(int row, int col, int color) {
        this.row = row;
        this.col = col;
        this.color = color;
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
        return "[" + row + "]" + "[" + col + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return color == position.color &&
                row == position.row &&
                col == position.col;
    }
}
