package com.manage.rbac.service;

import com.manage.rbac.entity.Post;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.PostDTO;
import com.model.base.PageVo;

import java.util.List;

/**
 * Post的服务接口
 */
public interface IPostService {

    //分页查询岗位
    public PageVo<PostDTO> listPostPage(String post, Integer pageIndex, Integer pageSize);

    //查询岗位详情
    public PostDTO getPostDetail(Integer id);

    //根据岗位名称查询数量
    public long countByName(String name);

    //根据编码查询数量
    public long countByCode(String code);

    //启用岗位
    public boolean enablePost(Integer id,OperatorDTO operator);

    //禁用岗位
    public boolean disablePost(Integer id,OperatorDTO operator);

    //添加岗位
    public boolean addPost(PostDTO postDTO);

    //修改岗位
    public boolean updatePost(PostDTO postDTO);

    //查询可选岗位
    public List<Post> listOptionalPost();

    //查询没机构的岗位
    public List<Post> listPostNotOrg();

    //根据机构查询关联的岗位
    public List<Post> listPostByOrg(Integer orgId);

    //根据用户查询关联的岗位
    public List<Post> listPostByUser(Integer userId);
}
