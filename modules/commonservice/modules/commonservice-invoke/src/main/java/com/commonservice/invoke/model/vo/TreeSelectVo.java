package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TreeSelectVo implements Serializable {

    @ApiModelProperty("节点ID")
    private Long id;

    @ApiModelProperty("节点名称")
    private String label;

    @ApiModelProperty("子节点")
    private List<TreeSelectVo> children;
}
