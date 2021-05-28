package com.manage.rbac.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "平台用户信息", value = "platUserVO")
public class PlatUserVO implements Serializable {

    @ApiModelProperty("平台账号uid")
    private Long uid;

    @ApiModelProperty("平台账号")
    private String account;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("昵称")
    private String nick;

}
