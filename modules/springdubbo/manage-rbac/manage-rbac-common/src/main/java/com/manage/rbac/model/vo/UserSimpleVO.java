package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(description = "用户简单信息", value = "userSimpleVO")
public class UserSimpleVO {

    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("网名")
    private String nickname;

}
