package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.MenuDTO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.SystemMenuDTO;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.model.enums.MenuTypeEnum;
import com.manage.rbac.model.param.EnableReq;
import com.manage.rbac.model.param.MenuReq;
import com.manage.rbac.model.vo.*;
import com.manage.rbac.shiro.LoginContext;
import com.model.base.Req;
import com.model.base.Resp;
import com.springdubbo.common.RestCode;
import com.springdubbo.controller.BaseController;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单接口
 */
@RestController
@RequestMapping("/rbac/menu")
@Api(value="菜单接口",description="菜单接口")
@Slf4j
public class MenuController extends BaseController {

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/getMenu")
    @ApiOperation("查询菜单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<MenuVO> getMenu(@RequestParam(value = "id") Integer id){
        if(id==null || id<=0) {
            return Resp.success();
        }
        Resp<MenuDTO> rt =dubboFactory.getMenuProvider().getMenuDetail(id);
        MenuDTO dto= rt.getData();
        if(dto==null) {
            return Resp.success();
        }
        MenuVO menuVO = TransUtil.transEntity(dto,MenuVO.class);
        return Resp.data(menuVO);
    }

    @PostMapping("/addMenu")
    @ApiOperation("添加菜单")
    public Resp<Void> addMenu(@RequestBody Req<MenuReq> req){
        MenuReq menuReq = req.getData();
        Resp<Void> checkResult = this.checkMenu(menuReq);
        if(!checkResult.isSuccess()) {
            return checkResult;
        }
        Integer parentId =menuReq.getParentId();
        if(parentId==null) {
            parentId=0;
            menuReq.setParentId(parentId);
        }
        String name = menuReq.getName();
        Resp<Long> nCountRt =dubboFactory.getMenuProvider().countByParentAndName(parentId,name);
        Long nameCount = nCountRt.getData();
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("同父级下已存在同名菜单");
            return result;
        }
        MenuDTO menuDTO = TransUtil.transEntity(menuReq,MenuDTO.class);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            menuDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getMenuProvider().addMenu(menuDTO);
        return result;
    }

    private Resp<Void> checkMenu(MenuReq menuReq) {
        if(menuReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Integer type = menuReq.getType();
        MenuTypeEnum menuType = MenuTypeEnum.getMenuTypeByValue(type);
        if(menuType==null) {
            return Resp.error(RestCode.V_PARAM_ERROR);
        }

        String name = menuReq.getName();
        if(StringUtils.isBlank(name)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        return Resp.success();
    }

    @PostMapping("/updateMenu")
    @ApiOperation("修改菜单")
    public Resp<Void> updateMenu(@RequestBody Req<MenuReq> req){
        MenuReq menuReq = req.getData();
        Resp<Void> checkResult = this.checkMenu(menuReq);
        if(!checkResult.isSuccess()) {
            return checkResult;
        }
        Integer id = menuReq.getId();
        Resp<MenuDTO> rs =dubboFactory.getMenuProvider().getById(id);
        MenuDTO menuOrgl = rs.getData();
        if(menuOrgl==null) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("菜单不存在");
            return result;
        }

        Integer parentId =menuOrgl.getParentId();
        String name = menuReq.getName();
        Resp<Long> nCountRt =dubboFactory.getMenuProvider().countByParentAndName(parentId,name);
        Long nameCount = nCountRt.getData();
        if(name.equals(menuOrgl.getName())) {
            nameCount-=1;
        }
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("同父级下已存在同名菜单");
            return result;
        }

        MenuDTO menuDTO = TransUtil.transEntity(menuReq,MenuDTO.class);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            menuDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getMenuProvider().updateMenu(menuDTO);
        return result;
    }

    @PostMapping("/deleteMenu")
    @ApiOperation("删除菜单")
    public Resp<Void> deleteMenu(@RequestBody Req<EntityVO> req){
        EntityVO entityVO = req.getData();
        Integer id = entityVO.getId();
        if(id==null || id<=0) {
            return Resp.success();
        }
        Resp<Long> childResult = dubboFactory.getMenuProvider().countChild(id);
        long childCount = childResult.getData()==null?0:childResult.getData();
        if(childCount>0) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("菜单下还有子菜单，请先删除子菜单");
            return result;
        }
        UserDTO curUser = LoginContext.getLoginUser();
        Integer operatorId = curUser==null?null:curUser.getId();
        log.info("MenuController deleteMenu id={},operatorId={}",id,operatorId);
        dubboFactory.getMenuProvider().deleteMenu(id);
        return Resp.success();
    }

    @PostMapping("/enableOrNotMenu")
    @ApiOperation("启动禁用菜单")
    public Resp<Void> enableOrNotMenu(@RequestBody Req<EnableReq> req){
        log.info("OrgController enableOrNotOrg start..........");
        EnableReq enableReq = req==null?null: req.getData();
        Integer id = enableReq==null?null:enableReq.getId();
        Integer operate = enableReq==null?null:enableReq.getOperate();
        if(id==null || operate==null) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        EnableEnum enableEnum = EnableEnum.getByValue(operate);
        if(enableEnum==null) {
            //参数错误
            return Resp.error(RestCode.V_PARAM_ERROR);
        }
        UserDTO curUser = LoginContext.getLoginUser();
        OperatorDTO operator=null;
        if(curUser!=null) {
            operator =new OperatorDTO(curUser.getId(), curUser.getNickname());
        }
        if(EnableEnum.ENABLE==enableEnum) {
            //启用
            dubboFactory.getMenuProvider().enableMenu(id,operator);
        } else {
            //禁用
            dubboFactory.getMenuProvider().disableMenu(id,operator);
        }
        return Resp.success();
    }

    @GetMapping("/listMenuTreeBySystem")
    @ApiOperation("根据系统查询菜单树")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "systemId", value = "系统Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<List<MenuNodeVO>> listMenuTreeBySystem(@RequestParam(value = "systemId") Integer systemId){
        if(systemId==null || systemId<=0) {
            return Resp.success();
        }
        Resp<List<MenuDTO>> rt=dubboFactory.getMenuProvider().listMenuTreeBySystem(systemId);
        List<MenuNodeVO> rtList = TransUtil.transListWithJson(rt.getData(),MenuNodeVO.class);
        return Resp.data(rtList);
    }

    @GetMapping("/listSystemMenuByRole")
    @ApiOperation("查询角色关联的系统菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<List<SystemMenuNodeVO>> listSystemMenuByRole(@RequestParam(value = "roleId") Integer roleId){
        List<SystemMenuNodeVO> list= new ArrayList<SystemMenuNodeVO>();
        if(roleId==null || roleId<=0) {
            return Resp.success();
        }
        Resp<List<SystemMenuDTO>> rt=dubboFactory.getMenuProvider().listSystemMenuByRole(roleId);
        List<SystemMenuNodeVO> rtList = TransUtil.transListWithJson(rt.getData(),SystemMenuNodeVO.class);
        return  Resp.data(rtList);
    }
    
}
