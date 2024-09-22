package com.boothu.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boothu.dao.mapper.UserMapper;
import com.boothu.model.pojo.User;
import com.boothu.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public User queryByMobile(String mobile) {
        User user = new User();
        user.setMobile(mobile);
        Wrapper<User> wrapper = new QueryWrapper<User>(user);
        List<User> list = this.baseMapper.selectList(wrapper);
        if(list!=null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
