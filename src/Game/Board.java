package Game;

public class Board {

    private final Position[][] positions;
    private Piece[][] pieces;
    private int size;

    public Board(int size, int rowsOfPieces) {
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

        createPieces(rowsOfPieces);
    }

    public void createPieces(int rowsOfPieces) {
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

    public Board(Board board) {
        positions = null;
    }

    public Position[][] getPositions() {
        return positions;
    }

    public Piece[][] getPieces() {
        return pieces;
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
