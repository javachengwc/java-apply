package com.micro.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.user.dao.plus.UserPlusMapper;
import com.micro.user.model.pojo.User;
import com.micro.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserPlusMapper, User> implements UserService {

    public User queryByMobile(String mobile) {
        User user =this.baseMapper.selectOne(new QueryWrapper<User>().eq("mobile",mobile));
        return user;
    }
}
