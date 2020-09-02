package Game;

import Display.GUI;
import Movement.Move;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

public class Game extends JPanel {

    private Board currentBoard;
    private Stack<Board> previousBoards;

    private int currentPlayer;
    private final int size;
    private Piece selected;

    private ArrayList<Move> validMoves;

    private int delay = 40; // 40ms repaint delay
    private int rectSize = 20;

    public Game() {
        currentPlayer = 1;
        size = 8;
        int rowsOfPieces = 3;
        initBoard(size, rowsOfPieces);

        new Timer(delay, e->repaint()).start();
    }

    public void handleActions(MouseEvent e) {
        int row = e.getY()/rectSize;
        int col = e.getX()/rectSize;

        Piece newSelect = currentBoard.getPieceAt(row, col);
        if (newSelect != null) {
            //if (newSelect.matchingPlayer(currentPlayer)){
                newSelect.setHighlight(true);
                if (selected != null && newSelect != selected) {
                    selected.setHighlight(false);
                }
                selected = newSelect;
                currentBoard.getValidMoves(selected);
            //}
        } else {
            if (selected != null) {

            }
        }
    }

    public void initBoard(int size, int rowsOfPieces) {
        this.currentBoard = new Board(size, rowsOfPieces); // Create an 8*8 board
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        // Draw Board Positions
        rectSize = (this.getWidth() < this.getHeight() ? this.getWidth()/size : this.getHeight()/size);
        currentBoard.paint(g, rectSize);
    }
}
