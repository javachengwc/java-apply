package com.kafka.mode;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class KafkaPartition {

    private int id;

    private KafkaBroker leader;

    private List<KafkaBroker> replicas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KafkaBroker getLeader() {
        return leader;
    }

    public void setLeader(KafkaBroker leader) {
        this.leader = leader;
    }

    public List<KafkaBroker> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<KafkaBroker> replicas) {
        this.replicas = replicas;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
