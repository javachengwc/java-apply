package com.kafka.mode;

import org.apache.commons.lang.builder.ToStringBuilder;

public class KafkaPartitionOffset {

    private int id;

    private long earliest;

    private long latest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getEarliest() {
        return earliest;
    }

    public void setEarliest(long earliest) {
        this.earliest = earliest;
    }

    public long getLatest() {
        return latest;
    }

    public void setLatest(long latest) {
        this.latest = latest;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
