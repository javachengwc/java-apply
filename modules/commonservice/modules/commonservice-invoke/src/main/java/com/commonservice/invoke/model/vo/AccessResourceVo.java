package com.commonservice.invoke.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
@ApiModel(description = "访问接口", value = "accessResourceVo")
public class AccessResourceVo implements Serializable {

    private Long id;

    private String name;

    private String note;

    private Long sysId;

    private String sysName;

    private Long cateId;

    private String cateName;

    private String httpMethod;

    private String contentType;

    private String resourceLink;

    private Integer analysisFlag;

    private String reqDemo;

    private String respDemo;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    @ApiModelProperty("接口请求头列表")
    private List<ResourceHeaderVo> headerList;

    @ApiModelProperty("接口请求参数列表")
    private List<ResourceParamVo> paramList;
}
