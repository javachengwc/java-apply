package com.shop.user.api.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.Pattern;

@ApiModel(value = "loginVo", description = "登录信息")
public class LoginVo {

    public static final String CELL_PHONE = "^1\\d{10}$";

    @Pattern(regexp = CELL_PHONE, message = "手机号不对")
    @ApiModelProperty(name = "mobile", value = "手机号")
    private String mobile;

    @ApiModelProperty(name = "captcha", value = "验证码")
    private String captcha;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
