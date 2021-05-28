package com.manage.rbac.dao.ext;

import com.manage.rbac.entity.User;
import com.manage.rbac.entity.ext.OrgPersonCountDO;
import com.manage.rbac.entity.ext.UserCrowdDO;
import com.manage.rbac.entity.ext.UserPostDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    //分页查询用户
    public List<User> listUserPage(@Param("user") String user,@Param("orgId") Integer orgId,@Param("orgName") String orgName,
                                   @Param("start") Integer start,@Param("pageSize") Integer pageSize);

    //分页计数
    public long countByUserAndOrg(@Param("user") String user,@Param("orgId") Integer orgId,@Param("orgName") String orgName);

    //根据手机号或网名查询账号
    public List<User> listUserByMobileOrNickname(@Param("condition") String condition,@Param("orgId") Integer orgId);

    //批量查询用户岗位
    public List<UserPostDO> listUserPostByUsers(@Param("userIdList") List<Integer> userIdList);

    //批量查询用户用户组
    public List<UserCrowdDO> listUserCrowdByUsers(@Param("userIdList") List<Integer> userIdList);

    //查询单个机构人数
    public Long countByOrg(@Param("orgId") Integer orgId);

    //查询多个机构人数
    public List<OrgPersonCountDO> listOrgPersonCount(@Param("orgIdList")  List<Integer> orgIdList);

    //根据姓名或网名查询用户列表
    public List<User> listUserByNameOrNickName(@Param("name") String name);

}
