package com.manage.rbac.dao.ext;

import com.manage.rbac.entity.Organization;
import com.manage.rbac.entity.ext.OrgCrowdDO;
import com.manage.rbac.entity.ext.OrgPostDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrgDao {

    //分页查询机构
    public List<Organization> listOrgPage(@Param("name") String name,@Param("start") Integer start,@Param("pageSize") Integer pageSize);

    //分页查询时总数
    public long countPage(@Param("name") String name);

    //查询岗位关联的机构
    public List<Organization> listOrgByPost(@Param("postId") Integer postId);

    //查询用户组关联的机构
    public List<Organization> listOrgByCrowd(@Param("crowdId") Integer crowdId);

    //查询与某岗位无关联的机构列表
    public List<Organization> listOrgNotRelaPost(@Param("postId") Integer postId);

    //查询与某用户组无关联的机构列表
    public List<Organization> listOrgNotRelaCrowd(@Param("crowdId") Integer crowdId);

    public List<OrgPostDO> listOrgPostByOrgs(@Param("orgIdList") List<Integer> orgIdList);

    public List<OrgCrowdDO> listOrgCrowdByOrgs(@Param("orgIdList") List<Integer> orgIdList);

    public List<Organization> listOrgByName(@Param("name") String name);
}
