package com.manage.rbac.service;

import com.manage.rbac.entity.Crowd;
import com.manage.rbac.model.dto.CrowdDTO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.model.base.PageVo;

import java.util.List;

/**
 * Crowd的服务接口
 */
public interface ICrowdService {

    //分页查询用户组
    public PageVo<CrowdDTO> listCrowdPage(String name, Integer pageIndex, Integer pageSize);

    //分页查询不包含系统角色的用户组
    public PageVo<CrowdDTO> listCrowdNoSysPage(String name, Integer pageIndex, Integer pageSize);

    //查询用户组详情
    public CrowdDTO getCrowdDetail(Integer id);

    //根据名称查询数量
    public long countByName(String name);

    //增加用户组
    public boolean addCrowd(CrowdDTO crowdDTO);

    //修改用户组
    public boolean updateCrowd(CrowdDTO crowdDTO);

    //启用用户组
    public boolean enableCrowd(Integer id, OperatorDTO operator);

    //禁用用户组
    public boolean disableCrowd(Integer id,OperatorDTO operator);

    //删除用户组
    public boolean deleteCrowd(Integer id);

    //查询可选用户组
    public List<Crowd> listOptionalCrowd();

    //查询无机构关联的用户组
    public List<Crowd> listCrowdNotOrg();

    //查询机构关联的用户组
    public List<Crowd> listCrowdByOrg(Integer orgId);

    //查询用户关联的用户组
    public List<Crowd> listCrowdByUser(Integer userId);

}
