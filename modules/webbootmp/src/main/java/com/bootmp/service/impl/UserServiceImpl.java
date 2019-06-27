package com.bootmp.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootmp.dao.mapper.UserMapper;
import com.bootmp.model.pojo.User;
import com.bootmp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public User queryByMobile(String mobile) {
        User user = new User();
        user.setMobile(mobile);
        Wrapper<User> wrapper = new QueryWrapper<User>(user);
        List<User> list = this.baseMapper.selectList(wrapper);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }
}
