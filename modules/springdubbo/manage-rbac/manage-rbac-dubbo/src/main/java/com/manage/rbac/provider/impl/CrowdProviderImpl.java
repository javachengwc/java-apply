package com.manage.rbac.provider.impl;

import com.model.base.PageVo;
import com.model.base.Resp;
import com.manage.rbac.model.common.Constant;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.provider.ICrowdProvider;
import com.manage.rbac.service.ICrowdService;
import com.manage.rbac.entity.Crowd;

import com.util.TransUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* Crowd相关dubbo服务实现类.
*/
@Service(version = Constant.DUBBO_API_VERSION)
public class CrowdProviderImpl implements ICrowdProvider {

    @Autowired
    private ICrowdService service;

    //分页查询用户组
    @Override
    public Resp<PageVo<CrowdDTO>> listCrowdPage(String name, Integer pageIndex, Integer pageSize) {
        PageVo<CrowdDTO> pageData = service.listCrowdPage(name,pageIndex,pageSize);
        return Resp.data(pageData);
    }


    //分页查询不包含系统角色的用户组
    @Override
    public Resp<PageVo<CrowdDTO>> listCrowdNoSysPage(String name, Integer pageIndex, Integer pageSize) {
        PageVo<CrowdDTO> pageData = service.listCrowdNoSysPage(name,pageIndex,pageSize);
        return Resp.data(pageData);
    }

    //查询用户组详情
    @Override
    public Resp<CrowdDTO> getCrowdDetail(Integer id) {
        CrowdDTO crowdDTO =service.getCrowdDetail(id);
        return Resp.data(crowdDTO);
    }

    //根据名称查询数量
    @Override
    public Resp<Long> countByName(String name) {
        long  count = service.countByName(name);
        return Resp.data(count);
    }

    //增加用户组
    @Override
    public Resp<Void> addCrowd(CrowdDTO crowdDTO) {
        service.addCrowd(crowdDTO);
        return Resp.success();
    }

    //修改用户组
    @Override
    public Resp<Void> updateCrowd(CrowdDTO crowdDTO) {
        service.updateCrowd(crowdDTO);
        return Resp.success();
    }

    //启用用户组
    @Override
    public Resp<Void> enableCrowd(Integer id,OperatorDTO operator) {
        service.enableCrowd(id,operator);
        return Resp.success();
    }

    //禁用用户组
    @Override
    public Resp<Void> disableCrowd(Integer id,OperatorDTO operator) {
        service.disableCrowd(id,operator);
        return Resp.success();
    }

    //删除用户组
    @Override
    public Resp<Void> deleteCrowd(Integer id) {
        service.deleteCrowd(id);
        return Resp.success();
    }

    //查询可选用户组列表
    @Override
    public Resp<List<CrowdDTO>> listOptionalCrowd() {
        List<Crowd> list =service.listOptionalCrowd();
        List<CrowdDTO> rtList = TransUtil.transList(list,CrowdDTO.class);
        return Resp.data(rtList);
    }

    //查询无机构关联的用户组
    @Override
    public Resp<List<CrowdDTO>> listCrowdNotOrg() {
        List<Crowd> list =service.listCrowdNotOrg();
        List<CrowdDTO> rtList = TransUtil.transList(list,CrowdDTO.class);
        return Resp.data(rtList);
    }

    //根据机构查询用户组列表
    @Override
    public Resp<List<CrowdDTO>> listCrowdByOrg(Integer orgId) {
        List<Crowd> list =service.listCrowdByOrg(orgId);
        List<CrowdDTO> rtList = TransUtil.transList(list,CrowdDTO.class);
        return Resp.data(rtList);
    }

    @Override
    public Resp<CrowdDTO> getById(Integer id) {
        CrowdDTO crowd = service.getCrowdDetail(id);
        return Resp.data(crowd);
    }
}