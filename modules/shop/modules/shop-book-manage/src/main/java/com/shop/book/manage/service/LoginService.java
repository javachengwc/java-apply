package com.shop.book.manage.service;

import com.shop.base.model.Resp;
import com.shop.book.manage.model.vo.LoginResultVo;
import com.shop.book.manage.model.vo.LoginVo;

public interface LoginService {

    public Resp<LoginResultVo> login(LoginVo loginVo);

    public boolean logout();
}
