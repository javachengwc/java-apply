package com.kafka.mode;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class KafkaTopic {

    private String name;

    private List<KafkaPartition> partitions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KafkaPartition> getPartitions() {
        return partitions;
    }

    public void setPartitions(List<KafkaPartition> partitions) {
        this.partitions = partitions;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
