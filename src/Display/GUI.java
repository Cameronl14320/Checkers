package Display;

import Game.Game;
import Game.Board;
import javafx.geometry.Pos;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private JFrame gameFrame;
    private JPanel displayPanel;
    private Graphics2D graphics;

    private Game game;

    private int rectSize = 20;

    public GUI() {
        game = new Game();
        gameFrame = new JFrame();
        displayPanel = new JPanel();
        graphics = (Graphics2D) displayPanel.getGraphics();
        gameFrame.setPreferredSize(new Dimension(800, 800));
    }

    public void draw() {
        Board board = game.getBoard();
        Position[][] positions = board.getPositions();
        Piece[][] pieces = board.getPieces();
        for (board.g)
    }
}
