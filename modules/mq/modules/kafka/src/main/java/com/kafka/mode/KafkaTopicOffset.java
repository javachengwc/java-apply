package com.kafka.mode;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class KafkaTopicOffset {

    private String name;

    private List<KafkaPartitionOffset> kafkaPartitionOffsets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KafkaPartitionOffset> getKafkaPartitionOffsets() {
        return kafkaPartitionOffsets;
    }

    public void setKafkaPartitionOffsets(
            List<KafkaPartitionOffset> kafkaPartitionOffsets) {
        this.kafkaPartitionOffsets = kafkaPartitionOffsets;
    }

    public String toString() {
        return  new StringBuffer("name: ").append(name)
        .append(" partitionsOffsets: ").append(ArrayUtils.toString(kafkaPartitionOffsets))
        .toString();
    }
}
