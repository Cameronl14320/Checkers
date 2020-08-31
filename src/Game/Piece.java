package Game;

import java.awt.*;

public class Piece {

    public static Color PLAYER_ONE_INNER = new Color(116, 38, 38);
    public static Color PLAYER_ONE_OUTER = new Color(165, 39, 39);
    public static Color PLAYER_TWO_INNER = new Color(172, 166, 139);
    public static Color PLAYER_TWO_OUTER = new Color(210, 202, 178);
    public static Color SELECT_HIGHLIGHT = new Color(206, 187, 45);

    private boolean isPromoted = false;
    private boolean isHighlighted = false;

    private final int player; // 1 for BLACK, 0 for WHITE
    private Position position;


    public Piece(int player, Position position) {
        this.player = player;
        this.position = position;
    }

    public boolean isBlack() {
        return (player == 1);
    }

    public void setHighlight(boolean selected) {
        if (selected) {
            isHighlighted = true;
        } else {
            isHighlighted = false;
        }
    }

    public void paint(Graphics g, int rectSize) {
        // Highlight
        int border = rectSize/16;
        if (isHighlighted) {
            g.setColor(SELECT_HIGHLIGHT);
            g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize),
                    rectSize - border, rectSize - border);
        }

        // Outer Circle
        border = rectSize/8;
        if (isBlack()) {
            g.setColor(PLAYER_ONE_OUTER);
        } else {
            g.setColor(PLAYER_TWO_OUTER);
        }
        g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize),
                rectSize - border, rectSize - border);

        // Inner Circle
        border = rectSize/4;
        if (isBlack()) {
            g.setColor(PLAYER_ONE_INNER);
        } else {
            g.setColor(PLAYER_TWO_INNER);
        }
        g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize),
                rectSize - border, rectSize - border);
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
