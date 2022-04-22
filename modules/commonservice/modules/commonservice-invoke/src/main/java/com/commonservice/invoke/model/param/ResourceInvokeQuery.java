package com.commonservice.invoke.model.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ResourceInvokeQuery implements Serializable {

    private Long id;

    private Integer sysId;

    private Integer cateId;

    private Long resourceId;

    private String resourceLink;

    private Integer isSuccess;

    private String createTimeBegin;

    private String createTimeEnd;

}
