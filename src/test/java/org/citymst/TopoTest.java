package org.citymst;

import org.citymst.graph.metrics.AlgorithmMetrics;
import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.model.Graph;
import org.citymst.graph.topo.KahnTopologicalSort;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Kahn’s algorithm on a small deterministic DAG.
 */
public class TopoTest {

    @Test
    void testKahnTopologicalOrder() {
        // DAG structure: 0→1, 0→2, 1→3, 2→3
        Graph dag = new Graph(4, true);
        dag.addEdge(0,1,1);
        dag.addEdge(0,2,1);
        dag.addEdge(1,3,1);
        dag.addEdge(2,3,1);

        Metrics metrics = new AlgorithmMetrics();
        KahnTopologicalSort topo = new KahnTopologicalSort(dag, metrics);
        List<Integer> order = topo.sort();

        assertEquals(4, order.size(), "All 4 vertices should appear in topological order");

        // Verify order validity: for every edge u→v, index(u) < index(v)
        Map<Integer,Integer> pos = new HashMap<>();
        for (int i = 0; i < order.size(); i++) pos.put(order.get(i), i);

        for (int u = 0; u < 4; u++)
            for (var e : dag.adj().get(u))
                assertTrue(pos.get(u) < pos.get(e.getV()),
                        "Invalid order: " + u + " before " + e.getV());

        assertTrue(metrics.pushes() >= 1);
        assertEquals(4, metrics.pops());
    }
}