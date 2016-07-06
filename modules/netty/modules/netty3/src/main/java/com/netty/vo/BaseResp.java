package com.netty.vo;

public class BaseResp {

    private Integer code = 0;
    private String description = "SUCCESS";

    public BaseResp() {
    }

    public BaseResp(Integer code,String description)
    {
        this.code=code;
        this.description=description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
