package com.configcenter.dao.rbac;

import com.configcenter.model.rbac.Role;
import com.configcenter.model.rbac.TagRelaUserOrRole;
import com.configcenter.model.rbac.User;

/**
 * 权限标签与用户或着角色的关联访问类
 */
public interface TagRelaUserOrRoleDao {

    public void add(TagRelaUserOrRole tagRelaUserOrRole);

    public void delete(TagRelaUserOrRole tagRelaUserOrRole);

    public void deleteByUser(User user);

    public void deleteByRole(Role role);
}
