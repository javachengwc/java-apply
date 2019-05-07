package com.storefront.manage.dao.rbac;

import com.storefront.manage.model.vo.UserQueryVo;
import com.storefront.manage.model.vo.UserVo;

import java.util.List;

public interface UserDao {

    public List<UserVo> queryPage(UserQueryVo queryVo);

    public List<String> queryUserPerms(Long userId);

}
