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

    // Core
    private final int size = 8;
    private final int rowsOfPieces = 1;
    private Board currentBoard;

    // Gameplay
    private int currentPlayer;
    private Stack<Move> previousMoves;
    private boolean forceJump = false;

    // Display
    private Piece selectedPiece;
    private Position selectedPosition;
    private final int delay = 40; // 40ms repaint delay
    private int rectSize = 20;


    public Game() {
        currentPlayer = 1;
        previousMoves = new Stack<>();
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
        Piece newSelect = currentBoard.getPieceAt(row, col);
        if (newSelect != null) {
            handleSelection(newSelect);
        } else if (selectedPiece != null) {
            selectedPosition = currentBoard.getPositionAt(row, col);
            if (selectedPosition != null) {
                ArrayList<Piece> takePiece = currentBoard.findTakePiece(selectedPiece, selectedPosition);
                Move newMove = forwardOrJump(takePiece);
                if (forceJump) {
                    if (currentBoard.canAnyJump(currentPlayer) && newMove.getClass().equals(Forward.class)) {
                        return;
                    }
                }
                if (newMove.apply(currentBoard)) {
                    currentBoard.checkPromoted(selectedPiece);
                    previousMoves.add(newMove);
                    currentBoard.removePositionHighlights();
                    selectedPiece.setSelected(false);
                    selectedPiece = null;
                    selectedPosition = null;
                    changeTurn();
                }
            }
        }
    }

    public void handleSelection(Piece newSelect) {
        if (newSelect.matchingPlayer(currentPlayer)) {
            if (selectedPiece != null && newSelect != selectedPiece) {
                newSelect.setSelected(true);
                selectedPiece.setSelected(false);
            }
            selectedPiece = newSelect;
            currentBoard.removePositionHighlights();

            if (forceJump && currentBoard.canAnyJump(currentPlayer)) {
                currentBoard.getValidPositions(selectedPiece, true);
            } else {
                currentBoard.getValidPositions(selectedPiece, false);
            }
        }
        repaint();
    }

    public Move forwardOrJump(ArrayList<Piece> takePiece) {
        if (!takePiece.isEmpty()) {
            return new Jump(selectedPiece, takePiece, selectedPosition, currentBoard.pieceAt(selectedPosition));
        } else {
            return new Forward(selectedPiece, selectedPosition, currentBoard.pieceAt(selectedPosition));
        }
    }

    private void changeTurn() {
        if (currentPlayer == 1) {
            currentPlayer = 0;
        } else {
            currentPlayer = 1;
        }
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