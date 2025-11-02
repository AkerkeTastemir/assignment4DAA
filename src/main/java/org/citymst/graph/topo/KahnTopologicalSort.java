package org.citymst.graph.topo;

import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.model.Edge;
import org.citymst.graph.model.Graph;

import java.util.*;

/**
 * Реализация топологической сортировки по алгоритму Кана.
 * Возвращает порядок вершин DAG. Подсчитывает push/pop операции.
 */
public class KahnTopologicalSort {

    private final Graph dag;
    private final Metrics metrics;

    public KahnTopologicalSort(Graph dag, Metrics metrics) {
        this.dag = dag;
        this.metrics = metrics;
    }

    public List<Integer> sort() {
        int n = dag.n();
        int[] indeg = new int[n];

        // считаем входящие рёбра
        for (int u = 0; u < n; u++) {
            for (Edge e : dag.adj().get(u)) {
                indeg[e.getV()]++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.add(i);
                metrics.incPush();
            }
        }

        List<Integer> order = new ArrayList<>();
        metrics.start();
        while (!q.isEmpty()) {
            int u = q.poll();
            metrics.incPop();
            order.add(u);

            for (Edge e : dag.adj().get(u)) {
                int v = e.getV();
                if (--indeg[v] == 0) {
                    q.add(v);
                    metrics.incPush();
                }
            }
        }
        metrics.stop();

        if (order.size() != n) {
            throw new IllegalStateException("Graph is not a DAG — topological order incomplete.");
        }

        return order;
    }
}