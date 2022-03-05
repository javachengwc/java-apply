package com.es.consumer.activity.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityIndex implements Serializable {

    private String id;

    @ApiModelProperty("日期")
    private String statDate;

    @ApiModelProperty("天时")
    private String hour;

    @ApiModelProperty("活动来源")
    private String source;

    @ApiModelProperty("用户uid")
    private String uid;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("终端设备类型,0--pc,1--android,2--ios")
    private Integer appid;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("imei设备号")
    private String imei;
}
