package jump61;

/** An automated Player.
 *  @author Eric Chang.
 */
class AI extends Player {

    /** Time allotted to all but final search depth (milliseconds). */
    private static final long TIME_LIMIT = 15000;

    /** Number of calls to minmax between checks of elapsed time. */
    private static final long TIME_CHECK_INTERVAL = 10000;

    /** Number of milliseconds in one second. */
    private static final double MILLIS = 1000.0;

    /** Copy of current read-only-board. */
    private Board _board;

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    public AI(Game game, Side color) {
        super(game, color);
    }

    @Override
    void makeMove() {
        _board = new MutableBoard(getBoard());
        int bestVal = Integer.MIN_VALUE;
        int bestSquare = 0;
        int possMov = 0;
        for (int i = 0; i < _board.size() * _board.size(); i++) {
            if (_board.isLegal(getSide(), i)) {
                possMov = i;
                _board.addSpot(getSide(), i);
                int curVal = minmax(getSide().opposite(),
                    _board, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (curVal > bestVal) {
                    bestVal = curVal;
                    bestSquare = i;
                }
                _board.undo();
            }
        }
        if (bestVal == Integer.MIN_VALUE) {
            getGame().makeMove(possMov);
            getGame().reportMove(getSide(), _board.row(possMov),
                _board.col(possMov));
        } else {
            getGame().makeMove(bestSquare);
            getGame().reportMove(getSide(), _board.row(bestSquare),
                _board.col(bestSquare));
        }
    }

    /** Return the minmax value of board B.
     *  @param p represent the Side we are on to evaluate.
     *  @param b board that we are using.
     *  @param d depth we are at.
     *  @param alpha represents the min score that the max player is assured of.
     *  @param beta represents the max score that the min player can have. */
    private int minmax(Side p, Board b, int d, int alpha, int beta) {
        int score;
        if (p == b.getWinner()) {
            return Integer.MAX_VALUE;
        } else if (p.opposite() == b.getWinner()) {
            return Integer.MIN_VALUE;
        }
        if (d == 0) {
            return staticEval(p, b);
        } else if (p.equals(getSide())) {
            for (int i = 0; i < b.size() * b.size(); i++) {
                if (b.isLegal(p, i)) {
                    b.addSpot(p, i);
                    score = minmax(p, b, d - 1, alpha, beta);
                    if (score > alpha) {
                        alpha = score;
                    }
                    if (alpha >= beta) {
                        _board.undo();
                        break;
                    }
                    _board.undo();
                }
            }
            return alpha;
        } else {
            for (int i = 0; i < b.size() * b.size(); i++) {
                if (b.isLegal(p, i)) {
                    b.addSpot(p, i);
                    score = minmax(p.opposite(), b, d - 1, alpha, beta);
                    if (score < beta) {
                        beta = score;
                    }
                    if (beta <= alpha) {
                        _board.undo();
                        break;
                    }
                    _board.undo();
                }
            }
            return beta;
        }
    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    private int staticEval(Side p, Board b) {
        return b.numOfSide(p) - b.numOfSide(p.opposite());
    }
}
