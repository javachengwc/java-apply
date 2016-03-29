package com.configcenter.template;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 模板类
 */
public class BaseTemplate implements Serializable{

    protected String ip;

    protected String port;

    protected String type;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
