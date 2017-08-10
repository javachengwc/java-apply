package com.app.entity;

import java.util.Map;

public class MetricsStat {

    private Map<String, Long> counters;

    private Map<String, Double> gauges;

    public Map<String, Long> getCounters() {
        return counters;
    }

    public void setCounters(Map<String, Long> counters) {
        this.counters = counters;
    }

    public Map<String, Double> getGauges() {
        return gauges;
    }

    public void setGauges(Map<String, Double> gauges) {
        this.gauges = gauges;
    }
}
