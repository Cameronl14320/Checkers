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
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Game extends JPanel {

    public enum gameStates {
            RUNNING, BLACK_WIN, WHITE_WIN, STALEMATE
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
    public List<Move> previousMoves;
    private boolean forceJump = false;
    private boolean mustJump = false;
    private gameStates gameState;

    private final int moveLimit = 200;

    // Display
    private Piece selectedPiece;
    private Position selectedPosition;
    private int rectSize = 20;

    public Game() {
        currentPlayer = 0;
        previousMoves = new ArrayList<>();
        gameState = gameStates.RUNNING;
        initBoard(size, rowsOfPieces);
        changeTurn();
        currentBoard.highlightMovable(currentPlayer, forceJump);
    }

    public void convertMouse(MouseEvent e) {
        int row = e.getY()/rectSize;
        int col = e.getX()/rectSize;

        if (computer && currentPlayer != humanPlayer) {
            return;
        }
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
                Move newMove = new Move(new ArrayList<>());
                newMove.addAction(action);
                applyMove(newMove);
                handleAI();
            }
        }
    }

    public void applyMove(Move m) {
        for (Action action : m.getActions()) {
            boolean lastWasJump = false;
            if (action instanceof Jump) {
                lastWasJump = true;
            } else {
                if (forceJump && !currentBoard.allValidMoves(currentPlayer, true).isEmpty()) {
                    return;
                }
            }
            if (action.apply(currentBoard)) {
                currentBoard.checkPromoted(action.getPiece());
                currentMove.addAction(action);
                previousMoves.add(currentMove);
                changeTurn();
            }
        }
    }


    public void undoMove() throws MoveException {
        if (previousMoves.isEmpty()) {
            throw new MoveException("No previous moves");
        }
        Move lastMove = previousMoves.remove(previousMoves.size()-1);
        lastMove.undo(currentBoard);
        changeTurn();
    }


    public boolean isGameOver() {
        int winner = currentBoard.returnWinningPlayer();
        if (winner != -1) {
            return true;
        }
        if (previousMoves.size() >= moveLimit) {
            return true;
        } else if (getValidMoves().isEmpty()) {
            return true;
        }

        return false;
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
        if (computer) {
            if (currentPlayer == humanPlayer) {
                currentBoard.removePieceHighlights();
                currentBoard.removePositionHighlights();
                currentBoard.highlightMovable(currentPlayer, forceJump);
            }
        } else {
            currentBoard.removePieceHighlights();
            currentBoard.removePositionHighlights();
            currentBoard.highlightMovable(currentPlayer, forceJump);
        }
        selectedPiece = null;
        selectedPosition = null;
        getValidMovesCached = null;
    }

    public void handleAI() {
        MonteCarlo AI = new MonteCarlo(this);
        Move AIMove = AI.search();
        if (isGameOver()) {
            return;
        } else {
            applyMove(AIMove);
        }
    }

    public List<Move> getValidMovesCached = null;

    public List<Move> getValidMoves() {
        if (getValidMovesCached == null) {
            this.getValidMovesCached = this.currentBoard.allValidMoves(currentPlayer, mustJump);
        }
        return this.getValidMovesCached;
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

    public String toString() {
        return currentBoard.toString();
    }
}