package com.manage.rbac.dao.ext;

import com.manage.rbac.entity.Crowd;
import com.manage.rbac.entity.ext.CrowdRoleDO;
import com.manage.rbac.entity.ext.OrgCrowdDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CrowdDao {

    //分页查询用户组
    public List<Crowd> listCrowdPage(@Param("name") String name,@Param("start") Integer start,@Param("pageSize") Integer pageSize);

    //分页查总数
    public long countPage(@Param("name") String name);

    //分页查询不包含系统角色的用户组
    public List<Crowd> listCrowdNoSysPage(@Param("name") String name,@Param("start") Integer start,@Param("pageSize") Integer pageSize);

    //分页查不包含系统角色的用户组总数
    public long countNoSysPage(@Param("name") String name);

    //查询无机构关联的用户组
    public List<Crowd> listCrowdNotOrg();

    //查询机构关联的用户组
    public List<Crowd> listCrowdByOrg(@Param("orgId") Integer orgId);

    //查询用户关联的用户组
    public List<Crowd> listCrowdByUser(@Param("userId") Integer userId);

    //批量查询用户组的用户组角色
    public List<CrowdRoleDO> listCrowdRoleByCrowds(@Param("crowdIdList") List<Integer> crowdIdList);

    //批量查询用户组的机构用户组
    public List<OrgCrowdDO> listOrgCrowdByCrowds(@Param("crowdIdList") List<Integer> crowdIdList);

    //根据用户组删除用户组角色
    public int deleteCrowdRoleByCrowd(Integer crowdId);

    //根据用户组删除机构用户组
    public int deleteOrgCrowdByCrowd(Integer crowdId);

    //根据用户组删除用户用户组
    public int deleteUserCrowdByCrowd(Integer crowdId);

    public List<CrowdRoleDO> listCrowdTeacherByCrowds(@Param("crowdIdList") List<Integer> crowdIdList);

}
