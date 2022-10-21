package com.commonservice.invoke.model.param;

import com.model.base.PageParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "accessLogQuery", description = "调用日志参数")
public class AccessLogQuery  extends PageParam {

    private Integer sysId;

    private Long resourceId;

    private String resourcePath;

    private String invokeTimeBegin;

    private String invokeTimeEnd;

    private Long minCost;

    private Long maxCost;
}
