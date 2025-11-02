package org.citymst.graph.metrics;

public class AlgorithmMetrics implements Metrics {
    private long start, elapsed;
    private long dfsVisit, dfsEdge, push, pop, relax;

    @Override public void start() { start = System.nanoTime(); }
    @Override public void stop()  { elapsed = System.nanoTime() - start; }
    @Override public long nanos() { return elapsed; }

    @Override public void incDfsVisit() { dfsVisit++; }
    @Override public void incDfsEdge()  { dfsEdge++; }
    @Override public long dfsVisits()   { return dfsVisit; }
    @Override public long dfsEdges()    { return dfsEdge; }

    @Override public void incPush() { push++; }
    @Override public void incPop()  { pop++; }
    @Override public long pushes()  { return push; }
    @Override public long pops()    { return pop; }

    @Override public void incRelax() { relax++; }
    @Override public long relaxations() { return relax; }

    @Override public void reset() {
        start = 0; elapsed = 0;
        dfsVisit = dfsEdge = push = pop = relax = 0;
    }
}