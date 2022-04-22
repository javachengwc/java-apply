package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "接口系统", value = "resourceSystemVo")
public class ResourceSystemVo implements Serializable {

    private Long id;

    private String name;

    private String note;

    private Integer sort;
}
