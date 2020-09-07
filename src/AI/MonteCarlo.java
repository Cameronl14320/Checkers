package AI;

import Game.Game;
import Movement.Action;
import Movement.Move;

import java.util.List;
import java.util.Set;

/*
    Selection: Start from root R and select successive child nodes until a leaf node L is reached. The root is the current game state and a leaf is any node that has a potential child from which no simulation (playout) has yet been initiated. The section below says more about a way of biasing choice of child nodes that lets the game tree expand towards the most promising moves, which is the essence of Monte Carlo tree search.
    Expansion: Unless L ends the game decisively (e.g. win/loss/draw) for either player, create one (or more) child nodes and choose node C from one of them. Child nodes are any valid moves from the game position defined by L.
    Simulation: Complete one random playout from node C. This step is sometimes also called playout or rollout. A playout may be as simple as choosing uniform random moves until the game is decided (for example in chess, the game is won, lost, or drawn).
    Backpropagation: Use the result of the playout to update information in the nodes on the path from C to R.

 */

public class MonteCarlo {
    private static final int maxComputeTime = 1 * 1000; // 1 second
    private static final int runsPerLoop = 1000;
    private Game game;
    private Node root;

    public MonteCarlo(Game game) {
        this.game = game;
        this.root = new Node(game.currentBoard.allValidMovesPlayer(game.getCurrentPlayer(), game.getMustJump()), null);
    }

    public Move search() {
        if (this.game.isGameOver()) {
            return null;
        }

        int totalIterations = 0;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < maxComputeTime) {
            for (int i = 0; i < runsPerLoop; i++) {
                this.root.search(this.game, findCurrentTargetState(this.game));
            }
            totalIterations += runsPerLoop;
        }

        return this.root.expSelectMove();
    }

    private Game.gameStates findCurrentTargetState(Game game) {
        if (game.getCurrentPlayer() == 1) {
            return Game.gameStates.BLACK_WIN;
        } else if (game.getCurrentPlayer() == 0) {
            return Game.gameStates.WHITE_WIN;
        } else {
            throw new Error("Invalid current player");
        }
    }

    public Game.gameStates randomPlaythrough(Game game) {
        int moveCount = 0;
        while (!game.isGameOver()) {
            List<Move> validMoves = game.currentBoard.allValidMovesPlayer(game.getCurrentPlayer(), game.getMustJump());
            Move randomMove = validMoves.get((int) Math.random() * validMoves.size());
            for (Action a: randomMove.getActions()) {
                game.applyMove(a);
            }
            moveCount += 1;
        }

        Game.gameStates result = game.getGameState();
        for (int i = 0; i < moveCount; i++) {
            game.undoMove();
        }
        return result;
    }

}
