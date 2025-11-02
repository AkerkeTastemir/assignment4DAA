package org.citymst.graph.scc;

import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.model.Edge;
import org.citymst.graph.model.Graph;

import java.util.*;

public class TarjanAlgorithm {
    private final Graph g;
    private final Metrics metrics;
    private final List<List<Integer>> components = new ArrayList<>();

    private int time = 0;
    private int[] disc, low;
    private boolean[] inStack;
    private Deque<Integer> stack = new ArrayDeque<>();

    public TarjanAlgorithm(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public List<List<Integer>> run() {
        int n = g.n();
        disc = new int[n];
        low = new int[n];
        inStack = new boolean[n];
        Arrays.fill(disc, -1);

        metrics.start();
        for (int i = 0; i < n; i++)
            if (disc[i] == -1)
                dfs(i);
        metrics.stop();

        return components;
    }

    private void dfs(int u) {
        metrics.incDfsVisit();
        disc[u] = low[u] = ++time;
        stack.push(u);
        inStack[u] = true;

        for (Edge e : g.adj().get(u)) {
            metrics.incDfsEdge();
            int v = e.getV();
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                inStack[w] = false;
                comp.add(w);
            } while (w != u);
            components.add(comp);
        }
    }
}