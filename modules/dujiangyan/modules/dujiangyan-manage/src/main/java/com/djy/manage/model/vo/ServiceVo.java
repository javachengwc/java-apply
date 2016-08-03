package com.djy.manage.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 服务信息
 */
public class ServiceVo implements Serializable {

    private Integer id;

    private Integer pid;

    //服务
    private String service;

    //所属应用
    private String application;

    //服务url
    private String url;

    //服务参数
    private String parameters;

    //服务地址
    private String address;

    //服务权重
    private Integer weight;

    //启用或禁用
    private boolean useable=true;

    //服务用户者
    private String owner;

    //服务备注
    private String note;

    //调用成功数
    private Long invokeSuccessCnt;

    //调用失败数
    private Long invokeFailCnt;

    //总的调用成功数
    private Long invokeSucTotalCnt;

    //总的调用失败数
    private Long invokeFailTotalCnt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public boolean isUseable() {
        return useable;
    }

    public void setUseable(boolean useable) {
        this.useable = useable;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getInvokeSuccessCnt() {
        return invokeSuccessCnt;
    }

    public void setInvokeSuccessCnt(Long invokeSuccessCnt) {
        this.invokeSuccessCnt = invokeSuccessCnt;
    }

    public Long getInvokeFailCnt() {
        return invokeFailCnt;
    }

    public void setInvokeFailCnt(Long invokeFailCnt) {
        this.invokeFailCnt = invokeFailCnt;
    }

    public Long getInvokeSucTotalCnt() {
        return invokeSucTotalCnt;
    }

    public void setInvokeSucTotalCnt(Long invokeSucTotalCnt) {
        this.invokeSucTotalCnt = invokeSucTotalCnt;
    }

    public Long getInvokeFailTotalCnt() {
        return invokeFailTotalCnt;
    }

    public void setInvokeFailTotalCnt(Long invokeFailTotalCnt) {
        this.invokeFailTotalCnt = invokeFailTotalCnt;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
