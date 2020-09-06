package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Action;
import Movement.Move;

import java.awt.*;
import java.util.*;

public class Board {

    public static Color BLACK_TILE = new Color(0, 0, 0);
    public static Color WHITE_TILE = new Color(255, 255, 255);
    public static Color HIGHLIGHT_TILE = new Color(39, 142, 187);

    public static Color PLAYER_ONE_INNER = new Color(116, 38, 38);
    public static Color PLAYER_ONE_OUTER = new Color(165, 39, 39);
    public static Color PLAYER_TWO_INNER = new Color(172, 166, 139);
    public static Color PLAYER_TWO_OUTER = new Color(210, 202, 178);
    public static Color SELECT_HIGHLIGHT = new Color(206, 187, 45);
    public static Color MOVABLE_HIGHLIGHT = new Color(0x43B443);
    public static Color PROMOTED_CROWN = new Color(132, 17, 196);

    private static Position[][] positions;
    private Map<Position, Piece> pieces;
    private Map<Piece, Position> positionsMap;
    private final int size;
    private final int rowsOfPieces;

    private Set<Piece> capturedBlack;
    private Set<Piece> capturedWhite;

    private ArrayList<Position> validPositions;

    public Board(int size, int rowsOfPieces) {
        this.size = size;
        this.rowsOfPieces = rowsOfPieces;
        this.capturedBlack = new HashSet<>();
        this.capturedWhite = new HashSet<>();

        validPositions = new ArrayList<>();
        pieces = new HashMap<>();
        positions = makePositions(size, rowsOfPieces);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (positions[row][col].isBlack()) {
                    Position currentPosition = positions[row][col];
                    if (row < rowsOfPieces) {
                        addPiece(new Piece(0), currentPosition);
                    }

                    if (row >= (size - rowsOfPieces)) {
                        addPiece(new Piece(1), currentPosition);
                    }
                }
            }
        }
    }

    public void addPiece(Piece piece, Position position) {
        pieces.put(position, piece);
        positionsMap.put(piece, position);
    }

    public void removePiece(Piece piece, Position position) {
        pieces.remove(position);
        positionsMap.remove(piece);
    }

    public void removePositionHighlights() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                positions[row][col].setHighlightTile(false);
            }
        }
    }

    public void highlightMovable(int player) {

    }

    public void removePieceHighlights() {
        for (Position pos : pieces.keySet()) {
            Piece p = pieces.get(pos);
            p.setMovable(false);
            p.setSelected(false);
        }
    }

    public void checkPromoted(Piece piece) {
        if (piece.getPlayer() == 1) {
            if (positionsMap.get(piece).getRow() == 0) {
                piece.setPromoted(true);
            }
        } else {
            if (positionsMap.get(piece).getRow() == size - 1) {
                piece.setPromoted(true);
            }
        }
    }

    public void getValidPositions(Piece piece, boolean mustJump) {

    }

    public ArrayList<Move> getValidMoves(Piece piece) {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Move newMove = null;
                if (properMovement(positionsMap.get(piece), positions[row][col])) {

                }
            }
        }
        return null;
    }

    public ArrayList<Forward> getAllForwards(Piece piece) {
        return null;
    }

    public ArrayList<Jump> getAllJumps(Piece piece) {
        return null;
    }


    public boolean properMovement(Position startPosition, Position goalPosition) {

        if (startPosition.equals(goalPosition)) {
            return false;
        }

        if (Math.abs(startPosition.getRow() - goalPosition.getRow()) != Math.abs(startPosition.getCol() - goalPosition.getCol())) {
            return false;
        }

        if (pieceAt(goalPosition)) {
            return false;
        }

        return true;
    }

    public boolean checkBounds(int row, int col) {
        if (row < 0 || col < 0) {
            return false;
        }

        if (row >= size - 1 || col >= size - 1) {
            return false;
        }

        return true;
    }

    // Getters

    public boolean pieceAt(int row, int col) {
        if (row >= size || col >= size) {
            return false;
        }

        if (row < 0 || col < 0) {
            return false;
        }
        return (pieces.get(positions[row][col]) != null);
    }

    public boolean pieceAt(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return (pieceAt(row, col));
    }

    public Piece getPieceAt(int row, int col) {
        if (pieceAt(row, col)) {
            return pieces.get(positions[row][col]);
        }
        return null;
    }

    public Piece getPieceAt(Position position) {
        if (pieceAt(position)) {
            int row = position.getRow();
            int col = position.getCol();
            return pieces.get(positions[row][col]);
        }
        return null;
    }

    public Position getPositionAt(int row, int col) {
        if (positions[row][col] != null) {
            return positions[row][col];
        } else {
            return null;
        }
    }

    /**
     * Intialise the Checkers Board through a Positions 2d Array
     * Remains constant throughout every Board object
     * @param size
     * @param rowsOfPieces
     * @return
     */
    private static Position[][] makePositions(int size, int rowsOfPieces) {
        Position[][] positions = new Position[size][size];
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
            throw new Error("Invalid number full rows for pieces");
        }
        return positions;
    }

    public void paint(Graphics g, int rectSize) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                paintPosition(positions[row][col], g, rectSize);
            }
        }

        // Draw Board Pieces
        for (Position pos : pieces.keySet()) {
            paintPiece(pos, pieces.get(pos), g, rectSize);
        }
    }

    public void paintPosition(Position position, Graphics g, int rectSize) {
        if (position.isBlack()) {
            g.setColor(BLACK_TILE);
        } else {
            g.setColor(WHITE_TILE);
        }
        if (position.isHighlighted()) {
            g.setColor(HIGHLIGHT_TILE);
        }
        g.fillRect(position.getCol() * rectSize, position.getRow() * rectSize, rectSize, rectSize);
    }

    public void paintPiece(Position position, Piece piece, Graphics g, int rectSize) {
        // Highlight
        int border = rectSize/16;
        if (piece.isSelected()) {
            g.setColor(SELECT_HIGHLIGHT);
            g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize),
                    rectSize - border, rectSize - border);
        } else if (piece.isMovable()) {
            g.setColor(MOVABLE_HIGHLIGHT);
            g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize),
                    rectSize - border, rectSize - border);
        }

        // Outer Circle
        border = rectSize/8;
        if (piece.isPromoted()) {
            g.setColor(PROMOTED_CROWN);
        } else {
            if (piece.isBlack()) {
                g.setColor(PLAYER_ONE_OUTER);
            } else {
                g.setColor(PLAYER_TWO_OUTER);
            }
        }
        g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize),
                rectSize - border, rectSize - border);

        // Inner Circle
        border = rectSize/4;
        if (piece.isBlack()) {
            g.setColor(PLAYER_ONE_INNER);
        } else {
            g.setColor(PLAYER_TWO_INNER);
        }
        g.fillOval(border/2 + (position.getCol() * rectSize), border/2 + (position.getRow() * rectSize),
                rectSize - border, rectSize - border);
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boardString.append(positions[row][col].toString());
            }
            boardString.append("|\n");
        }
        boardString.append("\n");

        for (Position pos : pieces.keySet()) {
            boardString.append(pieces.get(pos));
        }

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (pieces.get(positions[row][col]) != null) {
                    boardString.append(pieces.get(positions[row][col]).toString());
                } else {
                    boardString.append("|_");
                }
            }
            boardString.append("|\n");
        }
        return boardString.toString();
    }
}
