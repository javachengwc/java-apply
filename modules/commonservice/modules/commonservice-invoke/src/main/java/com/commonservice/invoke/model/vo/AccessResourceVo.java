package com.commonservice.invoke.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@ApiModel(description = "访问资源", value = "accessResourceVo")
public class AccessResourceVo implements Serializable {

    private Long id;

    private String name;

    private String note;

    private Integer sysId;

    private Integer cateId;

    private String httpMethod;

    private String contentType;

    private String resourceLink;

    private String header;

    private String reqDemo;

    private String respDemo;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;
}
