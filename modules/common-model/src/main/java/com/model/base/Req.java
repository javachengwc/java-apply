package com.model.base;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "req", description = "请求")
@Data
public class Req<T> implements Serializable {

    @ApiModelProperty("请求头")
    private ReqHeader header= new ReqHeader();

    @ApiModelProperty("数据")
    private T data;

    public Req() {

    }

    public Req(T data) {
        this.data=data;
    }
}

