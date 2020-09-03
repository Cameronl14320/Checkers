package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

public class Game extends JPanel {

    private Board currentBoard;
    private Stack<Board> previousBoards;

    private int currentPlayer;
    private final int size;
    private Piece selectedPiece;
    private Position selectedPosition;

    private ArrayList<Move> validMoves;

    private int delay = 40; // 40ms repaint delay
    private int rectSize = 20;

    public Game() {
        currentPlayer = 1;
        size = 8;
        int rowsOfPieces = 3;

        previousBoards = new Stack<Board>();
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
                if (selectedPiece != null && newSelect != selectedPiece) {
                    selectedPiece.setHighlight(false);
                }
                selectedPiece = newSelect;
                currentBoard.getValidMoves(selectedPiece);
            //}
        } else {
            if (selectedPiece != null) {
                Board newBoard = null;
                selectedPosition = currentBoard.getPositionAt(row, col);
                if (selectedPosition != null) {

                }
            }
        }
    }

    private void changeTurn() {
        if (currentPlayer == 1) {
            currentPlayer = 0;
        } else {
            currentPlayer = 1;
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
