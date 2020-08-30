public class Game {

    private Board board;

    public Game() {
        initBoard();
        System.out.println(board.toString());
    }

    public void initBoard() {
        board = new Board(8); // Create an 8*8 board
    }

}
