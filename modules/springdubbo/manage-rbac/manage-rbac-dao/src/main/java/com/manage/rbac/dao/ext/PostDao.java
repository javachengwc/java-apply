package com.manage.rbac.dao.ext;

import com.manage.rbac.entity.Post;
import com.manage.rbac.entity.ext.OrgPostDO;
import com.manage.rbac.entity.ext.PostRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostDao {

    //分页查询岗位
    public List<Post> listPostPage(@Param("post") String post,@Param("start") Integer start,@Param("pageSize") Integer pageSize);

    public long countByNameOrCode(@Param("post") String post);

    public List<Post> listPostNotOrg();

    public List<Post> listPostByOrg(@Param("orgId") Integer orgId);

    public List<Post> listPostByUser(@Param("userId") Integer userId);

    public List<PostRoleDO> listPostRoleByPosts(@Param("postIdList") List<Integer> postIdList);

    public List<OrgPostDO> listOrgPostByPosts(@Param("postIdList") List<Integer> postIdList);

    public List<PostRoleDO> listPostTeacherByPosts(@Param("postIdList") List<Integer> postIdList);
}
