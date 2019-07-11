package com.micro.user.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "pwdReq", description = "修改或重置密码")
public class PwdReq {

    @ApiModelProperty("重置密码时的手机号")
    private String mobile;

    @ApiModelProperty("重置密码时的验证码")
    private String code;

    @ApiModelProperty("修改密码时的老密码")
    private String oldPasswd;

    @ApiModelProperty("密码")
    private String passwd;

    @ApiModelProperty("确认的密码")
    private String confirmPasswd;

    private Long userId;

}
