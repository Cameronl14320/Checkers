package Game;

import Movement.Forward;
import Movement.Jump;
import Movement.Action;
import Movement.Move;

import java.awt.*;
import java.util.*;
import java.util.List;

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

    private ArrayList<Position> validPositions;

    public Board(int size, int rowsOfPieces) {
        this.size = size;

        validPositions = new ArrayList<>();
        pieces = new HashMap<>();
        positionsMap = new HashMap<>();
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

    public void highlightMovable(int player, boolean mustJump) {
        boolean canJump = false;
        if (mustJump) {
            for (Piece p : positionsMap.keySet()) {
                if (p.matchingPlayer(player)) {
                    Set<Move> validMoves = getAllJumps(p);
                    if (!validMoves.isEmpty()) {
                        p.setMovable(true);
                        canJump = true;
                    } else {
                        p.setMovable(false);
                    }
                } else {
                    p.setMovable(false);
                }
            }
        }
        if (!mustJump || canJump == false) {
            for (Piece p : positionsMap.keySet()) {
                if (p.matchingPlayer(player)) {
                    Set<Move> validMoves = getValidMoves(p);
                    if (!validMoves.isEmpty()) {
                        p.setMovable(true);
                    } else {
                        p.setMovable(false);
                    }
                } else {
                    p.setMovable(false);
                }
            }
        }
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
        for (Position p : validPositions) {
            p.setHighlightTile(false);
        }
        validPositions = new ArrayList<>();

        Set<Move> validMoves;
        if (mustJump) {
            validMoves = getAllJumps(piece);
        } else {
            validMoves = getValidMoves(piece);
        }

        for (Move m : validMoves) {
            for (Position p : m.getNextPositions()) {
                validPositions.add(p);
            }
        }

        for (Position p : validPositions) {
            p.setHighlightTile(true);
        }
    }

    public List<Move> allValidMoves() {
        List<Move> allValidMoves = new ArrayList<>();
        for (Piece p : positionsMap.keySet()) {
            Set<Move> tempMoves = getValidMoves(p);
            for (Move m : tempMoves) {
                allValidMoves.add(m);
            }
        }
        return allValidMoves;
    }

    public List<Move> allValidMovesPlayer(int player, boolean mustJump) {
        List<Move> allValidMoves = new ArrayList<>();
        for (Piece p : positionsMap.keySet()) {
            Set<Move> tempMoves = new HashSet<>();
            if (p.matchingPlayer(player)) {
                if (mustJump) {
                    tempMoves = getAllJumps(p);
                } else {
                    tempMoves = getValidMoves(p);
                }
            }

            for (Move m : tempMoves) {
                allValidMoves.add(m);
            }
        }
        return allValidMoves;
    }

    public Set<Move> getValidMoves(Piece piece) {
        Set<Move> validMoves = new HashSet<>();
        Set<Move> validForwards = getAllForwards(piece);
        Set<Move> validJumps = getAllJumps(piece);

        for (Move m : validForwards) {
            validMoves.add(m);
        }
        for (Move m : validJumps) {
            validMoves.add(m);
        }

        return validMoves;
    }

    private Set<Move> getAllForwards(Piece piece) {
        Set<Move> validForwards = new HashSet<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Move newMove = null;
                if (properMovement(positionsMap.get(piece), positions[row][col], 1)) {
                    Forward newForward = new Forward(piece, positionsMap.get(piece), positions[row][col], pieceAt(row, col));
                    if (newForward.isValid()) {
                        ArrayList<Action> newAction = new ArrayList<>();
                        newAction.add(newForward);
                        newMove = new Move(newAction);
                    }
                }
                if (newMove != null) {
                    validForwards.add(newMove);
                }
            }
        }
        return validForwards;
    }

    private Set<Move> getAllJumps(Piece piece) {
        Set<Move> validJumps = new HashSet<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Move newMove = null;
                if (properMovement(positionsMap.get(piece), positions[row][col], 2)) {
                    Piece takePiece = findTakePiece(positionsMap.get(piece), positions[row][col]);
                    if (takePiece != null) {
                        Jump newJump = new Jump(piece, takePiece, positionsMap.get(piece), positionsMap.get(takePiece), positions[row][col], pieceAt(row, col));
                        if (newJump.isValid()) {
                            ArrayList<Action> newAction = new ArrayList<>();
                            newAction.add(newJump);
                            newMove = new Move(newAction);
                        }
                    }
                }
                if (newMove != null) {
                    validJumps.add(newMove);
                }
            }
        }
        return validJumps;
    }

    public boolean canJump(Piece piece) {
        return !getAllJumps(piece).isEmpty();
    }

    public int returnWinningPlayer() {
        int blackPieces = 0;
        int whitePieces = 0;
        for (Piece p : positionsMap.keySet()) {
            if (p.getPlayer() == 1) {
                blackPieces++;
            } else {
                whitePieces++;
            }
        }

        if (blackPieces == 0) {
            return 0;
        } else if (whitePieces == 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public Piece findTakePiece(Position startPosition, Position goalPosition) {
        if (!properMovement(startPosition, goalPosition, 2)) {
            return null;
        }

        int newCol;
        int newRow;

        if (startPosition.getCol() < goalPosition.getCol()) {
            newCol = startPosition.getCol() + 1;
        } else {
            newCol = goalPosition.getCol() + 1;
        }

        if (startPosition.getRow() < goalPosition.getRow()) {
            newRow = startPosition.getRow() + 1;
        } else {
            newRow = goalPosition.getRow() + 1;
        }

        if (newCol != -1 && newRow != -1) {
            if (pieceAt(newRow, newCol)) {
                return getPieceAt(newRow, newCol);
            }
        }
        return null;
    }

    public boolean properMovement(Position startPosition, Position goalPosition, int expectedDistance) {

        if (!checkBounds(startPosition.getRow(), startPosition.getCol())) {
            return false;
        }

        if (!checkBounds(goalPosition.getRow(), goalPosition.getCol())) {
            return false;
        }

        if (startPosition.equals(goalPosition)) {
            return false;
        }

        if (Math.abs(startPosition.getRow() - goalPosition.getRow()) != Math.abs(startPosition.getCol() - goalPosition.getCol())) {
            return false;
        }

        if (Math.abs(startPosition.getRow() - goalPosition.getRow()) != expectedDistance) {
            return false;
        }

        if (Math.abs(startPosition.getCol() - goalPosition.getCol()) != expectedDistance) {
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

        if (row >= size || col >= size) {
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

    public Position getPiecePosition(Piece piece) {
        if (positionsMap.containsKey(piece)) {
            return positionsMap.get(piece);
        }
        return null;
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
                positions[row][col] = new Position(row, col, color);
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
