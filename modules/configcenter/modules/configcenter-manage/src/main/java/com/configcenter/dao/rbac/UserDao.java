package com.configcenter.dao.rbac;

import com.configcenter.dao.IDao;
import com.configcenter.model.rbac.Resource;
import com.configcenter.model.rbac.User;

import java.util.List;
import java.util.Map;

/**
 * 用户访问类
 */
public interface UserDao extends IDao<User> {

    public User getByAccount(User user);

    public List<Resource> queryUserResource(Map<String,Object> param);
}
