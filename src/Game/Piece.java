package Game;

import java.awt.*;

public class Piece {

    public static Color PLAYER_ONE_INNER = new Color(116, 38, 38);
    public static Color PLAYER_ONE_OUTER = new Color(165, 39, 39);
    public static Color PLAYER_TWO_INNER = new Color(172, 166, 139);
    public static Color PLAYER_TWO_OUTER = new Color(210, 202, 178);

    private boolean promoted = false;
    private final int player; // 1 for BLACK, 0 for WHITE
    private Position position;

    public Piece(int player, Position position) {
        this.player = player;
        this.position = position;
    }

    public boolean isBlack() {
        return (player == 1);
    }

    public void paint(Graphics g, int rectSize) {
        // Outer Circle
        int border = rectSize/8;
        int ovalSize = rectSize - border;
        if (isBlack()) {
            g.setColor(PLAYER_ONE_OUTER);
        } else {
            g.setColor(PLAYER_TWO_OUTER);
        }
        g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize), ovalSize, ovalSize);

        // Inner Circle
        border = rectSize/4;
        ovalSize = rectSize - border;
        if (isBlack()) {
            g.setColor(PLAYER_ONE_INNER);
        } else {
            g.setColor(PLAYER_TWO_INNER);
        }
        g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize), ovalSize, ovalSize);
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
