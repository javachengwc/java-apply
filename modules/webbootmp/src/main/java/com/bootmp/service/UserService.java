package com.bootmp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bootmp.model.pojo.User;

public interface UserService extends IService<User>  {

    public User queryByMobile(String mobile);

}
