package jump61;

import static jump61.Side.*;
import static jump61.Square.square;

import java.util.Stack;

/** A Jump61 board state that may be modified.
 *  @author Eric Chang.
 */
class MutableBoard extends Board {

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        _N = N;
        squares = new Square[N * N];
        for (int i = 0; i < N * N; i++) {
            squares[i] = Square.square(Side.WHITE, 0);
        }
    }

    /** A board whose initial contents are copied from BOARD0, but whose
     *  undo history is clear. */
    MutableBoard(Board board0) {
        copy(board0);
        movesHistory = new Stack<Board>();
    }

    @Override
    void clear(int N) {
        _N = N;
        squares = new Square[N * N];
        for (int i = 0; i < N * N; i++) {
            squares[i] = Square.square(Side.WHITE, 0);
        }
        movesHistory = new Stack<Board>();
        announce();
    }

    @Override
    void copy(Board board) {
        _N = board.size();
        squares = new Square[_N * _N];
        for (int i = 0; i < _N * _N; i++) {
            squares[i] = board.get(i);
        }
    }

    /** Copy the contents of BOARD into me, without modifying my undo
     *  history.  Assumes BOARD and I have the same size. */
    private void internalCopy(MutableBoard board) {
        copy(board);
    }

    @Override
    int size() {
        return _N;
    }

    @Override
    Square get(int n) {
        return squares[sqNum(row(n), col(n))];
    }

    @Override
    int numOfSide(Side side) {
        int count = 0;
        for (int i = 0; i < _N * _N; i++) {
            if ((squares[i].getSide()).equals(side)) {
                count += 1;
            }
        }
        return count;
    }

    @Override
    int numPieces() {
        int count = 0;
        for (int i = 0; i < _N * _N; i++) {
            count += squares[i].getSpots();
        }
        return count;
    }

    @Override
    void addSpot(Side player, int r, int c) {
        markUndo();
        int spots = squares[sqNum(r, c)].getSpots();
        if (isLegal(player, r, c)) {
            squares[sqNum(r, c)] = Square.square(player, spots + 1);
            if (squares[sqNum(r, c)].getSpots() > neighbors(r, c)) {
                overfull(player, sqNum(r, c));
            }
        }
        announce();
    }

    /** Check and add overfull spots recursively.
     *  @param player turn of the SIDE.
     *  @param n the square #N of the board. */
    private void overfull(Side player, int n) {
        if (n >= size() * size()) {
            return;
        } else if (getWinner() != null) {
            return;
        } else if (squares[n].getSpots() > neighbors(n)) {
            addNeighborSpots(player, row(n), col(n));
            squares[n] = Square.square(player, 1);
        } else {
            overfull(player, n + 1);
            return;
        }
        overfull(player, 0);
    }

    /** Add spots to neighbor squares.
     *  @param player turn of the Side.
     *  @param r row of the board.
     *  @param c col of the board. */
    private void addNeighborSpots(Side player, int r, int c) {
        int size = size();
        if (r > 1) {
            squares[sqNum(r - 1, c)] =
                Square.square(player, squares[sqNum(r - 1, c)].getSpots() + 1);
        }
        if (c > 1) {
            squares[sqNum(r, c - 1)] =
                Square.square(player, squares[sqNum(r, c - 1)].getSpots() + 1);
        }
        if (r < size) {
            squares[sqNum(r + 1, c)] =
                Square.square(player, squares[sqNum(r + 1, c)].getSpots() + 1);
        }
        if (c < size) {
            squares[sqNum(r, c + 1)] =
                Square.square(player, squares[sqNum(r, c + 1)].getSpots() + 1);
        }
    }

    @Override
    void addSpot(Side player, int n) {
        addSpot(player, row(n), col(n));
        announce();
    }

    @Override
    void set(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), square(player, num));
    }

    @Override
    void set(int n, int num, Side player) {
        internalSet(n, square(player, num));
        announce();
    }

    @Override
    void undo() {
        if (!movesHistory.empty()) {
            Board prevBoard = movesHistory.pop();
            copy(prevBoard);
        } else {
            System.out.println("no undo history");
        }
    }

    /** Record the beginning of a move in the undo history. */
    private void markUndo() {
        Board initial = new MutableBoard(this);
        movesHistory.push(initial);
    }

    /** Set the contents of the square with index IND to SQ. Update counts
     *  of numbers of squares of each color.  */
    private void internalSet(int ind, Square sq) {
        movesHistory = new Stack<Board>();
        squares[ind] = sq;
    }

    /** Notify all Observers of a change. */
    private void announce() {
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MutableBoard)) {
            return obj.equals(this);
        } else {
            return this.toString().equals(((Board) obj).toString());
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /** Number of squares on the board. */
    private int _N;
    /** Array of Square objects of the board. */
    private Square[] squares;
    /** Stack to hold moves to undo. */
    private Stack<Board> movesHistory = new Stack<Board>();

}
