package com.manage.rbac.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "机构信息", value = "orgVO")
public class OrgVO {

    @ApiModelProperty("机构id")
    private Integer id;

    @ApiModelProperty("机构名称")
    private String name;

    @ApiModelProperty("机构人数")
    private Integer personCount=0;

    @ApiModelProperty("状态，0:正常、1:禁用")
    private Integer state;

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

    @ApiModelProperty("岗位列表")
    private List<PostSimpleVO> postList= Collections.EMPTY_LIST;

    @ApiModelProperty("用户组列表")
    private List<CrowdSimpleVO> crowdList= Collections.EMPTY_LIST;
}
