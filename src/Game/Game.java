package Game;

import Display.GUI;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Game extends JPanel {

    private Board currentBoard;
    private Stack<Board> previousBoards;

    private int delay = 40; // 40ms repaint delay

    public Game() {
        int size = 8;
        int rowsOfPieces = 3;
        initBoard(size, rowsOfPieces);

        new Timer(delay, e->repaint()).start();
    }

    public void initBoard(int size, int rowsOfPieces) {
        this.currentBoard = new Board(size, rowsOfPieces); // Create an 8*8 board
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        Position[][] positions = currentBoard.getPositions();
        Piece[][] pieces = currentBoard.getPieces();

        int rectSize = (this.getWidth() < this.getHeight() ? this.getWidth()/positions.length : this.getHeight()/ positions.length);
        // Draw Board Positions
        for (int row = 0; row < positions.length; row++) {
            for (int col = 0; col < positions[0].length; col++) {
                positions[row][col].paint(g, rectSize);
            }
        }

        // Draw Board Pieces
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[0].length; col++) {
                Piece piece = pieces[row][col];
                if (piece != null) {
                    piece.paint(g, rectSize);
                }
            }
        }
    }
}
