package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "接口请求参数", value = "resourceParamVo")
public class ResourceParamVo implements Serializable {

    private Long id;

    private Long resourceId;

    private String name;

    private String type;

    private Integer isMast;

    private String defaultValue;

    private String note;

    private Integer sort;
}
