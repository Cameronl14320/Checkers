package Display;

import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUI {

    private JFrame gameFrame;
    private Game game;

    public GUI() {
        gameFrame = new JFrame("Checkers");
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setMinimumSize(new Dimension(800, 800));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("hello");
        game = new Game();
        System.out.println("hello");
        game.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        game.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                game.convertMouse(e);
            }
        });
        System.out.println("hello");
        game.setVisible(true);
        gameFrame.add(game);

        gameFrame.pack();
        gameFrame.setVisible(true);
    }
}
