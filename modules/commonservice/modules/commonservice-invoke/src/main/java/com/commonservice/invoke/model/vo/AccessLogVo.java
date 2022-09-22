package com.commonservice.invoke.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "接口日志", value = "accessLogVo")
public class AccessLogVo implements Serializable {

    private Long id;

    private Long sysId;

    private Long resourceId;

    private String resourcePath;

    @ApiModelProperty("调用时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date invokeTime;

    @ApiModelProperty("返回时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date returnTime;

    private Long cost;
}
