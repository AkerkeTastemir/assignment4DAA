package org.citymst.graph.model;

public final class GraphBuilder {
    private GraphBuilder() {}
    public static Graph directed(int n) { return new Graph(n, true); }
    public static Graph undirected(int n) { return new Graph(n, false); }
}