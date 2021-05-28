package com.manage.rbac.provider.impl;

import com.model.base.Resp;
import com.manage.rbac.model.common.Constant;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.provider.ITokenProvider;
import com.manage.rbac.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = Constant.DUBBO_API_VERSION)
public class TokenProviderImpl implements ITokenProvider {

    @Autowired
    private IUserService userService;

    //检查是否登陆
    @Override
    public Resp<Boolean> checkLogin(String token) {
        if(StringUtils.isBlank(token)) {
            return Resp.data(Boolean.FALSE);
        }
        return Resp.data(Boolean.TRUE);
    }

    //根据token获取登陆账号信息
    @Override
    public Resp<UserDTO> getUserByToken(String token) {
        if(StringUtils.isBlank(token)) {
            return Resp.success();
        }
        return Resp.data(null);
    }
}
