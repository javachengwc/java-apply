package com.netty.keepalive;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 心跳消息
 */
public class KeepaliveMessage implements Serializable{

    //心跳请求code
    public static int REQ_CODE=1;

    //心跳回应code
    public static int RSP_CODE=2;

    private String msg ;

    private String sn;

    private Integer reqCode;

    public KeepaliveMessage()
    {

    }

    public KeepaliveMessage(String msg)
    {
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getReqCode() {
        return reqCode;
    }

    public void setReqCode(Integer reqCode) {
        this.reqCode = reqCode;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
