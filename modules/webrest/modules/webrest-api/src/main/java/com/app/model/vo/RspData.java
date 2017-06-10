package com.app.model.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RspData<T> {

    protected Integer result;

    protected String msg;

    protected T data;

    public RspData()
    {

    }

    public RspData(Integer result,String msg,T data)
    {
        this.result=result;
        this.msg=msg;
        this.data=data;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
