package org.citymst;

import org.citymst.graph.dagsp.DagLongestPaths;
import org.citymst.graph.metrics.AlgorithmMetrics;
import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.model.Graph;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests DAG longest path (critical path) using the same deterministic DAG.
 */
public class DagLongestTest {

    @Test
    void testLongestPathInDag() {
        // DAG:
        // 0→1(2), 0→2(5), 1→2(1), 1→3(3), 2→3(1)
        Graph dag = new Graph(4, true);
        dag.addEdge(0,1,2);
        dag.addEdge(0,2,5);
        dag.addEdge(1,2,1);
        dag.addEdge(1,3,3);
        dag.addEdge(2,3,1);

        List<Integer> order = List.of(0,1,2,3);

        Metrics m = new AlgorithmMetrics();
        DagLongestPaths lp = new DagLongestPaths(dag, order, m);
        var result = lp.run(0);

        // Expected longest path to vertex 3: 0→2→3 (length 6)
        assertEquals(6, result.dist[3], "Longest path to vertex 3 should be length 6");
        assertEquals(List.of(0,2,3), result.reconstructPath(3));
        assertTrue(m.relaxations() >= 3);
    }
}