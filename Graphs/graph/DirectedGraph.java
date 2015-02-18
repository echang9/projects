package graph;

import java.util.Collections;
import java.util.Iterator;

/* See restrictions in Graph.java. */

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Eric Chang.
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        if (!_predecessors.containsKey(v)) {
            return 0;
        } else {
            return _predecessors.get(v).size();
        }
    }

    @Override
    public int predecessor(int v, int k) {
        if (!_predecessors.containsKey(v) || k >= _predecessors.get(v).size()) {
            return 0;
        } else {
            return _predecessors.get(v).get(k);
        }
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        if (!contains(v) || _predecessors.get(v).isEmpty()) {
            Iterator<Integer> empty = Collections.emptyIterator();
            return Iteration.iteration(empty);
        }
        return Iteration.iteration(_predecessors.get(v).iterator());
    }

}
