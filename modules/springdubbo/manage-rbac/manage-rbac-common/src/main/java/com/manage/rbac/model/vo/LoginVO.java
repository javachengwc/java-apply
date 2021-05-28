package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "loginVo", description = "登录信息")
@Data
public class LoginVO {

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("密码")
    private String passwd;

}
