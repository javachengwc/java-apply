package com.shop.book.manage.controller.rbac;

import com.shop.base.model.Page;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.util.TransUtil;
import com.shop.book.manage.model.pojo.manage.Menu;
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
import java.util.Date;
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

    @ApiOperation(value = "查询树状菜单", notes = "查询树状菜单")
    @PostMapping("/queryMenuTree")
    public Resp<List<MenuVo>> queryMenuTree(@Validated @RequestBody Req<Void> req) {
        List<MenuVo> list=menuService.queryMenuTree();
        Resp<List<MenuVo>> resp = Resp.success(list,"成功");
        return  resp;
    }

    @ApiOperation(value = "根据菜单ID查询菜单", notes = "根据菜单ID查询菜单")
    @PostMapping("/getById")
    public Resp<MenuVo> getById(@Validated @RequestBody Req<Long> req) {
        Long menuId = req.getData();
        if(menuId==null ) {
            Resp<MenuVo> resp = Resp.error("参数验证失败");
            return  resp;
        }
        Menu menu = menuService.getById(menuId);
        if (menu == null) {
            Resp<MenuVo> resp = Resp.error("菜单不存在");
            return  resp;
        }
        MenuVo menuVo = TransUtil.transEntity(menu,MenuVo.class);

        Resp<MenuVo> resp = Resp.success(menuVo,"成功");
        return  resp;
    }

    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    @PostMapping("/add")
    public Resp<Long> add(@Validated @RequestBody Req<MenuVo> req) {
        MenuVo menuVo = req.getData();
        if(menuVo==null) {
            Resp<Long> resp = Resp.error("参数验证失败");
            return  resp;
        }
        Date now = new Date();
        Menu menu =TransUtil.transEntity(menuVo,Menu.class);
        menu.setCreateTime(now);
        menu.setModifiedTime(now);

        menuService.addMenu(menu);
        Long menuId =menu.getId();
        Resp<Long> resp = Resp.success(menuId,"成功");
        return  resp;
    }

    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    @PostMapping("/update")
    public Resp<Void> update(@Validated @RequestBody Req<MenuVo> req) {
        MenuVo menuVo = req.getData();
        if(menuVo==null || menuVo.getId()==null) {
            Resp<Void> resp = Resp.error("参数验证失败");
            return  resp;
        }
        Menu uptMenu =TransUtil.transEntity(menuVo,Menu.class);
        uptMenu.setId(menuVo.getId());
        uptMenu.setModifiedTime(new Date());
        menuService.uptMenu(uptMenu);

        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }

    @ApiOperation(value = "批量删除菜单", notes = "批量删除菜单")
    @PostMapping("/batchDel")
    public Resp<Void> batchDel(@Validated @RequestBody Req<List<Long>> req) {
        List<Long> menuIds = req.getData();
        if(menuIds==null || menuIds.size() <=0) {
            Resp<Void> resp = Resp.success(null,"成功");
            return  resp;
        }
        for (Long menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if(menu==null) {
                continue;
            }
            boolean hasChild =menuService.hasChildren(menuId);
            if(hasChild) {
                Resp<Void> resp = Resp.error("菜单"+menu.getName()+"下还有子菜单,请先删除子菜单");
                return  resp;
            }
        }
        menuService.batchDel(menuIds);
        Resp<Void> resp = Resp.success(null,"成功");
        return  resp;
    }
}
