package com.djy.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 服务信息类
 */
public class Provider implements Serializable{

    //服务唯一标示
    private String id;

    //服务名称(其实就是服务标示)
    private String serviceName;

    //服务对应的thrift接口
    private String api;

    //服务的端口
    private Integer port;

    //服务版本
    private String version;

    //服务超时时间(毫秒)
    private Integer timeout;

    //服务线程数
    private Integer threads;

    public Provider()
    {

    }

    public Provider(String id, String api, Integer port, String version, Integer timeout, Integer threads)
    {
        this.id=id;
        this.serviceName=id;
        this.api=api;
        this.port=port;
        this.version=version;
        this.timeout=timeout;
        this.threads=threads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
