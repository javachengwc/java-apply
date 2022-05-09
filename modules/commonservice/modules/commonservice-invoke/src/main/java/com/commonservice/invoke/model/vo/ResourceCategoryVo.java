package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description = "接口目录", value = "resourceCategoryVo")
public class ResourceCategoryVo implements Serializable {

    private Long id;

    private String name;

    private Long parentId;

    private String note;

    private Integer sort;

    private List<ResourceCategoryVo> children;

}
