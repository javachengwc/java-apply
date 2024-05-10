package com.sharding.junior.service.impl;

import com.sharding.junior.dao.mapper.UserMapper;
import com.sharding.junior.model.entity.User;
import com.sharding.junior.model.vo.UserVo;
import com.sharding.junior.service.UserService;
import com.tool.util.TransUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Long id) {
      User user= userMapper.selectById(id);
      return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(UserVo userVo) {
        Date now = new Date();
        User user = TransUtil.transEntity(userVo,User.class);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userMapper.insert(user);
        Long id = user.getId();
        logger.info("UserServiceImpl addUser, id={},userName={}",id,user.getUserName());
        user = userMapper.selectById(id);
        return user;
    }

}
