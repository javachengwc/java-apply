package com.manage.rbac.model.param;

import com.manage.rbac.model.vo.CrowdSimpleVO;
import com.manage.rbac.model.vo.PostSimpleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(description = "用户参数", value = "userReq")
public class UserReq {

    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("平台账号id")
    private Long uid;

    @ApiModelProperty("平台账号")
    private String account;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("网名")
    private String nickname;

    @ApiModelProperty("上级id")
    private Integer superiorId;

    @ApiModelProperty("机构id")
    private Integer orgId;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("岗位列表")
    private List<PostSimpleVO> postList= Collections.EMPTY_LIST;

    @ApiModelProperty("用户组列表")
    private List<CrowdSimpleVO> crowdList= Collections.EMPTY_LIST;
}
