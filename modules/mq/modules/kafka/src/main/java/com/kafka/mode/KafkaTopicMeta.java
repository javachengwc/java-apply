package com.kafka.mode;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;
import java.util.Set;

public class KafkaTopicMeta {

    private String name;

    private List<Integer> partitionIds;

    private Set<Integer> replicaIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPartitionIds() {
        return partitionIds;
    }

    public void setPartitionIds(List<Integer> partitionIds) {
        this.partitionIds = partitionIds;
    }

    public Set<Integer> getReplicaIds() {
        return replicaIds;
    }

    public void setReplicaIds(Set<Integer> replicaIds) {
        this.replicaIds = replicaIds;
    }

    public String toString() {

        return ToStringBuilder.reflectionToString(this);
    }
}
