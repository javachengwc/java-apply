package com.manage.rbac.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "用户信息", value = "userVO")
public class UserVO {

    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("平台账号id")
    private Long uid;

    @ApiModelProperty("平台账号")
    private String account;
    
    @ApiModelProperty("平台账号昵称")
    private String nick;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("网名")
    private String nickname;

    @ApiModelProperty("状态，0:正常、1:禁用")
    private Integer state;

    @ApiModelProperty("上级id")
    private Integer superiorId;

    @ApiModelProperty("上级姓名")
    private String superiorName;

    @ApiModelProperty("上级网名")
    private String superiorNickname;

    @ApiModelProperty("机构id")
    private Integer orgId;

    @ApiModelProperty("机构名称")
    private String orgName;

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

    @ApiModelProperty("逻辑删除 1--删除，0--不删除")
    private Integer disable;

    @ApiModelProperty("是否超级管理员 0--否，1--是")
    private Integer superUser;

    @ApiModelProperty("岗位列表")
    private List<PostSimpleVO> postList= Collections.EMPTY_LIST;

    @ApiModelProperty("用户组列表")
    private List<CrowdSimpleVO> crowdList= Collections.EMPTY_LIST;
}
