package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

public class Game extends JPanel {

    // Core
    private final int size = 8;
    private final int rowsOfPieces = 1;
    private Board currentBoard;

    // Gameplay
    private int currentPlayer;
    private Stack<Action> previousActions;
    private boolean forceJump = false;

    // Display
    private Piece selectedPiece;
    private Position selectedPosition;
    private final int delay = 40; // 40ms repaint delay
    private int rectSize = 20;


    public Game() {
        currentPlayer = 1;
        previousActions = new Stack<>();
        initBoard(size, rowsOfPieces);
        new Timer(delay, e->repaint()).start();
        currentBoard.highlightMovable(currentPlayer);
    }

    public void convertMouse(MouseEvent e) {
        int row = e.getY()/rectSize;
        int col = e.getX()/rectSize;
        handleActions(row, col);
    }

    public void handleActions(int row, int col) {

    }

    public void handleSelection(Piece newSelect) {

    }

    public Action forwardOrJump(ArrayList<Piece> takePiece) {
        return null;
    }

    private void changeTurn() {
        if (currentPlayer == 1) {
            currentPlayer = 0;
        } else {
            currentPlayer = 1;
        }
        currentBoard.removePieceHighlights();
        currentBoard.removePositionHighlights();
        currentBoard.highlightMovable(currentPlayer);
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