package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@ApiModel(description = "用户登录信息", value = "userLoginVo")
@Data
public class UserLoginVo implements Serializable {

    private UserVo user;

    private Set<String> roles;

    private Set<String> permissions;
}
