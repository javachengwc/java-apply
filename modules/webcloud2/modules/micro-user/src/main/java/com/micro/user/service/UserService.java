package com.micro.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.user.model.pojo.User;

public interface UserService extends IService<User> {

    //根据手机号查询用户
    public User queryByMobile(String mobile);

    //根据第三方账号查询用户
    public User queryByThirdAccount(String thirdAccount,Integer thirdType);
}
