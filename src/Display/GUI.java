package Display;

import Game.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUI {

    private static Color BG_COLOR = new Color(0xDEDEDE);
    private static Color BUTTON_COLOR = new Color(0xC9C9C9);


    private JFrame gameFrame;
    private Game game;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton undoButton;
    private JButton newButton;
    private int currentPlayer;

    public GUI() {
        gameFrame = new JFrame("Checkers");
        gameFrame.setLayout(new BorderLayout());
        gameFrame.setMinimumSize(new Dimension(800, 800));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        buttonPanel = new JPanel(new FlowLayout());
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> gameUndo());
        newButton = new JButton("New Game");
        newButton.addActionListener(e -> createGame());

        buttonPanel.add(newButton);
        buttonPanel.add(undoButton);
        buttonPanel.setBackground(BG_COLOR);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.setBorder(new EmptyBorder(0, 50, 0 ,50));
        gameFrame.add(mainPanel);

        gameFrame.pack();
        gameFrame.setVisible(true);
    }

    private void createGame() {
        if (game != null) {
            mainPanel.remove(game);
        }
        game = new Game();
        game.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        game.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                game.convertMouse(e);
            }
        });
        game.setBackground(BG_COLOR);
        game.setVisible(true);
        mainPanel.add(game, BorderLayout.CENTER);
        gameFrame.pack();
    }

    private void gameUndo() {
        if (game == null) {
            return;
        }
        game.undoMove();
    }


}
