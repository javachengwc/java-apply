package com.commonservice.push.model;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "umengResp", description = "友盟返回结果")
public class UmengResp<T> {

    public static String SUCCESS="SUCCESS";

    public static String FAIL="FAIL";

    private  String ret;

    private T data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
