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

    private boolean gameOver = false;

    private JFrame gameFrame;
    private Game game;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton undoButton;
    private JButton newButton;
    private JButton endButton;
    private Timer timer;
    private final int delay = 40; // 40ms repaint delay

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
        endButton = new JButton("End Jump Early");
        endButton.addActionListener(e -> endTurn());

        buttonPanel.add(newButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(endButton);
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
            timer.stop();
            game = null;
            timer = null;
        }
        game = new Game();
        gameOver = false;
        game.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        game.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                game.convertMouse(e);
            }
        });
        game.setBackground(BG_COLOR);
        game.setVisible(true);
        timer = new Timer(delay, e->updateGame());
        timer.start();

        mainPanel.add(game, BorderLayout.CENTER);
        gameFrame.pack();
    }

    private void gameUndo() {
        if (game == null) {
            JOptionPane.showMessageDialog(null, "Game hasn't started yet");
            return;
        }
        if (!game.undoMove()) {
            JOptionPane.showMessageDialog(null, "No more moves to undo");
        }
    }

    private void endTurn() {
        if (game == null) {
            JOptionPane.showMessageDialog(null, "Game hasn't started yet");
            return;
        }
        if (!game.endExtraJump()) {
            JOptionPane.showMessageDialog(null, "Not valid, can only end turn on extra jumps");
        }
    }

    private void updateGame() {
        if (game == null) {
            throw new Error("updateGame() running when invalid");
        }

        game.repaint();
    }
}
