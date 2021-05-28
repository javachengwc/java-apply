package com.manage.rbac.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "菜单信息", value = "menuVO")
public class MenuVO {

    @ApiModelProperty("菜单id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态，0:正常、1:禁用，默认值:0")
    private Integer state;

    @ApiModelProperty("系统id")
    private Integer systemId;

    @ApiModelProperty("系统名称")
    private String systemName;

    @ApiModelProperty("父菜单id")
    private Integer parentId;

    @ApiModelProperty("父菜单名称")
    private String parentName;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("权限")
    private String perms;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("菜单或按钮标识")
    private String tag;

    @ApiModelProperty("类型(0--目录、1--菜单、2--按钮)")
    private Integer type;

    @ApiModelProperty("排序")
    private Integer sort;

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
