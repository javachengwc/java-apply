package com.storefront.manage.service.rbac.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.base.model.Page;
import com.shop.base.util.TransUtil;
import com.storefront.manage.config.DbStorefrontConfig;
import com.storefront.manage.dao.rbac.UserDao;
import com.storefront.manage.dao.rbac.UserRoleDao;
import com.storefront.manage.dao.rbac.mapper.UserMapper;
import com.storefront.manage.enums.UserStatuEnum;
import com.storefront.manage.model.pojo.rbac.Role;
import com.storefront.manage.model.pojo.rbac.User;
import com.storefront.manage.model.pojo.rbac.UserExample;
import com.storefront.manage.model.vo.UserQueryVo;
import com.storefront.manage.model.vo.UserVo;
import com.storefront.manage.service.rbac.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

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

    //添加用户，附带加角色
    @Transactional(value = DbStorefrontConfig.MANAGE_TRANSACTION_MANAGER_NAME)
    public User addUserWithRole(UserVo userVo) {
        Date now = new Date();
        User user = TransUtil.transEntity(userVo,User.class);
        user.setCreateTime(now);
        user.setModifiedTime(now);

        this.addUser(user);
        Long userId =user.getId();

        List<Long> roleIds = userVo.getRoleIds();
        if(roleIds!=null && roleIds.size()>0) {
            userRoleDao.addUserRoles(userId,roleIds.toArray(new Long [roleIds.size()]));
        }
        return user;
    }

    public Integer uptUser(User user) {
        int rt = userMapper.updateByPrimaryKeySelective(user);
        return rt;
    }

    //修改用户，附带修改角色
    @Transactional(value = DbStorefrontConfig.MANAGE_TRANSACTION_MANAGER_NAME)
    public Integer uptUserWithRole(UserVo userVo) {
        Long userId = userVo.getId();
        Date now = new Date();
        User user = TransUtil.transEntity(userVo,User.class);
        user.setModifiedTime(now);
        user.setId(userId);
        Integer rt =this.uptUser(user);

        userRoleDao.deleteByUser(userId);
        List<Long> roleIds = userVo.getRoleIds();
        if(roleIds!=null && roleIds.size()>0) {
            userRoleDao.addUserRoles(userId,roleIds.toArray(new Long [roleIds.size()]));
        }
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

    public Page<UserVo> queryPage(UserQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        List<UserVo> list = userDao.queryPage(queryVo);
        PageInfo<UserVo> pageInfo = new PageInfo<UserVo>(list);
        int totalCnt = new Long(pageInfo.getTotal()).intValue();
        Page<UserVo> page = new Page<UserVo>();
        page.setList(list);
        page.setTotalCount(totalCnt);
        return page;
    }

    //查询用户权限
    public Set<String> queryUserPerms(Long userId) {
        Set<String> permSet = new HashSet<String>();
        List<String> list = userDao.queryUserPerms(userId);
        if(list==null || list.size()<=0){
            return permSet;
        }
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

    //查询用户的角色列表
    public List<Role> queryRoleByUser(Long userId) {
        List<Role> list =userRoleDao.queryRoleByUser(userId);
        return list;
    }
}
