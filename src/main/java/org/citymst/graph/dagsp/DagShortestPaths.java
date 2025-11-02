package org.citymst.graph.dagsp;

import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.model.Edge;
import org.citymst.graph.model.Graph;

import java.util.*;

public class DagShortestPaths {
    private final Graph dag;
    private final List<Integer> topo;
    private final Metrics metrics;

    public DagShortestPaths(Graph dag, List<Integer> topo, Metrics metrics) {
        this.dag = dag;
        this.topo = topo;
        this.metrics = metrics;
    }

    public Result run(int source) {
        int n = dag.n();
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        metrics.start();
        for (int u : topo) {
            if (dist[u] == Integer.MAX_VALUE) continue;
            for (Edge e : dag.adj().get(u)) {
                int v = e.getV();
                int w = e.getW();
                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                    metrics.incRelax();
                }
            }
        }
        metrics.stop();
        return new Result(dist, parent);
    }

    public static class Result {
        public final int[] dist, parent;
        public Result(int[] d, int[] p) { dist = d; parent = p; }

        public List<Integer> reconstructPath(int target) {
            List<Integer> path = new ArrayList<>();
            for (int v = target; v != -1; v = parent[v]) path.add(v);
            Collections.reverse(path);
            return path;
        }
    }
}