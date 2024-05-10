package com.sharding.junior.service;

import com.sharding.junior.model.entity.User;
import com.sharding.junior.model.vo.UserVo;

public interface UserService {

    User getById(Long id);

    User addUser(UserVo userVo);
}
