package com.app.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AccessLog {

    private String methodType;

    private String path;

    private String queryValue;

    private Integer status;

    private Long costTime;

    private String traceId;

    private String remoteAddr;

    private String xForwardFor;

    public AccessLog() {}

    public AccessLog(String methodType,String path, String queryValue,Integer status,Long costTime,
                     String traceId, String remoteAddr, String xForwardFor) {
        this.methodType=methodType;
        this.path=path;
        this.queryValue=queryValue;
        this.status=status;
        this.costTime=costTime;
        this.traceId=traceId;
        this.remoteAddr=remoteAddr;
        this.xForwardFor=xForwardFor;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getxForwardFor() {
        return xForwardFor;
    }

    public void setxForwardFor(String xForwardFor) {
        this.xForwardFor = xForwardFor;
    }

    public String toString() {

        return ToStringBuilder.reflectionToString(this);
    }
}
