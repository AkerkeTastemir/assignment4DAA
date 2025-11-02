package org.citymst;

import org.citymst.graph.io.JsonDataReader;
import org.citymst.graph.model.Graph;
import org.citymst.graph.metrics.AlgorithmMetrics;
import org.citymst.graph.metrics.Metrics;
import org.citymst.graph.scc.TarjanAlgorithm;
import org.citymst.graph.scc.CondensationGraph;
import org.citymst.graph.topo.KahnTopologicalSort;
import org.citymst.graph.dagsp.DagShortestPaths;
import org.citymst.graph.dagsp.DagLongestPaths;
import org.citymst.graph.util.Timer;

import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            // --- Load input JSON (choose dataset) ---
            String file = args.length > 0 ? args[0] : "data/small-1-dag.json";
            System.out.println("=== Loading dataset: " + file + " ===");

            JsonDataReader.Loaded loaded = JsonDataReader.loadSingleGraph(Path.of(file));
            Graph g = loaded.graph;

            System.out.println("\n=== Input Graph Summary ===");
            System.out.println("Vertices: " + g.n());
            long m = g.adj().stream().mapToLong(list -> list.size()).sum();
            System.out.println("Edges: " + m);
            System.out.println("Directed: " + g.isDirected());
            System.out.println("Weight model: " + loaded.weightModel);
            System.out.println("Source (if any): " + loaded.source);

            // --- SCC Phase ---
            System.out.println("\n=== SCC Detection (Tarjan) ===");
            Metrics sccMetrics = new AlgorithmMetrics();
            TarjanAlgorithm tarjan = new TarjanAlgorithm(g, sccMetrics);
            List<List<Integer>> sccs = tarjan.run();

            System.out.println("Total SCCs: " + sccs.size());
            for (int i = 0; i < sccs.size(); i++) {
                System.out.println("Component " + i + ": " + sccs.get(i));
            }
            System.out.println("DFS visits: " + sccMetrics.dfsVisits() +
                    ", edges: " + sccMetrics.dfsEdges() +
                    ", time: " + Timer.fmtNanos(sccMetrics.nanos()));

            // --- Condensation Graph ---
            CondensationGraph cond = new CondensationGraph(g, sccs);
            Graph dag = cond.getDAG();
            System.out.println("\n=== Condensation DAG Summary ===");
            System.out.println("Vertices (components): " + dag.n());
            long dagEdges = dag.adj().stream().mapToLong(List::size).sum();
            System.out.println("Edges in DAG: " + dagEdges);

            // --- Topological Sort (Kahn) ---
            System.out.println("\n=== Topological Ordering (Kahn) ===");
            Metrics topoMetrics = new AlgorithmMetrics();
            KahnTopologicalSort topo = new KahnTopologicalSort(dag, topoMetrics);
            var order = topo.sort();

            System.out.println("Topological order of components: " + order);
            System.out.println("Pushes: " + topoMetrics.pushes() +
                    ", Pops: " + topoMetrics.pops() +
                    ", time: " + Timer.fmtNanos(topoMetrics.nanos()));

            // --- Shortest & Longest Paths in DAG ---
            System.out.println("\n=== Shortest & Longest Paths in DAG ===");
            int source = (loaded.source != null) ? loaded.source : 0;

            // ---- Shortest Paths ----
            Metrics spMetrics = new AlgorithmMetrics();
            DagShortestPaths sssp = new DagShortestPaths(dag, order, spMetrics);
            DagShortestPaths.Result spRes = sssp.run(source);

            System.out.println("Shortest distances from source " + source + ":");
            for (int i = 0; i < spRes.dist.length; i++) {
                System.out.printf("  %d -> %d = %s%n", source, i,
                        (spRes.dist[i] == Integer.MAX_VALUE ? "∞" : spRes.dist[i]));
            }
            System.out.println("Relaxations: " + spMetrics.relaxations() +
                    ", time: " + Timer.fmtNanos(spMetrics.nanos()));

            // ---- Longest (Critical) Paths ----
            Metrics lpMetrics = new AlgorithmMetrics();
            DagLongestPaths lpaths = new DagLongestPaths(dag, order, lpMetrics);
            DagLongestPaths.Result lpRes = lpaths.run(source);

            int criticalNode = lpRes.findCriticalNode();
            System.out.println("\nCritical path length: " + lpRes.dist[criticalNode]);
            System.out.println("Critical path: " + lpRes.reconstructPath(criticalNode));
            System.out.println("Relaxations: " + lpMetrics.relaxations() +
                    ", time: " + Timer.fmtNanos(lpMetrics.nanos()));

            System.out.println("\n✅ DAG Shortest & Longest Path phase completed!");
            System.out.println("✅ Topological sorting completed successfully!");
            System.out.println("✅ SCC & Condensation phase completed successfully!");
            System.out.println("\n=== Process finished successfully ===");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to load/parse file. Run with: java -jar app.jar data/<filename>.json");
        }
    }
}
