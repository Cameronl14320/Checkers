package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Move;

import java.awt.*;
import java.util.ArrayList;

public class Board {

    private final Position[][] positions;
    private Piece[][] pieces;
    private int size;

    private ArrayList<Position> validPositions;

    public Board(int size, int rowsOfPieces) {
        validPositions = new ArrayList<>();
        this.size = size;
        positions = new Position[size][size];
        boolean stagger = true;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int color;
                if (stagger) {
                    if ((col + 1) % 2 == 0) {
                        color = 1;
                    } else {
                        color = 0;
                    }
                } else {
                    if ((col + 1)% 2 == 0) {
                        color = 0;
                    } else {
                        color = 1;
                    }
                }
                positions[row][col] = new Position(row, col, color, null);
            }
            if (stagger) {
                stagger = false;
            } else {
                stagger = true;
            }
        }

        if (rowsOfPieces*2 > size) {
            return;
        }

        pieces = new Piece[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (positions[row][col].isBlack()) {
                    if (row >= 0 && row < rowsOfPieces) {
                        pieces[row][col] = new Piece(0, positions[row][col]);
                    }

                    if (row >= (size - rowsOfPieces) && row < size) {
                        pieces[row][col] = new Piece(1, positions[row][col]);
                    }
                }
            }
        }
    }

    public void getValidMoves(Piece piece) {
        for (Position p : validPositions) {
            p.setHighlightTile(false);
        }
        validPositions = new ArrayList<>();
        Move nextMove = null;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position nextPosition = positions[row][col];
                Piece takePiece = pieces[row][col];
                // Determine if able to take a piece
                if (takePiece != null) {
                    nextPosition = findJumpPosition(piece, takePiece);
                    nextMove = new Jump(piece, takePiece, nextPosition, pieceAt(row, col));
                // Determine if standard movement available
                } else {
                    nextMove = new Forward(piece, nextPosition, pieceAt(row, col));
                }
                // Add it to available moves
                if (nextMove != null && nextMove.isValid()) {
                    validPositions.add(nextPosition);
                    nextMove = null;
                }
            }
        }

        for (Position p: validPositions) {
            p.setHighlightTile(true);
        }
    }

    public Position findJumpPosition(Piece current, Piece take) {
        Position currentPosition = current.getPosition();
        Position takePosition = take.getPosition();
        int newRow = 0;
        int newCol = 0;

        // Determine Column after Taken Piece
        if (currentPosition.getCol() < takePosition.getCol()) {
            newCol = takePosition.getCol() + 1;
        } else {
            newCol = takePosition.getCol() - 1;
        }
        // Determine Row after Taken Piece
        if (currentPosition.getRow() < takePosition.getRow()) {
            newRow = takePosition.getRow() + 1;
        } else {
            newRow = takePosition.getRow() - 1;
        }

        // Return if valid position
        if (positions[newRow][newCol] != null) {
            return positions[newRow][newCol];
        }
        return null;
    }


    public boolean pieceAt(int row, int col) {
        return (pieces[row][col] != null);
    }

    public Piece getPieceAt(int row, int col) {
        if (pieces[row][col] != null) {
            return pieces[row][col];
        } else {
            return null;
        }
    }

    public void paint(Graphics g, int rectSize) {
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

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < positions.length; row++) {
            for (int col = 0; col < positions[0].length; col++) {
                boardString.append(positions[row][col].toString());
            }
            boardString.append("|\n");
        }
        boardString.append("\n");

        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[0].length; col++) {
                if (pieces[row][col] != null) {
                    boardString.append(pieces[row][col].toString());
                } else {
                    boardString.append("|_");
                }
            }
            boardString.append("|\n");
        }
        return boardString.toString();
    }


}
