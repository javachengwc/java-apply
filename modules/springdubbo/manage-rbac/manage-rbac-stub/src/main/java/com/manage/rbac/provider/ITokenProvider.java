package com.manage.rbac.provider;

import com.manage.rbac.model.dto.UserDTO;
import com.model.base.Resp;

public interface ITokenProvider {

    //检查是否登陆
    public Resp<Boolean> checkLogin(String token);

    //根据token获取登陆账号信息
    public Resp<UserDTO> getUserByToken(String token);

}
