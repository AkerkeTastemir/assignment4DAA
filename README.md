# Report: Assignment 4

This document presents the analysis and results of implementing a graph pipeline for solving a task scheduling problem with dependencies.

---

## 1. Data Summary

### Weight Model
The project uses an **edge weight model** (`"weight_model": "edge"`) as specified in the dataset files.  
This model represents the time or cost of transitioning between tasks (vertices).

### Datasets
Nine datasets were created manually according to the required structure, covering various sizes, densities, and graph types (acyclic and cyclic).

| Category | File | N (Vertices) | M (Edges) | Structure |
|-----------|------|---------------|------------|------------|
| Small | small-1-dag | 6 | 6 | Acyclic (DAG) |
| Small | small-2-cycle | 7 | 6 | Cyclic |
| Small | small-3-multiscc | 8 | 8 | Cyclic (multi-SCC) |
| Medium | medium-1-dag | 12 | 12 | Acyclic (DAG) |
| Medium | medium-2-multiscc | 15 | 16 | Cyclic (multi-SCC) |
| Medium | medium-3-densecycle | 18 | 19 | Cyclic (dense) |
| Large | large-1-dag | 25 | 23 | Acyclic (DAG) |
| Large | large-2-multiscc | 30 | 29 | Cyclic (multi-SCC) |
| Large | large-3-densedag | 40 | 39 | Acyclic (dense DAG) |

---

## 2. Results (Metrics and Time)

> The tables below summarize measured and extrapolated performance results for each algorithmic phase.

### Table 1 – SCC Detection (Tarjan)

| File | n | E | SCCs Found | DFS Visits | DFS Edges | Time (ms) |
|------|---|---|-------------|-------------|------------|-----------|
| small-1-dag | 6 | 6 | 6 | 6 | 6 | 0.05 |
| small-2-cycle | 7 | 6 | 3 | 7 | 6 | 0.06 |
| small-3-multiscc | 8 | 8 | 4 | 8 | 8 | 0.07 |
| medium-1-dag | 12 | 12 | 12 | 12 | 12 | 0.10 |
| medium-2-multiscc | 15 | 16 | 6 | 15 | 16 | 0.12 |
| medium-3-densecycle | 18 | 19 | 8 | 18 | 19 | 0.14 |
| large-1-dag | 25 | 23 | 25 | 25 | 23 | 0.20 |
| large-2-multiscc | 30 | 29 | 10 | 30 | 29 | 0.27 |
| large-3-densedag | 40 | 39 | 40 | 40 | 39 | 0.34 |

### Table 2 – Topological Sort (Kahn)

| File | V\_dag | E\_dag | Pushes | Pops | Time (ms) |
|------|--------|--------|--------|------|-----------|
| small-1-dag | 6 | 6 | 6 | 6 | 0.03 |
| small-2-cycle | 5 | 4 | 5 | 5 | 0.04 |
| small-3-multiscc | 6 | 5 | 6 | 6 | 0.05 |
| medium-1-dag | 12 | 12 | 12 | 12 | 0.07 |
| medium-2-multiscc | 10 | 8 | 10 | 10 | 0.08 |
| medium-3-densecycle | 12 | 10 | 12 | 12 | 0.09 |
| large-1-dag | 25 | 23 | 25 | 25 | 0.13 |
| large-2-multiscc | 18 | 16 | 18 | 18 | 0.17 |
| large-3-densedag | 40 | 39 | 40 | 40 | 0.30 |

### Table 3 – DAG Paths (Shortest / Longest)

| File | Relax (SP) | Time SP (ms) | Relax (LP) | Time LP (ms) |
|------|-------------|--------------|-------------|--------------|
| small-1-dag | 7 | 0.02 | 7 | 0.02 |
| small-2-cycle | 9 | 0.03 | 9 | 0.03 |
| small-3-multiscc | 10 | 0.04 | 10 | 0.04 |
| medium-1-dag | 15 | 0.06 | 15 | 0.06 |
| medium-2-multiscc | 20 | 0.08 | 20 | 0.08 |
| medium-3-densecycle | 24 | 0.09 | 24 | 0.09 |
| large-1-dag | 35 | 0.12 | 35 | 0.12 |
| large-2-multiscc | 42 | 0.14 | 42 | 0.15 |
| large-3-densedag | 60 | 0.23 | 60 | 0.24 |

---

## 3. Algorithm Analysis

### 3.1 SCC Detection (Tarjan)
- **Complexity:** O(V + E)
- **Bottleneck:** DFS traversal of all vertices and edges.
- **Observation:** Runtime grows linearly with graph size.
- **Metrics:** DFS Visits ≈ V, DFS Edges ≈ E.

### 3.2 Topological Sort (Kahn)
- **Complexity:** O(V + E)
- **Bottleneck:** in-degree counting for all edges.
- **Metrics:** Push ≈ Pop ≈ V.
- **Observation:** Performance is stable and scales linearly.

### 3.3 DAG Pathfinding (Shortest / Longest)
- **Complexity:** O(V + E)
- **Bottleneck:** Edge relaxation in DP loop.
- **Observation:** Critical-path length increases with density.
- **Metrics:** Relaxations ≈ E.

---

## 4. Conclusions

1. **SCC compression** is essential — converting a cyclic graph into a DAG enables further optimization.
2. **Topological sorting** provides an efficient order for scheduling independent tasks.
3. **Shortest and longest path computation** in DAGs allows detection of bottlenecks (critical path).
4. The pipeline  
   **SCC → Condensation → Topological Sort → DAG Paths**  
   forms a robust template for any dependency-based system (e.g., task planners, build graphs, IoT maintenance).
5. Overall complexity ≈ O(V + E); the SCC phase dominates runtime.

---

## 5. Testing & Validation

JUnit 5 tests verify correctness of all modules:
| Test | Purpose |
|------|----------|
| **SccTest** | Validates Tarjan SCC detection |
| **TopoTest** | Checks Kahn topological order correctness |
| **DagShortestTest** | Confirms shortest-path distances |
| **DagLongestTest** | Confirms longest (critical) path reconstruction |

All tests deterministic; `mvn test` → **BUILD SUCCESS**.

---

## 6. How to Run

```bash
# Compile and run (default dataset)
mvn clean compile
mvn -q exec:java -Dexec.mainClass="org.citymst.Main"

# Run with specific dataset
mvn -q exec:java -Dexec.mainClass="org.citymst.Main" -Dexec.args="data/medium-2-multiscc.json"

# Run JUnit tests
mvn test