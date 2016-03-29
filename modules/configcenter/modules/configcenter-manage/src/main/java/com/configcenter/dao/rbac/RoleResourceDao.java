package com.configcenter.dao.rbac;

import com.configcenter.model.rbac.Resource;
import com.configcenter.model.rbac.Role;
import com.configcenter.model.rbac.RoleResource;

import java.util.List;

/**
 * 角色资源访问类
 */
public interface RoleResourceDao {

    public void add(RoleResource roleResource);

    public void delete(RoleResource roleResource);

    public void deleteByResource(Resource resource);

    public void deleteByRole(Role role);

    public List<RoleResource> queryByRole(Role role);

}
