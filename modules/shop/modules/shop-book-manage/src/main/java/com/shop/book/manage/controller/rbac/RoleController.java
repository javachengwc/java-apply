package com.shop.book.manage.controller.rbac;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.util.TransUtil;
import com.shop.book.manage.model.pojo.Role;
import com.shop.book.manage.model.vo.RoleQueryVo;
import com.shop.book.manage.model.vo.RoleVo;
import com.shop.book.manage.service.rdbc.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Resp<List<RoleVo>> queryAll(@Validated @RequestBody Req<Void> reqst) {
        List<Role> list = roleService.queryAll();
        List<RoleVo> rtList= TransUtil.transList(list,RoleVo.class);
        Resp<List<RoleVo>> resp = Resp.success(rtList,"成功");
        return  resp;
    }
}
