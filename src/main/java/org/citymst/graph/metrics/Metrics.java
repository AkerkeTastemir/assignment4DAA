package org.citymst.graph.metrics;

public interface Metrics {
    void start();
    void stop();
    long nanos();

    // SCC
    void incDfsVisit();
    void incDfsEdge();
    long dfsVisits();
    long dfsEdges();

    // Kahn (topo)
    void incPush();
    void incPop();
    long pushes();
    long pops();

    // DAG shortest/longest
    void incRelax();
    long relaxations();

    void reset(); // обнулить счётчики/таймер для следующего алгоритма
}