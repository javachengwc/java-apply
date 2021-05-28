package com.manage.rbac.provider;

import com.manage.rbac.model.dto.CrowdDTO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.model.base.PageVo;
import com.model.base.Resp;

import java.util.List;

public interface ICrowdProvider{

    //分页查询用户组
    public Resp<PageVo<CrowdDTO>> listCrowdPage(String name, Integer pageIndex, Integer pageSize);

    //分页查询不包含系统角色的用户组
    public Resp<PageVo<CrowdDTO>> listCrowdNoSysPage(String name, Integer pageIndex, Integer pageSize);

    //查询用户组详情
    public Resp<CrowdDTO> getCrowdDetail(Integer id);

    //根据名称查询数量
    public Resp<Long> countByName(String name);

    //增加用户组
    public Resp<Void> addCrowd(CrowdDTO crowdDTO);

    //修改用户组
    public Resp<Void> updateCrowd(CrowdDTO crowdDTO);

    //启用用户组
    public Resp<Void> enableCrowd(Integer id, OperatorDTO operator);

    //禁用用户组
    public Resp<Void> disableCrowd(Integer id,OperatorDTO operator);

    //删除用户组
    public Resp<Void> deleteCrowd(Integer id);

    //查询可选用户组列表
    public Resp<List<CrowdDTO>> listOptionalCrowd();

    //查询无机构关联的用户组
    public Resp<List<CrowdDTO>> listCrowdNotOrg();

    //根据机构查询用户组列表
    public Resp<List<CrowdDTO>> listCrowdByOrg(Integer orgId);

    public Resp<CrowdDTO> getById(Integer id);

}