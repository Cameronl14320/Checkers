package AI;

import Game.Game;

/*
    Selection: Start from root R and select successive child nodes until a leaf node L is reached. The root is the current game state and a leaf is any node that has a potential child from which no simulation (playout) has yet been initiated. The section below says more about a way of biasing choice of child nodes that lets the game tree expand towards the most promising moves, which is the essence of Monte Carlo tree search.
    Expansion: Unless L ends the game decisively (e.g. win/loss/draw) for either player, create one (or more) child nodes and choose node C from one of them. Child nodes are any valid moves from the game position defined by L.
    Simulation: Complete one random playout from node C. This step is sometimes also called playout or rollout. A playout may be as simple as choosing uniform random moves until the game is decided (for example in chess, the game is won, lost, or drawn).
    Backpropagation: Use the result of the playout to update information in the nodes on the path from C to R.

 */

public class MonteCarlo {
    Game game;
    Node root;

    public MonteCarlo(Game game) {
        this.game = game;
        this.root = new Node(game.currentBoard.allValidMoves(), null);
    }

    public void search() {

    }

}
