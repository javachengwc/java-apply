package com.storefront.manage.controller.rbac;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.util.TransUtil;
import com.storefront.manage.model.pojo.rbac.Role;
import com.storefront.manage.model.vo.RoleQueryVo;
import com.storefront.manage.model.vo.RoleVo;
import com.storefront.manage.service.rbac.RoleService;
import com.storefront.manage.enums.ApiCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Api(value = "角色相关接口", description = "角色相关接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "分页查询角色", notes = "分页查询角色")
    @PostMapping("/queryPage")
    public Resp<Page<RoleVo>> queryPage(@Validated @RequestBody Req<RoleQueryVo> req, Errors errors) {
        RoleQueryVo queryVo = req.getData();
        queryVo.genPage();

        Page<RoleVo> page= roleService.queryPage(queryVo);
        if(page.getList()==null) {
            page.setList(Collections.EMPTY_LIST);
        }
        Resp<Page<RoleVo>> resp = Resp.success(page,"成功");
        return  resp;
    }

    @ApiOperation(value = "查询所有的角色", notes = "查询所有的角色")
    @PostMapping("/queryAll")
    public Resp<List<RoleVo>> queryAll(@Validated @RequestBody Req<Void> req) {
        List<Role> list = roleService.queryAll();
        List<RoleVo> rtList= TransUtil.transList(list,RoleVo.class);
        Resp<List<RoleVo>> resp = Resp.success(rtList,"成功");
        return  resp;
    }

    @ApiOperation(value = "查询角色详情", notes = "查询角色详情")
    @PostMapping("/getById")
    public Resp<RoleVo> getById(@Validated @RequestBody Req<Long> req) {
        Long roleId = req.getData();
        Role role = roleService.getById(roleId);
        if(role==null) {
            Resp<RoleVo> resp =Resp.error(ApiCode.NO_DATA.getCode(),"角色不存在");
            return resp;
        }
        RoleVo roleVo =TransUtil.transEntity(role,RoleVo.class);
        List<Long> menuIds = roleService.queryMenuIdsByRole(roleId);
        if (menuIds == null) {
            menuIds = Collections.EMPTY_LIST;
        }
        roleVo.setMenuIds(menuIds);
        Resp<RoleVo> resp = Resp.success(roleVo,"成功");
        return  resp;
    }

    @ApiOperation(value = "新增角色", notes = "新增角色")
    @PostMapping("/add")
    public Resp<Void> add(@Validated @RequestBody Req<RoleVo> req, Errors errors) {
        RoleVo roleVo = req.getData();
        String roleName = roleVo.getName();
        if (roleService.hasExistRoleName(roleName)) {
            return Resp.error("角色名称已经存在");
        }

        String roleCode =roleVo.getCode();
        if (roleService.hasExistRoleCode(roleCode)) {
            return Resp.error("角色code已经存在");
        }
        roleService.addRoleWithMenu(roleVo);
        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PostMapping("/update")
    public Resp<Void> update(@Validated @RequestBody Req<RoleVo> req) {
        RoleVo roleVo = req.getData();
        Long roleId=roleVo.getId();
        Role orglRole = roleService.getById(roleId);
        String newName = roleVo.getName();
        if (!orglRole.getName().equals(newName) &&  roleService.hasExistRoleName(newName)) {
            return Resp.error("角色名称已经存在");
        }

        String newCode =roleVo.getCode();
        if (!orglRole.getCode().equals(newCode) &&  roleService.hasExistRoleCode(newCode)) {
            return Resp.error("角色code已经存在");
        }

        roleService.uptRoleWithMenu(roleVo);
        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }

    @ApiOperation(value = "批量删除角色", notes = "批量删除角色")
    @PostMapping("/batchDel")
    public Resp<Void> batchDel(@Validated @RequestBody Req<List<Long>> req) {
        List<Long> roleIds = req.getData();
        if(roleIds==null || roleIds.size() <=0) {
            Resp<Void> resp = Resp.success(null,"成功");
            return  resp;
        }
        for (Long roleId : roleIds) {
            Role role = roleService.getById(roleId);
            if(role==null) {
                continue;
            }
            boolean hasUser =roleService.hasUser(roleId);
            if(hasUser) {
                Resp<Void> resp = Resp.error("角色"+role.getName()+"下还有用户");
                return  resp;
            }
        }
        roleService.delRolesWithMenu(roleIds);
        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }
}
