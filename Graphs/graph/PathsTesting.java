package graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class PathsTesting {

    private class GraphPath extends SimpleShortestPaths {

        public GraphPath(Graph G, int source, int dest) {
            super(G, source, dest);
            _edgeWeight = new HashMap<Integer, Double>();
            marked = false;
        }

        @Override
        protected double getWeight(int u, int v) {
            int id = _G.edgeId(u, v);
            return _edgeWeight.get(id);
        }

        @Override
        protected double estimatedDistance(int v) {
            marked = true;
            if (v == 4) {
                return 102.0;
            } else if (v == 2) {
                return 4.0;
            } else if (v == 5) {
                return 5.1;
            } else if (v == 6) {
                return 40.0;
            } else {
                return 0;
            }
        }

        private void setWeights(int u, int v, double k) {
            int id = _G.edgeId(u, v);
            _edgeWeight.put(id, k);
        }

        private HashMap<Integer, Double> _edgeWeight;

        private boolean marked;

    }
    @Test
    public void testBasics() {
        DirectedGraph g = new DirectedGraph();
        List<Integer> answer = new ArrayList<Integer>();
        answer.add(4);
        answer.add(2);
        answer.add(3);
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.remove(1);
        g.add(4, 2);
        g.add(4, 5);
        g.add(4, 3);
        g.add(2, 3);
        g.add(5, 3);
        g.add(5, 6);
        g.add(6, 7);
        GraphPath path = new GraphPath(g, 4, 3);
        path.setWeights(4, 2, 12.2);
        path.setWeights(2, 3, 6.5);
        path.setWeights(4, 3, 102.0);
        path.setWeights(4, 5, 11.2);
        path.setWeights(5, 3, 9.1);
        path.setWeights(5, 6, 30.0);
        path.setWeights(6, 7, 13.0);
        path.setPaths();
        List<Integer> expected = path.pathTo();
        assertEquals(path.getWeight(4), 0, 0.01);
        assertEquals(path.getWeight(2, 3), 6.5, 0.01);
        assertEquals(path.getWeight(3), 18.7, 0.01);
        assertEquals(path.getPredecessor(5), 4);
        assertEquals(path.getSource(), 4);
        assertEquals(path.getDest(), 3);
        assertEquals(path.getPredecessor(3), 2);
        assertEquals(answer.get(0), expected.get(0));
        assertEquals(answer.get(1), expected.get(1));
        assertEquals(answer.get(2), expected.get(2));
        assertEquals(path.getWeight(5), 11.2, 0.01);
        assertEquals(path.getWeight(6), 41.2, 0.01);
        assertEquals(path.getWeight(7), Double.POSITIVE_INFINITY, 0.01);
        assertEquals(true, path.marked);
    }
    @Test
    public void testDijkstra() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.remove(4);
        g.remove(2);
        g.remove(5);
        g.remove(6);
        g.add(1, 3);
        g.add(3, 9);
        g.add(1, 7);
        g.add(7, 8);
        GraphPath path = new GraphPath(g, 1, 8);
        path.setWeights(1, 3, 12.2);
        path.setWeights(1, 7, 6.5);
        path.setWeights(7, 8, 11.2);
        path.setWeights(3, 9, 30.0);
        path.setPaths();
        assertEquals(path.getWeight(3), 12.2, 0.01);
        assertEquals(path.getWeight(9), 42.2, 0.01);
        assertEquals(path.getWeight(7), 6.5, 0.01);
        assertEquals(path.getWeight(8), 17.7, 0.01);
        assertEquals(path.getPredecessor(3), 1);
        assertEquals(path.getPredecessor(7), 1);
        assertEquals(path.getPredecessor(8), 7);
    }
}
