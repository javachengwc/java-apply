package com.kafka.mode;

import org.apache.commons.lang.builder.ToStringBuilder;

public class KafkaBroker {

    private int id;

    private String host;

    private int port;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
