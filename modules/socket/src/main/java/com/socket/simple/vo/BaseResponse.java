package com.socket.simple.vo;


public class BaseResponse {

    protected   int serialId;

    protected  Object ret;

    protected  long serverTime;

    public BaseResponse()
    {

    }

    public BaseResponse(int serialId)
    {
        this.serialId=serialId;
    }

    public BaseResponse(int serialId,Object ret)
    {
        this.serialId=serialId;
        this.ret=ret;
    }

    public int getSerialId() {
        return serialId;
    }

    public void setSerialId(int serialId) {
        this.serialId = serialId;
    }

    public Object getRet() {
        return ret;
    }

    public void setRet(Object ret) {
        this.ret = ret;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
