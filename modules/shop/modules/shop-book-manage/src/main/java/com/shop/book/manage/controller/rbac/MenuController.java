package com.shop.book.manage.controller.rbac;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.manage.model.vo.MenuQueryVo;
import com.shop.book.manage.model.vo.MenuVo;
import com.shop.book.manage.service.rdbc.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Api(value = "菜单相关接口", description = "菜单相关接口")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "分页查询菜单", notes = "分页查询菜单")
    @PostMapping("/queryPage")
    public Resp<Page<MenuVo>> queryPage(@Validated @RequestBody Req<MenuQueryVo> req, Errors errors) {
        MenuQueryVo queryVo = req.getData();
        queryVo.genPage();

        Page<MenuVo> page= menuService.queryPage(queryVo);
        if(page.getList()==null) {
            page.setList(Collections.EMPTY_LIST);
        }

        Resp<Page<MenuVo>> resp = Resp.success(page,"成功");
        return  resp;
    }

    @ApiOperation(value = "查询类型是菜单的菜单列表", notes = "查询类型是菜单的菜单列表")
    @PostMapping("/queryOnlyMenuList")
    public Resp<List<MenuVo>> queryOnlyMenuList(@RequestBody Req<Void> req) {
        List<MenuVo> list = menuService.queryOnlyMenuList();
        if(list==null) {
            list = Collections.EMPTY_LIST;
        }
        Resp<List<MenuVo>> resp = Resp.success(list,"成功");
        return  resp;
    }
}
