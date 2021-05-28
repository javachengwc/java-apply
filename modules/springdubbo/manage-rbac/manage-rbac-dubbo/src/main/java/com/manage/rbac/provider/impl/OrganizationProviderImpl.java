package com.manage.rbac.provider.impl;

import com.model.base.PageVo;
import com.model.base.Resp;
import com.manage.rbac.model.common.Constant;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.provider.IOrganizationProvider;
import com.manage.rbac.service.IOrganizationService;
import com.manage.rbac.entity.Organization;

import com.util.TransUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = Constant.DUBBO_API_VERSION)
public class OrganizationProviderImpl implements IOrganizationProvider {

    @Autowired
    private IOrganizationService service;
    //分页查询机构
    @Override
    public Resp<PageVo<OrganizationDTO>> listOrgPage(String name, Integer pageIndex, Integer pageSize) {
        PageVo<OrganizationDTO> pageData = service.listOrgPage(name,pageIndex,pageSize);
        return Resp.data(pageData);
    }

    //查询机构详情
    @Override
    public Resp<OrganizationDTO> getOrgDetail(Integer id) {
        OrganizationDTO orgDTO =service.getOrgDetail(id);
        return Resp.data(orgDTO);
    }

    //根据名称查询数量
    @Override
    public Resp<Long> countByName(String name) {
        long  count = service.countByName(name);
        return Resp.data(count);
    }

    //增加机构
    @Override
    public Resp<Void> addOrg(OrganizationDTO orgDTO) {
        service.addOrg(orgDTO);
        return Resp.success();
    }

    //修改机构
    @Override
    public Resp<Void> updateOrg(OrganizationDTO orgDTO) {
        service.updateOrg(orgDTO);
        return Resp.success();
    }

    //启用机构
    @Override
    public Resp<Void> enableOrg(Integer id,OperatorDTO operator) {
        service.enableOrg(id,operator);
        return Resp.success();
    }

    //禁用机构
    @Override
    public Resp<Void> disableOrg(Integer id,OperatorDTO operator) {
        service.disableOrg(id,operator);
        return Resp.success();
    }

    //查询所有启用的机构
    @Override
    public Resp<List<OrganizationDTO>> listAbleOrg() {
        List<Organization> list =service.listAbleOrg();
        List<OrganizationDTO> rtList = TransUtil.transList(list,OrganizationDTO.class);
        return Resp.data(rtList);
    }

    //查询可选机构列表
    public Resp<List<OrganizationDTO>> listOptionalOrg() {
        List<Organization> list =service.listOptionalOrg();
        List<OrganizationDTO> rtList = TransUtil.transList(list,OrganizationDTO.class);
        return Resp.data(rtList);
    }

    //查询岗位可属机构列表
    public Resp<List<OrganizationDTO>> listOptionalOrgByPost(Integer postId) {
        List<Organization> list =service.listOptionalOrgByPost(postId);
        List<OrganizationDTO> rtList = TransUtil.transList(list,OrganizationDTO.class);
        return Resp.data(rtList);
    }

    //查询用户组可属机构列表
    public Resp<List<OrganizationDTO>> listOptionalOrgByCrowd(Integer crowdId) {
        List<Organization> list =service.listOptionalOrgByCrowd(crowdId);
        List<OrganizationDTO> rtList = TransUtil.transList(list,OrganizationDTO.class);
        return Resp.data(rtList);
    }

    @Override
    public Resp<List<OrganizationDTO>> listOrgByName(String name) {
        List<Organization> list =service.listOrgByName(name);
        List<OrganizationDTO> rtList = TransUtil.transList(list,OrganizationDTO.class);
        return Resp.data(rtList);
    }

    @Override
    public Resp<OrganizationDTO> getById(Integer id) {
        OrganizationDTO org =service.getOrgDetail(id);
        return Resp.data(org);
    }
}