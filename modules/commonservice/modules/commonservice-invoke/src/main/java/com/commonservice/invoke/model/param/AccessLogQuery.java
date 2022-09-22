package com.commonservice.invoke.model.param;

import com.model.base.PageParam;
import lombok.Data;

@Data
public class AccessLogQuery  extends PageParam {

    private Integer sysId;

    private String sysName;

    private Long resourceId;

    private String resourceName;

    private String resourcePath;

    private String invokeTimeBegin;

    private String invokeTimeEnd;

}
