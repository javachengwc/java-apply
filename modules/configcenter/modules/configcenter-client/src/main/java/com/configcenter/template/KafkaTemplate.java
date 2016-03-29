package com.configcenter.template;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * kafka模板
 */
public class KafkaTemplate implements Serializable {

    private String broker;

    private String zookeeper;

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
