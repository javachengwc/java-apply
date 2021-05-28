package com.manage.rbac.provider;

import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.PostDTO;
import com.model.base.PageVo;
import com.model.base.Resp;

import java.util.List;

public interface IPostProvider {

    //分页查询岗位
    public Resp<PageVo<PostDTO>> listPostPage(String post, Integer pageIndex, Integer pageSize);

    //查询岗位详情
    public Resp<PostDTO> getPostDetail(Integer id);

    //根据岗位名称查询数量
    public Resp<Long> countByName(String name);

    //根据岗位code查询数量
    public Resp<Long> countByCode(String code);

    //添加岗位
    public Resp<Void> addPost(PostDTO post);

    //修改岗位
    public Resp<Void> updatePost(PostDTO post);

    //启用岗位
    public Resp<Void> enablePost(Integer id,OperatorDTO operator);

    //禁用岗位
    public Resp<Void> disablePost(Integer id,OperatorDTO operator);

    //查询可选岗位
    public Resp<List<PostDTO>> listOptionalPost();

    //根据机构查询关联的岗位
    public Resp<List<PostDTO>> listPostByOrg(Integer orgId);

    public Resp<PostDTO> getById(Integer id);

    //查询没机构的岗位
    public Resp<List<PostDTO>> listPostNotOrg();
}