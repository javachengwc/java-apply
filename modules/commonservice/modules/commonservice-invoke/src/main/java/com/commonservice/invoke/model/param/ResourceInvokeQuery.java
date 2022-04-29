package com.commonservice.invoke.model.param;

import com.model.base.PageParam;
import lombok.Data;

@Data
public class ResourceInvokeQuery extends PageParam {

    private Long id;

    private Integer sysId;

    private String sysName;

    private Integer cateId;

    private Long resourceId;

    private String resourceName;

    private String resourceLink;

    private Integer isSuccess;

    private String createTimeBegin;

    private String createTimeEnd;

}
