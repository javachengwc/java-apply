package com.micro.user.service;

import com.micro.user.model.pojo.User;
import com.micro.user.model.req.PwdReq;

public interface UserAccountService {

    //修改密码
    public boolean modifyPasswd(User user,PwdReq pwdReq);

    //验证账号密码
    public boolean checkAccount(User user, String passwd);
}
