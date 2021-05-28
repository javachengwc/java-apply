package com.manage.rbac.provider;

import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.OrganizationDTO;
import com.model.base.PageVo;
import com.model.base.Resp;

import java.util.List;

public interface IOrganizationProvider {

    //分页查询机构
    public Resp<PageVo<OrganizationDTO>> listOrgPage(String name, Integer pageIndex, Integer pageSize);

    //查询机构详情
    public Resp<OrganizationDTO> getOrgDetail(Integer id);

    //根据名称查询数量
    public Resp<Long> countByName(String name);

    //增加机构
    public Resp<Void> addOrg(OrganizationDTO orgDTO);

    //修改机构
    public Resp<Void> updateOrg(OrganizationDTO orgDTO);

    //启用机构
    public Resp<Void> enableOrg(Integer id,OperatorDTO operator);

    //禁用机构
    public Resp<Void> disableOrg(Integer id,OperatorDTO operator);

    //查询所有启用的机构
    public Resp<List<OrganizationDTO>> listAbleOrg();

    //根据名称查询机构列表
    public Resp<List<OrganizationDTO>> listOrgByName(String name);

    public Resp<OrganizationDTO> getById(Integer id);
}