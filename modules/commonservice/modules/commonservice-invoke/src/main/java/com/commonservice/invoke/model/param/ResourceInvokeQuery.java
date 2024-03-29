package com.commonservice.invoke.model.param;

import com.model.base.PageParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "resourceInvokeQuery", description = "调用查询参数")
public class ResourceInvokeQuery extends PageParam {

    private Long id;

    private Integer sysId;

    private String sysName;

    private Integer cateId;

    private Long resourceId;

    private String resourceName;

    private String resourceLink;

    private Integer isSuccess;

    private String invokeTimeBegin;

    private String invokeTimeEnd;

}
