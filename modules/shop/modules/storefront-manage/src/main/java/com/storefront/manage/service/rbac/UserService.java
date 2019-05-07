package com.storefront.manage.service.rbac;

import com.shop.base.model.Page;
import com.storefront.manage.model.pojo.rbac.Role;
import com.storefront.manage.model.pojo.rbac.User;
import com.storefront.manage.model.vo.UserQueryVo;
import com.storefront.manage.model.vo.UserVo;

import java.util.List;
import java.util.Set;

public interface UserService {

    public User getById(Long userId);

    public User queryByMobile(String mobile);

    public User addUser(User user);

    //添加用户，附带加角色
    public User addUserWithRole(UserVo userVo);

    public Integer uptUser(User user);

    //修改用户，附带修改角色
    public Integer uptUserWithRole(UserVo userVo);

    //禁用
    public boolean disable(Long userId);

    //启用
    public boolean enable(Long userId);

    public Page<UserVo> queryPage(UserQueryVo queryVo);

    //查询用户权限
    public Set<String> queryUserPerms(Long userId);

    //查询用户的角色列表
    public List<Role> queryRoleByUser(Long userId);
}
