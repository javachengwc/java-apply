package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.model.param.*;
import com.manage.rbac.model.vo.*;
import com.manage.rbac.shiro.LoginContext;
import com.model.base.PageVo;
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

import java.util.Collections;
import java.util.List;

/**
 * 角色接口
 */
@RestController
@RequestMapping("/rbac/role")
@Api(value="角色接口",description="角色接口")
@Slf4j
public class RoleController extends BaseController {

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/listRolePage")
    @ApiOperation("分页查询角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "第几页", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Integer", paramType = "query"),
    })
    public Resp<PageVo<RoleVO>> listRolePage(@RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
                                             @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize) {
        Resp<PageVo<RoleDTO>> rt =dubboFactory.getRoleProvider().listRolePage(pageIndex,pageSize);
        PageVo<RoleDTO> pageInfo =rt.getData();

        PageVo<RoleVO> pageVo = new PageVo<RoleVO>();
        pageVo.setTotalCount(pageInfo.getTotalCount());
        pageVo.setList(TransUtil.transList(pageInfo.getList(),RoleVO.class));

        return Resp.data(pageVo);
    }

    @GetMapping("/getRole")
    @ApiOperation("查询角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<RoleVO> getRole(@RequestParam(value = "id") Integer id){
        if(id==null || id<=0) {
            return Resp.success();
        }
        Resp<RoleDTO> rt =dubboFactory.getRoleProvider().getRole(id);
        RoleDTO dto= rt.getData();
        if(dto==null) {
            return Resp.success();
        }
        RoleVO roleVO = TransUtil.transEntity(dto,RoleVO.class);
        return Resp.data(roleVO);
    }

    @PostMapping("/addRole")
    @ApiOperation("添加角色")
    public Resp<Void> addRole(@RequestBody Req<RoleReq> req){
        log.info("RoleController addRole start..........");
        RoleReq roleReq = req.getData();
        if(roleReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        String name = roleReq.getName();
        String code = roleReq.getCode();
        if(StringUtils.isAnyBlank(name,code)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Resp<Long> nCountRt =dubboFactory.getRoleProvider().countByName(name);
        Long nameCount = nCountRt.getData();
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同名角色");
            return result;
        }
        Resp<Long> cCountRt =dubboFactory.getRoleProvider().countByCode(code);
        Long codeCount =cCountRt.getData();
        if(codeCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同编码角色");
            return result;
        }

        RoleDTO roleDTO = TransUtil.transEntity(roleReq,RoleDTO.class);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            roleDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getRoleProvider().addRole(roleDTO);
        return result;
    }

    @PostMapping("/updateRole")
    @ApiOperation("修改角色")
    public Resp<Void> updateRole(@RequestBody Req<RoleReq> req){
        log.info("RoleController updateRole start..........");
        RoleReq roleReq = req.getData();
        if(roleReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        Integer id = roleReq.getId();
        Resp<RoleDTO> rs =dubboFactory.getRoleProvider().getById(id);
        RoleDTO orglRole = rs.getData();
        if(orglRole==null) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("角色不存在");
            return result;
        }

        String name = roleReq.getName();
        String code = roleReq.getCode();

        if(StringUtils.isNoneBlank(name)) {
            Resp<Long> nCountRt = dubboFactory.getRoleProvider().countByName(name);
            Long nameCount = nCountRt.getData();
            if (name.equals(orglRole.getName())) {
                nameCount -= 1;
            }
            if (nameCount > 0L) {
                Resp<Void> result = Resp.error(RestCode.V_PARAM_ERROR);
                result.getHeader().setMsg("已存在同名角色");
                return result;
            }
        }

        if(StringUtils.isNoneBlank(code)) {
            Resp<Long> cCountRt = dubboFactory.getRoleProvider().countByCode(code);
            Long codeCount = cCountRt.getData();
            if (code.equals(orglRole.getCode())) {
                codeCount -= 1;
            }
            if (codeCount > 0L) {
                Resp<Void> result = Resp.error(RestCode.V_PARAM_ERROR);
                result.getHeader().setMsg("已存在同编码角色");
                return result;
            }
        }

        RoleDTO roleDTO = TransUtil.transEntity(roleReq,RoleDTO.class);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            roleDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getRoleProvider().updateRole(roleDTO);
        return result;
    }

    @PostMapping("/enableOrNotRole")
    @ApiOperation("启动禁用角色")
    public Resp<Void> enableOrNotRole(@RequestBody Req<EnableReq> req){
        log.info("RoleController enableOrNotRole start..........");
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
            dubboFactory.getRoleProvider().enableRole(id,operator);
        } else {
            //禁用
            dubboFactory.getRoleProvider().disableRole(id,operator);
        }
        return Resp.success();
    }

    @GetMapping("/listOptionalRole")
    @ApiOperation("查询可选角色列表")
    public Resp<List<RoleSimpleVO>> listOptionalRole(){
        Resp<List<RoleDTO>> rt =dubboFactory.getRoleProvider().listOptionalRole();
        List<RoleSimpleVO> list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),RoleSimpleVO.class);
        return Resp.data(list);
    }

    @PostMapping("/updateRoleMenu")
    @ApiOperation("修改角色关联菜单")
    public Resp<Void> updateRoleMenu(@RequestBody Req<RoleMenuReq> req){
        RoleMenuReq roleMenuReq = req.getData();
        Integer roleId = (roleMenuReq==null || roleMenuReq.getRoleId()==null) ? null: roleMenuReq.getRoleId();
        log.info("RoleController updateRoleMenu start,roleId={}",roleId);
        if(roleMenuReq==null || roleId==null ) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        RoleMenuDTO roleMenuDTO = TransUtil.transEntity(roleMenuReq,RoleMenuDTO.class);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            roleMenuDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getRoleProvider().updateRoleMenu(roleMenuDTO);
        return result;
    }
}
