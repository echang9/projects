package jump61;

/** A Player that gets its moves from manual input.
 *  @author Eric Chang.
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. */
    HumanPlayer(Game game, Side color) {
        super(game, color);
    }

    @Override
    /** Retrieve moves using getGame().getMove() until a legal one is found and
     *  make that move in getGame().  Report erroneous moves to player. */
    void makeMove() {
        int[] moves = new int[2];

        if (getGame().getMove(moves)) {
            getGame().makeMove(moves[0], moves[1]);
        }
    }

}
