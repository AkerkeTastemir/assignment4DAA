package org.citymst.graph.io;

import org.citymst.graph.model.Graph;
import org.citymst.graph.model.GraphBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;

public class JsonDataReader {

    public static class Loaded {
        public final Graph graph;
        public final Integer source;       // может быть null
        public final String weightModel;   // "edge" | "node" | null
        public Loaded(Graph g, Integer s, String wm) { this.graph = g; this.source = s; this.weightModel = wm; }
    }

    public static Loaded loadSingleGraph(Path file) throws Exception {
        String jsonText = Files.readString(file);
        JSONObject root = new JSONObject(jsonText);

        boolean directed = root.optBoolean("directed", true);
        int n = root.getInt("n");
        Graph g = directed ? GraphBuilder.directed(n) : GraphBuilder.undirected(n);

        JSONArray edges = root.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            int u = e.getInt("u");
            int v = e.getInt("v");
            int w = e.optInt("w", 1);
            g.addEdge(u, v, w);
        }

        Integer source = root.has("source") ? root.getInt("source") : null;
        String weightModel = root.optString("weight_model", "edge");
        return new Loaded(g, source, weightModel);
    }
}