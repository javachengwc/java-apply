package com.manage.rbac.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "角色信息", value = "roleVO")
public class RoleVO {

    @ApiModelProperty("角色id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("状态，0:正常、1:禁用")
    private Integer state;

    @ApiModelProperty("角色类型(1--业务角色 2--管理角色)")
    private Integer type;

    @ApiModelProperty("是否系统角色(0--否 1--是)")
    private Integer sysRole;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人id")
    private Integer createrId;

    @ApiModelProperty("创建人网名")
    private String createrNickname;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    @ApiModelProperty("操作人id")
    private Integer operatorId;

    @ApiModelProperty("操作人网名")
    private String operatorNickname;
}
