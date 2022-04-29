package com.commonservice.invoke.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Cpu implements Serializable {

    @ApiModelProperty("CPU数")
    private int cpuNum;

    @ApiModelProperty("CPU总的使用率")
    private double total;

    @ApiModelProperty("CPU系统使用率")
    private double sys;

    @ApiModelProperty("CPU用户使用率")
    private double used;

    @ApiModelProperty("CPU当前等待率")
    private double wait;

    @ApiModelProperty("CPU当前空闲率")
    private double free;

}
