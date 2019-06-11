package com.bootmp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootmp.dao.mapper.UserMapper;
import com.bootmp.model.pojo.User;
import com.bootmp.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
