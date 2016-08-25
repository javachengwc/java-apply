package com.mountain.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 服务消费类
 */
public class Consumer implements Serializable{

    //消费id
    private String id;

    //应用名
    private String application;

    //消费的服务id
    private String sid;

    //服务对应的thrift接口
    private String api;

    //消费的服务版本
    private String version = "1.0";

    //请求服务超时时间(毫秒)
    private Integer timeout;

    //注册中心id
    private String registry;

    //消费责任人
    private String owner;

    //直接服务地址, 如果有值,则不去注册中心获取服务地址, 而是直接请求该地址
    private String directurl;

    public Consumer()
    {

    }

    public Consumer(String id,String application, String sid,String api,String version ,Integer timeout,String registry, String owner,String directurl)
    {
        this.id=id;
        this.application=application;
        this.sid=sid;
        this.api=api;
        this.version=version;
        this.timeout=timeout;
        this.registry=registry;
        this.owner=owner;
        this.directurl=directurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
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

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDirecturl() {
        return directurl;
    }

    public void setDirecturl(String directurl) {
        this.directurl = directurl;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
