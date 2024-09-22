package com.boothu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boothu.model.pojo.User;

public interface UserService extends IService<User>  {

    public User queryByMobile(String mobile);

}
