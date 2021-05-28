package com.manage.rbac.provider.impl;

import com.manage.rbac.model.common.Constant;
import com.model.base.Resp;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.SystemMenuDTO;
import com.manage.rbac.provider.IMenuProvider;
import com.manage.rbac.service.IMenuService;
import com.manage.rbac.model.dto.MenuDTO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* Menu相关dubbo服务实现类.
*/
@Service(version = Constant.DUBBO_API_VERSION)
public class MenuProviderImpl implements IMenuProvider {

    @Autowired
    private IMenuService service;

    //查询菜单详情
    @Override
    public Resp<MenuDTO> getMenuDetail(Integer id) {
        MenuDTO menuDTO =service.getMenuDetail(id);
        return Resp.data(menuDTO);
    }

    //根据父级和名称查询数量
    @Override
    public Resp<Long> countByParentAndName(Integer parentId,String name) {
        long count = service.countByParentAndName(parentId,name);
        return Resp.data(count);
    }

    //查询子菜单数量
    @Override
    public Resp<Long> countChild(Integer id) {
        long count = service.countChild(id);
        return Resp.data(count);
    }

    //增加菜单
    @Override
    public Resp<Void> addMenu(MenuDTO menuDTO) {
        service.addMenu(menuDTO);
        return Resp.success();
    }

    //修改菜单
    @Override
    public Resp<Void> updateMenu(MenuDTO menuDTO) {
        service.updateMenu(menuDTO);
        return Resp.success();
    }

    //启用菜单
    @Override
    public Resp<Void> enableMenu(Integer id, OperatorDTO operator) {
        service.enableMenu(id,operator);
        return Resp.success();
    }

    //禁用菜单
    @Override
    public Resp<Void> disableMenu(Integer id, OperatorDTO operator) {
        service.disableMenu(id,operator);
        return Resp.success();
    }

    //删除菜单
    @Override
    public Resp<Void> deleteMenu(Integer id) {
        service.deleteMenu(id);
        return Resp.success();
    }

    //查询系统菜单树
    @Override
    public Resp<List<MenuDTO>> listMenuTreeBySystem(Integer systemId) {
        List<MenuDTO> list = service.listMenuTreeBySystem(systemId);
        return Resp.data(list);
    }

    //查询角色关联系统菜单
    @Override
    public Resp<List<SystemMenuDTO>> listSystemMenuByRole(Integer roleId) {
        List<SystemMenuDTO> list = service.listSystemMenuByRole(roleId);
        return Resp.data(list);
    }

    //查询用户菜单
    @Override
    public Resp<List<SystemMenuDTO>> listSystemMenuByUser(Integer userId) {
        List<SystemMenuDTO> list = service.listSystemMenuByUser(userId);
        return Resp.data(list);
    }

    //修改角色系统菜单
    @Override
    public Resp<Void> updateRoleMenu(Integer roleId,List<SystemMenuDTO> systemMenuList) {
        service.updateRoleMenu(roleId,systemMenuList);
        return Resp.success();
    }

    @Override
    public Resp<MenuDTO> getById(Integer id) {
        MenuDTO menu=service.getMenuDetail(id);
        return Resp.data(menu);
    }
}