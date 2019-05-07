package com.storefront.manage.service;

import com.shop.base.model.Resp;
import com.storefront.manage.model.vo.LoginResultVo;
import com.storefront.manage.model.vo.LoginVo;

public interface LoginService {

    public Resp<LoginResultVo> login(LoginVo loginVo);

    public boolean logout();
}
