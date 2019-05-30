package com.micro.user.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(description = "用户钱包信息", value = "userWalletVo")
@Data
public class UserWalletVo {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    private String userName;

    private String userMobile;

    @ApiModelProperty("金额")
    private Long amount;

    @ApiModelProperty("冻结金额")
    private Long freezeAmount;

    private Date createTime;
}
