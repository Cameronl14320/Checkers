package Game;

import Game.Piece;

import java.awt.*;

public class Position {

    public static Color BLACK_TILE = new Color(0, 0, 0);
    public static Color WHITE_TILE = new Color(255, 255, 255);
    public static Color HIGHLIGHT_TILE = new Color(39, 142, 187);

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

    public void paint(Graphics g, int rectSize) {
        if (isBlack()) {
            g.setColor(BLACK_TILE);
        } else {
            g.setColor(WHITE_TILE);
        }
        if (isHighlighted) {
            g.setColor(HIGHLIGHT_TILE);
        }
        g.fillRect(col * rectSize, row * rectSize, rectSize, rectSize);
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
