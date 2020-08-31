package Display;

import Game.*;

import javax.swing.*;
import java.awt.*;



public class GUI {

    private JFrame gameFrame;
    private Game game;

    private int rectSize = 40;

    public GUI() {
        gameFrame = new JFrame("Checkers");
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setMinimumSize(new Dimension(800, 800));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.getContentPane().add(new JLabel(), BorderLayout.CENTER);

        game = new Game();
        game.setMinimumSize(new Dimension(rectSize*8, rectSize*8));
        game.setVisible(true);
        gameFrame.add(game);

        gameFrame.pack();
        gameFrame.setVisible(true);
    }
}
