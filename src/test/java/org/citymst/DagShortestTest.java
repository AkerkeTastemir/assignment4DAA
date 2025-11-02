package org.citymst;

import org.citymst.graph.dagsp.DagShortestPaths;
import org.citymst.graph.metrics.AlgorithmMetrics;
import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.model.Graph;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests DAG single-source shortest path using a fixed small DAG.
 */
public class DagShortestTest {

    @Test
    void testShortestPathsInDag() {
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
        DagShortestPaths sssp = new DagShortestPaths(dag, order, m);
        var result = sssp.run(0);

        assertArrayEquals(new int[]{0,2,3,4}, result.dist, "Expected distances [0,2,3,4]");
        assertTrue(m.relaxations() >= 3);

        var path = result.reconstructPath(3);
        assertEquals(List.of(0,1,3), path, "Shortest path 0→3 should be [0,1,3]");
    }
}