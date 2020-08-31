package Game;

public class Game {

    private Board board;

    public Game() {
        int size = 8;
        int rowsOfPieces = 3;
        initBoard(size, rowsOfPieces);
        System.out.println(board.toString());
    }

    public void initBoard(int size, int rowsOfPieces) {
        board = new Board(size, rowsOfPieces); // Create an 8*8 board
    }


    public Board getBoard() {
        return this.board;
    }
}
