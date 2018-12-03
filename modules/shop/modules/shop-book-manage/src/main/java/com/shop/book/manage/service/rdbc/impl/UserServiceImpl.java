package com.shop.book.manage.service.rdbc.impl;

import com.shop.book.manage.dao.UserDao;
import com.shop.book.manage.dao.mapper.UserMapper;
import com.shop.book.manage.enums.UserStatuEnum;
import com.shop.book.manage.model.pojo.User;
import com.shop.book.manage.model.pojo.UserExample;
import com.shop.book.manage.model.vo.UserQueryVo;
import com.shop.book.manage.model.vo.UserVo;
import com.shop.book.manage.service.rdbc.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDao userDao;

    public User getById(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return user;
    }

    public User queryByMobile(String mobile) {
        UserExample example= new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<User> list = userMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    public User addUser(User user) {
        userMapper.insertSelective(user);
        return user;
    }

    public Integer uptUser(User user) {
        int rt = userMapper.updateByPrimaryKeySelective(user);
        return rt;
    }

    //禁用
    public boolean disable(Long userId) {
        User uptUser = new User();
        uptUser.setId(userId);
        uptUser.setStatus(UserStatuEnum.FREEZE.getValue());
        uptUser.setModifiedTime(new Date());
        uptUser(uptUser);
        return true;
    }

    //启用
    public boolean enable(Long userId) {
        User uptUser = new User();
        uptUser.setId(userId);
        uptUser.setStatus(UserStatuEnum.NOMAL.getValue());
        uptUser.setModifiedTime(new Date());
        uptUser(uptUser);
        return true;
    }

    public List<UserVo> queryPage(UserQueryVo queryVo) {
        List<UserVo> list = userDao.queryPage(queryVo);
        if(list==null) {
            list= Collections.EMPTY_LIST;
        }
        return list;
    }

    //查询用户权限
    public Set<String> queryUserPerms(Long userId) {
        List<String> list = userDao.queryUserPerms(userId);
        if(list==null || list.size()<=0){
            return Collections.EMPTY_SET;
        }
        Set<String> permSet = new HashSet<String>();
        for (String perm : list) {
            if(StringUtils.isBlank(perm)) {
                continue;
            }
            if(perm.indexOf(",")>0){
                List perList = Arrays.asList(StringUtils.split(perm,","));
                permSet.addAll(perList);
            } else {
                permSet.add(perm);
            }
        }
        return permSet;
    }
}
