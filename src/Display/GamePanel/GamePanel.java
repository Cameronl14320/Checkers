package Display.GamePanel;

import Display.GamePanel.Modes.GamePanelMode;
import Game.Game;

import javax.swing.*;

public class GamePanel extends JPanel {
    private GamePanelMode currentMode = GamePanelMode.NONE;
    Game game;


    GamePanel(Game game) {
        this.game = game;
    }

    public void handleClick() {
        this.currentMode = this.currentMode.handleClick();
    }

    @Override
    public void repaint() {
        super.repaint();
        int size = 8; // game.size();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Draw some rectangles

            }
        }

        /*
        this.currentMode.repaint(this);

        if (gamePanelMode == NONE) {
            for (Piece piece : game.getPieces()) {
                // draw at piece.x(), piece.y();
                if (piece at highlighted position){

                }
                // piece.color()
            }
        }

        piece.x = 16;

        game.push() {
            ...
            p
        }

         */
    }
}
