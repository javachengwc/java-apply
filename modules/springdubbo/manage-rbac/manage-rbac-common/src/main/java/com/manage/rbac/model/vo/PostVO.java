package com.manage.rbac.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "岗位信息", value = "postVO")
public class PostVO {

    @ApiModelProperty("岗位id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("状态，0:正常、1:禁用")
    private Integer state;

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

    @ApiModelProperty("机构列表")
    private List<OrgSimpleVO> orgList= Collections.EMPTY_LIST;

    @ApiModelProperty("角色列表")
    private List<RoleSimpleVO> roleList= Collections.EMPTY_LIST;
}
