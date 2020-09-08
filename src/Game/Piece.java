package Game;

import java.awt.*;
import java.util.Objects;

public class Piece {

    private boolean isPromoted = false;
    private boolean isSelected = false;
    private boolean isMovable = false;

    private final int player; // 1 for BLACK, 0 for WHITE

    public Piece(int player) {
        this.player = player;
    }

    public boolean isBlack() {
        return (player == 1);
    }

    public int getPlayer() {
        return this.player;
    }

    public boolean matchingPlayer(int player) {
        return player == this.player;
    }

    public void setPromoted(boolean state) {
        if (state) {
            isPromoted = true;
        } else {
            isPromoted = false;
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            isSelected = true;
        } else {
            isSelected = false;
        }
    }

    public void setMovable(boolean selected) {
        if (selected) {
            isMovable = true;
        } else {
            isMovable = false;
        }
    }

    public boolean isPromoted() {
        return this.isPromoted;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public boolean isMovable() {
        return this.isMovable;
    }


    @Override
    public String toString() {
        if (isBlack()) {
            return "|B";
        } else {
            return "|W";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return isPromoted == piece.isPromoted &&
                isSelected == piece.isSelected &&
                isMovable == piece.isMovable &&
                player == piece.player;
    }

}
