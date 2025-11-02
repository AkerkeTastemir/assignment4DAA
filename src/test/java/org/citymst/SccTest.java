package org.citymst;

import org.citymst.graph.metrics.AlgorithmMetrics;
import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.model.Graph;
import org.citymst.graph.scc.TarjanAlgorithm;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the correctness of Tarjan SCC implementation
 * on a deterministic small directed graph.
 */
public class SccTest {

    @Test
    void testTarjanFindsCorrectSccs() {
        // Graph structure:
        // 0→1, 1→2, 2→0  -> one SCC (0,1,2)
        // 3→4, 4→3        -> one SCC (3,4)
        // 5                -> single vertex SCC
        Graph g = new Graph(6, true);
        g.addEdge(0,1,1);
        g.addEdge(1,2,1);
        g.addEdge(2,0,1);
        g.addEdge(3,4,1);
        g.addEdge(4,3,1);

        Metrics metrics = new AlgorithmMetrics();
        TarjanAlgorithm tarjan = new TarjanAlgorithm(g, metrics);
        List<List<Integer>> sccs = tarjan.run();

        assertEquals(3, sccs.size(), "Expected 3 SCC components");
        assertTrue(metrics.dfsVisits() >= 6);
        assertTrue(metrics.dfsEdges() >= 5);
    }
}