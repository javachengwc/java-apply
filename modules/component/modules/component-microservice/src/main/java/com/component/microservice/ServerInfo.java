package com.component.microservice;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ServerInfo {

    //服务id
    private volatile String id;

    //服务名称
    private String name;

    //服务类别
    private Integer type;

    //服务组
    private String groupName;

    //服务实例编号
    private String instanceNo;

    //服务实例ip
    private String instanceIp;

    //服务实例端口
    private Integer instancePort;

    private Boolean aliveFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInstanceNo() {
        return instanceNo;
    }

    public void setInstanceNo(String instanceNo) {
        this.instanceNo = instanceNo;
    }

    public String getInstanceIp() {
        return instanceIp;
    }

    public void setInstanceIp(String instanceIp) {
        this.instanceIp = instanceIp;
    }

    public Integer getInstancePort() {
        return instancePort;
    }

    public void setInstancePort(Integer instancePort) {
        this.instancePort = instancePort;
    }

    public Boolean getAliveFlag() {
        return aliveFlag;
    }

    public void setAliveFlag(Boolean aliveFlag) {
        this.aliveFlag = aliveFlag;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
