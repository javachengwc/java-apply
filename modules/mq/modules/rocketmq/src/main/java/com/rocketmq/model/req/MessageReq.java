package com.rocketmq.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "messageReq", description = "消息")
public class MessageReq {

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("类型")
    private Integer type;
}
