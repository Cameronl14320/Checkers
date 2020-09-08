package Game;

import AI.MonteCarlo;
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
    private final int rowsOfPieces = 3;
    private Board currentBoard;

    // Gameplay
    private boolean computer = true;
    private int humanPlayer = 1;
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

    public void handleSelection(Piece newSelect) {
        if (newSelect.matchingPlayer(currentPlayer)) {
            if (mustJump) {
                return;
            } else if (forceJump && !currentBoard.allValidMoves(currentPlayer, true).isEmpty()) {
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

    public void handleActions(int row, int col) {
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

    public void applyMove(Move m) {
        for (Action a : m.getActions()) {
            applyMove(a);
        }
    }

    public void applyMove(Action action) {
        boolean lastWasJump = false;
        if (action.getClass().equals(Jump.class)) {
            lastWasJump = true;
        } else {
            if (forceJump && !currentBoard.allValidMoves(currentPlayer, true).isEmpty()) {
                return;
            }
        }

        if (action.apply(currentBoard)) {
            currentBoard.checkPromoted(action.getPiece());
            currentMove.addAction(action);

            if (lastWasJump && currentBoard.canJump(action.getPiece())) {
                // TODO : make end turn button
                mustJump = true;
                currentBoard.removePieceHighlights();
                currentBoard.removePositionHighlights();
                currentBoard.getValidPositions(action.getPiece(), true);
                action.getPiece().setSelected(true);
            } else {
                previousMoves.add(currentMove);
                changeTurn();
                handleAI();
            }
        }
    }

    public boolean undoMove() {
        if (previousMoves.isEmpty()) {
            return false;
        }
        previousMoves.pop().undo(currentBoard);
        changeTurn();
        return true;
    }


    public boolean isGameOver() {
        int winner = currentBoard.returnWinningPlayer();
        if (winner != -1) {
            if (winner == 1) {
                gameState = gameStates.BLACK_WIN;
            } else {
                gameState = gameStates.WHITE_WIN;
            }
        } else if (previousMoves.size() > 200) {
            gameState = gameStates.STALEMATE;
        }

        if (gameState != gameStates.RUNNING) {
            return true;
        }
        return false;
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
        currentMove = new Move(new ArrayList<>());
        currentBoard.removePieceHighlights();
        currentBoard.removePositionHighlights();
        currentBoard.highlightMovable(currentPlayer, forceJump);
        selectedPiece = null;
        selectedPosition = null;
    }

    private void handleAI() {
        if (isGameOver()) {
            return;
        }
        if (computer) {
            while (currentPlayer != humanPlayer) {
                findAIMove();
            }
        }
    }

    private void findAIMove() {
        if (!computer) {
            return;
        }
        if (currentPlayer == humanPlayer) {
            return;
        }
        MonteCarlo AI = new MonteCarlo(this);
        Move AIMove = AI.search();
        if (AIMove == null) {
            return;
        }
        for (Action a : AIMove.getActions()) {
            applyMove(a);
        }
    }

    public java.util.List<Move> getValidMoves(int player, boolean mustJump) {
        return this.currentBoard.allValidMoves(player, mustJump);
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

    public gameStates getGameState() {
        return gameState;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean getMustJump() {
        return this.mustJump;
    }
}