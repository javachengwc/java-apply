package com.manage.rbac.provider.impl;

import com.model.base.PageVo;
import com.model.base.Resp;
import com.manage.rbac.model.common.Constant;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.provider.IPostProvider;
import com.manage.rbac.service.IPostService;
import com.manage.rbac.model.dto.PostDTO;
import com.manage.rbac.entity.Post;

import com.util.TransUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = Constant.DUBBO_API_VERSION)
public class PostProviderImpl implements IPostProvider {

    @Autowired
    private IPostService service;
    //分页查询岗位
    @Override
    public Resp<PageVo<PostDTO>> listPostPage(String post, Integer pageIndex, Integer pageSize) {
        PageVo<PostDTO> pageData = service.listPostPage(post,pageIndex,pageSize);
        return Resp.data(pageData);
    }

    //查询岗位详情
    @Override
    public Resp<PostDTO> getPostDetail(Integer id) {
        PostDTO postDTO =service.getPostDetail(id);
        return Resp.data(postDTO);
    }

    //根据岗位名称查询数量
    @Override
    public Resp<Long> countByName(String name) {
        long  count = service.countByName(name);
        return Resp.data(count);
    }

    //根据岗位code查询数量
    @Override
    public Resp<Long> countByCode(String code) {
        long  count = service.countByCode(code);
        return Resp.data(count);
    }

    //添加岗位
    @Override
    public Resp<Void> addPost(PostDTO postDTO) {
        service.addPost(postDTO);
        return Resp.success();
    }

    //修改岗位
    @Override
    public Resp<Void> updatePost(PostDTO postDTO) {
        service.updatePost(postDTO);
        return Resp.success();
    }

    //启用岗位
    @Override
    public Resp<Void> enablePost(Integer id, OperatorDTO operator) {
        service.enablePost(id,operator);
        return Resp.success();
    }

    //禁用岗位
    @Override
    public Resp<Void> disablePost(Integer id,OperatorDTO operator) {
        service.disablePost(id,operator);
        return Resp.success();
    }

    //查询可选岗位
    @Override
    public Resp<List<PostDTO>> listOptionalPost() {
        List<Post> list =service.listOptionalPost();
        List<PostDTO> rtList = TransUtil.transList(list,PostDTO.class);
        return Resp.data(rtList);
    }

    //查询没机构的岗位
    @Override
    public Resp<List<PostDTO>> listPostNotOrg() {
        List<Post> list =service.listPostNotOrg();
        List<PostDTO> rtList = TransUtil.transList(list,PostDTO.class);
        return Resp.data(rtList);
    }

    //根据机构查询关联的岗位
    @Override
    public Resp<List<PostDTO>> listPostByOrg(Integer orgId) {
        List<Post> list =service.listPostByOrg(orgId);
        List<PostDTO> rtList = TransUtil.transList(list,PostDTO.class);
        return Resp.data(rtList);
    }

    @Override
    public Resp<PostDTO> getById(Integer id) {
        PostDTO post=service.getPostDetail(id);
        return Resp.data(post);
    }

}