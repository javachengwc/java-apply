package com.configcenter.dao.rbac;

import com.configcenter.model.rbac.Role;
import com.configcenter.model.rbac.User;
import com.configcenter.model.rbac.UserRole;

import java.util.List;

/**
 * 用户角色访问类
 */
public interface UserRoleDao {

    public void add(UserRole userRole);

    public void delete(UserRole userRole);

    public void deleteByUser(User user);

    public void deleteByRole(Role role);

    public List<UserRole> queryByUser(User user);
}
