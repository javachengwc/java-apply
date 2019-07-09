package com.micro.user.service;

import com.micro.user.model.pojo.User;
import com.micro.user.model.req.LoginReq;
import com.micro.user.model.vo.LoginVo;

public interface LoginService {

    public LoginVo login(User user, LoginReq loginReq);

    public boolean checkLoginByToken(String token);
}
