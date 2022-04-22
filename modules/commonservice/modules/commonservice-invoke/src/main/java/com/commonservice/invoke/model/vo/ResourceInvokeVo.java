package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "接口调用记录", value = "resourceInvokeVo")
public class ResourceInvokeVo implements Serializable {

    private Long id;

    private Long resourceId;

    private String resourceName;

    private String resourceLink;

    private String httpMethod;

    private String contentType;

    private String reqHeader;

    private String reqData;

    private Integer respCode;

    private String respData;

    private Integer isSuccess;

    private String errorMessage;

    private Date createTime;

    private Date modifyTime;
}
