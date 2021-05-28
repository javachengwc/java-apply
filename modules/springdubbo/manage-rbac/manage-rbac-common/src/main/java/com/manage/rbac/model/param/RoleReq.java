package com.manage.rbac.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "角色参数", value = "roleReq")
public class RoleReq {

    @ApiModelProperty("角色id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("角色类型(1--业务角色 2--管理角色)")
    private Integer type;

    @ApiModelProperty("是否系统角色(0--否 1--是)")
    private Integer sysRole;

    @ApiModelProperty("备注")
    private String remark;

}
