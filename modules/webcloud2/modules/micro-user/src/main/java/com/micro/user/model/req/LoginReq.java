package com.micro.user.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "loginReq", description = "登录请求")
public class LoginReq {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private  String passwd;

    @ApiModelProperty("登录方式 0--账号密码，1--手机验证码，2--第三方账号")
    private Integer loginType=0;

    @ApiModelProperty("第三方登录下哪个第三方 0--支付宝 1--微信,2--qq")
    private Integer thirdType=0;

}
