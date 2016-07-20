package com.kafka.mode;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class KafkaConsumerGroup {

    private String name;

    private Integer topicCnt;

    private List<KafkaTopicOffset> owners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTopicCnt() {
        return topicCnt;
    }

    public void setTopicCnt(Integer topicCnt) {
        this.topicCnt = topicCnt;
    }

    public List<KafkaTopicOffset> getOwners() {
        return owners;
    }

    public void setOwners(List<KafkaTopicOffset> owners) {
        this.owners = owners;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
