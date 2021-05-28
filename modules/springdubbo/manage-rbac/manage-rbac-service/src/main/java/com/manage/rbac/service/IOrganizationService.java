package com.manage.rbac.service;

import com.manage.rbac.entity.Organization;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.OrganizationDTO;
import com.model.base.PageVo;

import java.util.List;

/**
 * Organization的服务接口
 */
public interface IOrganizationService {

    //分页查询机构
    public PageVo<OrganizationDTO> listOrgPage(String name, Integer pageIndex, Integer pageSize);

    //查询机构详情
    public OrganizationDTO getOrgDetail(Integer id);

    //根据名称查询机构数量
    public long countByName(String name);

    //添加机构
    public boolean addOrg(OrganizationDTO orgDTO);

    //修改机构
    public boolean updateOrg(OrganizationDTO orgDTO);

    //启用机构
    public boolean enableOrg(Integer id, OperatorDTO operator);

    //禁用机构
    public boolean disableOrg(Integer id,OperatorDTO operator);

    //查询所有启用的机构
    public List<Organization> listAbleOrg();

    //查询可选机构列表
    public List<Organization> listOptionalOrg();

    //查询岗位可属机构列表
    public List<Organization> listOptionalOrgByPost(Integer postId);

    //查询用户组可属机构列表
    public List<Organization> listOptionalOrgByCrowd(Integer crowdId);

    //查询岗位关联的机构
    public List<Organization> listOrgByPost(Integer postId);

    //查询用户组关联的机构
    public List<Organization> listOrgByCrowd(Integer crowdId);

    //根据名称查询机构列表
    public List<Organization> listOrgByName(String name);

    public Organization getById(Integer id);
}
