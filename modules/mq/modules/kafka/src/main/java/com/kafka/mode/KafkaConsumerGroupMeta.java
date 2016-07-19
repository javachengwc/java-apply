package com.kafka.mode;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class KafkaConsumerGroupMeta {

    private String name;

    private List<String> owners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
