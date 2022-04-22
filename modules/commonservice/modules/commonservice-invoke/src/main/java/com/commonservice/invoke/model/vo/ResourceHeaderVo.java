package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel(description = "接口请求头", value = "resourceHeaderVo")
public class ResourceHeaderVo implements Serializable {

    private Long id;

    private Long resourceId;

    private String name;

    private String defaultValue;

    private String note;

    private Integer sort;

}
