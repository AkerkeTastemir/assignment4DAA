package org.citymst.graph.scc;

import org.citymst.graph.model.Edge;
import org.citymst.graph.model.Graph;
import org.citymst.graph.model.GraphBuilder;

import java.util.*;

public class CondensationGraph {
    private final Graph original;
    private final List<List<Integer>> sccs;
    private Graph dag;
    private int[] compOf;

    public CondensationGraph(Graph g, List<List<Integer>> sccs) {
        this.original = g;
        this.sccs = sccs;
        build();
    }

    private void build() {
        int n = original.n();
        compOf = new int[n];
        for (int i = 0; i < sccs.size(); i++)
            for (int v : sccs.get(i))
                compOf[v] = i;

        dag = GraphBuilder.directed(sccs.size());
        Set<String> seen = new HashSet<>();

        for (int u = 0; u < n; u++) {
            for (Edge e : original.adj().get(u)) {
                int a = compOf[u];
                int b = compOf[e.getV()];
                if (a != b) {
                    String key = a + "->" + b;
                    if (seen.add(key)) dag.addEdge(a, b, e.getW());
                }
            }
        }
    }

    public Graph getDAG() { return dag; }
    public int[] compOf() { return compOf; }
}