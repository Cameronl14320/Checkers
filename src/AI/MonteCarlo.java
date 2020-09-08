package AI;

import Game.Game;
import Movement.Action;
import Movement.Move;

import java.util.*;

/*
    Selection: Start from root R and select successive child nodes until a leaf node L is reached. The root is the current game state and a leaf is any node that has a potential child from which no simulation (playout) has yet been initiated. The section below says more about a way of biasing choice of child nodes that lets the game tree expand towards the most promising moves, which is the essence of Monte Carlo tree search.
    Expansion: Unless L ends the game decisively (e.g. win/loss/draw) for either player, create one (or more) child nodes and choose node C from one of them. Child nodes are any valid moves from the game position defined by L.
    Simulation: Complete one random playout from node C. This step is sometimes also called playout or rollout. A playout may be as simple as choosing uniform random moves until the game is decided (for example in chess, the game is won, lost, or drawn).
    Backpropagation: Use the result of the playout to update information in the nodes on the path from C to R.

 */

public class MonteCarlo {
    private static final int maxComputeTime = 1 * 1000; // 1 second
    private static final int runsPerLoop = 1;
    private Game game;
    private Node root;

    public MonteCarlo(Game game) {
        this.game = game;
        this.root = new Node(game.getValidMoves(), null);
    }

    public Move search() {
        if (this.game.isGameOver()) {
            return null;
        }

        double startTime = System.currentTimeMillis();
        for (int i = 0; i < runsPerLoop; i++) {
            if (startTime - System.currentTimeMillis() > maxComputeTime) {
                break;
            }
            this.root._search(this.game, findCurrentTargetState(this.game));
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
        int moveCount = game.undoSize();
        while (!game.isGameOver()) {
            List<Move> validMoves = game.getValidMoves();
            Move randomMove = validMoves.get((int) (Math.random() * validMoves.size()));
            game.applyMove(randomMove);
            game.repaint();
            //System.out.println(game.toString());
            moveCount += 1;
        }

        Game.gameStates result = game.getGameState();
        for (int i = 0; i < moveCount - game.undoSize(); i++) {
            game.undoMove();
        }
        return result;
    }

    private class Node {
        private Map<Move, Node> moveToChildMap;
        private List<Move> validMoves;
        private Move move;
        private int wins;
        private int n;


        public Node(List<Move> validMoves, Move move) {
            this.moveToChildMap = new HashMap<>();
            this.validMoves = validMoves;
            this.move = move;
            this.wins = 0;
            this.n = 0;
        }

        public Game.gameStates search(Game game) {
            Game.gameStates targetState = findCurrentTargetState(game);

            game.applyMove(move);
            Game.gameStates simulationResult = this._search(game, targetState);
            game.undoMove();

            return simulationResult;
        }

        public Game.gameStates _search(Game game, Game.gameStates targetState) {
            this.n += 1;
            if (game.isGameOver()) {
                if (game.getGameState().equals(targetState)) {
                    this.wins += 1;
                }
                return game.getGameState();
            }

            Move selectedMove = this.ucbSelectMove();
            Node child = moveToChildMap.get(selectedMove);

            if (child == null) {
                Game.gameStates childTargetState = findCurrentTargetState(game);
                game.applyMove(selectedMove);
                List<Move> childValidMoves = game.getValidMoves();
                game.undoMove();

                child = new Node(childValidMoves, selectedMove);
                moveToChildMap.put(selectedMove, child);

                game.applyMove(selectedMove);
                Game.gameStates randomPlaythroughResult = randomPlaythrough(game);
                game.undoMove();

                child.n += 1;
                if (randomPlaythroughResult == childTargetState) {
                    child.wins += 1;
                }
                if (randomPlaythroughResult == targetState) {
                    this.wins += 1;
                }

                return randomPlaythroughResult;
            } else {
                Game.gameStates result = child.search(game);
                if (result == targetState) {
                    this.wins += 1;
                }
                return result;
            }
        }

        public Move ucbSelectMove() {
            double a = Math.sqrt(2);
            Move bestMove = null;
            double bestMoveScore = Float.NEGATIVE_INFINITY; // Smallest possible number

            for (Move m : validMoves) {
                Node child = moveToChildMap.get(m);
                if (child == null) {
                    return m;
                }
                double score = ((double) child.wins / child.n) + a * Math.sqrt(Math.log(this.n) / child.n);
                if (score > bestMoveScore) {
                    bestMove = m;
                    bestMoveScore = score;
                }

            }
            return bestMove;
        }

        public Move expSelectMove() {
            Move bestMove = null;
            double bestMoveScore = Float.NEGATIVE_INFINITY;

            for (Move m : validMoves) {
                Node child = moveToChildMap.get(m);
                if (child == null) {
                    continue;
                }

                double score = (double) child.wins / child.n;
                if (score > bestMoveScore) {
                    bestMove = m;
                    bestMoveScore = score;
                }
            }
            return bestMove;
        }

    }

}
