package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Action;
import Movement.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

public class Game extends JPanel {

    public enum gameStates {
            MENU, RUNNING, BLACK_WIN, WHITE_WIN, STALEMATE
    }

    // Core
    private final int size = 8;
    private final int rowsOfPieces = 1;
    public Board currentBoard;

    // Gameplay
    private int currentPlayer;
    private Move currentMove;
    private Stack<Move> previousMoves;
    private boolean forceJump = false;
    private boolean mustJump = false;
    private gameStates gameState;

    // Display
    private Piece selectedPiece;
    private Position selectedPosition;
    private int rectSize = 20;

    public Game() {
        currentPlayer = 0;
        previousMoves = new Stack<>();
        gameState = gameStates.RUNNING;
        initBoard(size, rowsOfPieces);
        changeTurn();
        currentBoard.highlightMovable(currentPlayer, forceJump);
    }

    public void convertMouse(MouseEvent e) {
        int row = e.getY()/rectSize;
        int col = e.getX()/rectSize;
        handleActions(row, col);
    }

    public void handleActions(int row, int col) {
        if (isGameOver()) {
            return;
        }
        if (currentBoard.pieceAt(row, col)) {
            handleSelection(currentBoard.getPieceAt(row, col));
        } else {
            if (selectedPiece == null) {
                return;
            }

            Position newSelect = currentBoard.getPositionAt(row, col);
            if (newSelect != null) {
                selectedPosition = newSelect;
            }
            if (selectedPosition != null) {
                Set<Move> validMoves;
                Position currentPosition = currentBoard.getPiecePosition(selectedPiece);
                Piece takePiece = currentBoard.findTakePiece(currentPosition, selectedPosition);

                Action action = null;
                if (takePiece != null) {
                    action = new Jump(selectedPiece, takePiece, currentPosition, currentBoard.getPiecePosition(takePiece),
                            selectedPosition, currentBoard.pieceAt(selectedPosition));
                } else {
                    action = new Forward(selectedPiece, currentPosition, selectedPosition, currentBoard.pieceAt(selectedPosition));
                }
                if (action != null) {
                    applyMove(action);
                }
            }
        }
    }

    public void applyMove(Action action) {
        boolean lastWasJump = false;
        if (action.getClass().equals(Jump.class)) {
            lastWasJump = true;
        } else {
            if (forceJump && !currentBoard.allValidMovesPlayer(currentPlayer, true).isEmpty()) {
                return;
            }
        }

        if (action.apply(currentBoard)) {
            currentBoard.checkPromoted(selectedPiece);
            currentMove.addAction(action);

            if (isGameOver()) {
                System.out.println("OMG THE GAME IS OVER");
            }
            if (lastWasJump && currentBoard.canJump(selectedPiece)) {
                // TODO : make end turn button
                mustJump = true;
                currentBoard.removePieceHighlights();
                currentBoard.removePositionHighlights();
                currentBoard.getValidPositions(selectedPiece, true);
                selectedPiece.setSelected(true);
            } else {
                changeTurn();
            }
        }
    }

    public void undoMove() {
        if (previousMoves.isEmpty()) {
            return;
        }
        previousMoves.pop().undo(currentBoard);
    }

    public void handleSelection(Piece newSelect) {
        if (newSelect.matchingPlayer(currentPlayer)) {
            if (mustJump) {
                return;
            } else if (forceJump && !currentBoard.allValidMovesPlayer(currentPlayer, true).isEmpty()) {
                if (currentBoard.canJump(newSelect)) {
                    selectedPiece = newSelect;
                    currentBoard.getValidPositions(newSelect, true);
                }
            } else {
                selectedPiece = newSelect;
                currentBoard.getValidPositions(newSelect, false);
            }
        }

        if (selectedPiece != null) {
            selectedPiece.setSelected(true);
        }
    }

    public boolean isGameOver() {
        int winner = currentBoard.returnWinningPlayer();
        if (winner != -1) {
            if (winner == 1) {
                gameState = gameStates.BLACK_WIN;
            } else {
                gameState = gameStates.WHITE_WIN;
            }
        } else if (previousMoves.size() > 75) {
            gameState = gameStates.STALEMATE;
        }

        if (gameState != gameStates.RUNNING) {
            return true;
        }
        return false;
    }

    public gameStates getGameState() {
        return gameState;
    }

    private void finishGame() {

    }

    public boolean endExtraJump() {
        if (mustJump) {
            changeTurn();
            return true;
        }
        return false;
    }

    private void changeTurn() {
        if (currentPlayer == 1) {
            currentPlayer = 0;
        } else {
            currentPlayer = 1;
        }
        mustJump = false;
        previousMoves.add(currentMove);
        currentMove = new Move(new ArrayList<>());
        currentBoard.removePieceHighlights();
        currentBoard.removePositionHighlights();
        currentBoard.highlightMovable(currentPlayer, forceJump);
        selectedPiece = null;
        selectedPosition = null;
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