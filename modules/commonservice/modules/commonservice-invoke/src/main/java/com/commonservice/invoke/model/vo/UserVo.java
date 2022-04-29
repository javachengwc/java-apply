package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("帐号状态（0正常 1停用） ")
    private Integer status;

    @ApiModelProperty("最后登录IP")
    private String loginIp;

    @ApiModelProperty("部门Id")
    private Long deptId;

}
