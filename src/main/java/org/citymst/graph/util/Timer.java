package org.citymst.graph.util;

public final class Timer {
    private Timer(){}
    public static String fmtNanos(long ns){
        if (ns < 1_000) return ns + " ns";
        if (ns < 1_000_000) return (ns/1_000) + " µs";
        if (ns < 1_000_000_000) return (ns/1_000_000) + " ms";
        return String.format("%.3f s", ns/1_000_000_000.0);
    }
}