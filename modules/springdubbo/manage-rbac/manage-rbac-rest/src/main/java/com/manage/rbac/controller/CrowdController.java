package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.model.param.EnableReq;
import com.manage.rbac.model.param.CrowdReq;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户组接口
 */
@RestController
@RequestMapping("/rbac/crowd")
@Api(value="用户组接口",description="用户组接口")
@Slf4j
public class CrowdController extends BaseController {

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/listCrowdPage")
    @ApiOperation("分页查询用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户组名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "第几页", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Integer", paramType = "query"),
    })
    public Resp<PageVo<CrowdVO>> listCrowdPage(@RequestParam(value = "name",required = false) String name,
                                               @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
                                               @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize) {
        UserDTO operator = LoginContext.getLoginUser();
        boolean isSuper =true;
        if(operator!=null ) {
            Resp<Boolean> superResult = dubboFactory.getUserProvider().isSuperUser(operator.getId());
            isSuper = superResult.getData()==null?false:superResult.getData();
        }
        Resp<PageVo<CrowdDTO>> rt = null;
        if(isSuper) {
            rt = dubboFactory.getCrowdProvider().listCrowdPage(name, pageIndex, pageSize);
        } else {
            rt = dubboFactory.getCrowdProvider().listCrowdNoSysPage(name,pageIndex,pageSize);
        }

        PageVo<CrowdDTO> pageInfo = rt.getData();
        PageVo<CrowdVO> pageVo = new PageVo<CrowdVO>();
        pageVo.setTotalCount(pageInfo.getTotalCount());
        pageVo.setList(TransUtil.transListWithJson(pageInfo.getList(),CrowdVO.class));

        return Resp.data(pageVo);
    }

    private CrowdDTO trans2CrowdDTO(CrowdReq  crowdReq) {
        CrowdDTO crowdDTO = TransUtil.transEntity(crowdReq,CrowdDTO.class);
        crowdDTO.setOrgList(TransUtil.transList(crowdReq.getOrgList(), OrganizationDTO.class));
        crowdDTO.setRoleList(TransUtil.transList(crowdReq.getRoleList(), RoleDTO.class));
        return crowdDTO;
    }

    @GetMapping("/getCrowd")
    @ApiOperation("查询用户组详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户组Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<CrowdVO> getCrowd(@RequestParam(value = "id") Integer id){
        if(id==null || id<=0) {
            return Resp.success();
        }
        Resp<CrowdDTO> rt =dubboFactory.getCrowdProvider().getCrowdDetail(id);
        CrowdDTO dto= rt.getData();
        if(dto==null) {
            return Resp.success();
        }
        CrowdVO crowdVO = TransUtil.transEntityWithJson(dto,CrowdVO.class);
        return Resp.data(crowdVO);
    }

    @PostMapping("/addCrowd")
    @ApiOperation("添加用户组")
    public Resp<Void> addCrowd(@RequestBody Req<CrowdReq> req){
        CrowdReq crowdReq = req.getData();
        if(crowdReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        String name = crowdReq.getName();
        if(StringUtils.isBlank(name)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Resp<Long> nCountRt =dubboFactory.getCrowdProvider().countByName(name);
        Long nameCount = nCountRt.getData();
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同名用户组");
            return result;
        }
        CrowdDTO crowdDTO = this.trans2CrowdDTO(crowdReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            crowdDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getCrowdProvider().addCrowd(crowdDTO);
        return result;
    }

    @PostMapping("/updateCrowd")
    @ApiOperation("修改用户组")
    public Resp<Void> updateCrowd(@RequestBody Req<CrowdReq> req){
        CrowdReq crowdReq = req.getData();
        if(crowdReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        String name = crowdReq.getName();
        if(StringUtils.isBlank(name)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Integer id = crowdReq.getId();
        Resp<CrowdDTO> rs =dubboFactory.getCrowdProvider().getById(id);
        CrowdDTO crowdOrgl = rs.getData();
        if(crowdOrgl==null) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("用户组不存在");
            return result;
        }

        Resp<Long> nCountRt =dubboFactory.getCrowdProvider().countByName(name);
        Long nameCount = nCountRt.getData();
        if(name.equals(crowdOrgl.getName())) {
            nameCount-=1;
        }
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同名用户组");
            return result;
        }

        CrowdDTO crowdDTO = this.trans2CrowdDTO(crowdReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            crowdDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getCrowdProvider().updateCrowd(crowdDTO);
        return result;
    }

    @PostMapping("/enableOrNotCrowd")
    @ApiOperation("启动禁用用户组")
    public Resp<Void> enableOrNotCrowd(@RequestBody Req<EnableReq> req){
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
            dubboFactory.getCrowdProvider().enableCrowd(id,operator);
        } else {
            //禁用
            dubboFactory.getCrowdProvider().disableCrowd(id,operator);
        }
        return Resp.success();
    }

    @PostMapping("/deleteCrowd")
    @ApiOperation("删除用户组")
    public Resp<Void> deleteCrowd(@RequestBody Req<EntityVO> req){
        EntityVO entityVO = req.getData();
        Integer id = entityVO.getId();
        if(id==null || id<=0) {
            return Resp.success();
        }
        UserDTO curUser = LoginContext.getLoginUser();
        Integer operatorId = curUser==null?null:curUser.getId();
        log.info("CrowdController deleteCrowd id={},operatorId={}",id,operatorId);
        dubboFactory.getCrowdProvider().deleteCrowd(id);
        return Resp.success();
    }

    @GetMapping("/listOptionalCrowd")
    @ApiOperation("查询可选用户组列表")
    public Resp<List<CrowdSimpleVO>> listOptionalCrowd(){
        Resp<List<CrowdDTO>> rt =dubboFactory.getCrowdProvider().listOptionalCrowd();
        List<CrowdSimpleVO> list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),CrowdSimpleVO.class);
        return Resp.data(list);
    }

    @GetMapping("/listOptionalCrowdForUser")
    @ApiOperation("添加用户时查询可选的用户组列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "机构id", required = false, dataType = "Integer", paramType = "query")
    })
    public Resp<List<CrowdSimpleVO>> listOptionalCrowdForUser(@RequestParam(value = "orgId",required = false) Integer orgId){

        UserDTO operator = LoginContext.getLoginUser();
        boolean isSuper =true;
        if(operator!=null ) {
            Resp<Boolean> superResult = dubboFactory.getUserProvider().isSuperUser(operator.getId());
            isSuper = superResult.getData()==null?false:superResult.getData();
            if(orgId==null ) {
                orgId = operator.getOrgId();
            }
        }
        List<CrowdSimpleVO> list = null;
        if(isSuper) {
            //超级管理员
            if(orgId==null) {
                Resp<List<CrowdDTO>> rt =dubboFactory.getCrowdProvider().listOptionalCrowd();
                list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),CrowdSimpleVO.class);
                return Resp.data(list);
            }
            Resp<List<CrowdDTO>> rt =dubboFactory.getCrowdProvider().listCrowdByOrg(orgId);
            List<CrowdDTO> dtoList = rt.getData()==null ? new ArrayList<CrowdDTO>(): rt.getData();
            Resp<List<CrowdDTO>> rt2 =dubboFactory.getCrowdProvider().listCrowdNotOrg();
            if(rt2.getData()!=null) {
                dtoList.addAll(rt2.getData());
            }
            list = TransUtil.transList(dtoList,CrowdSimpleVO.class);
            return Resp.data(list);
        } else {
            if(orgId==null) {
                return Resp.success();
            }
            Resp<List<CrowdDTO>> rt =dubboFactory.getCrowdProvider().listCrowdByOrg(orgId);
            list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),CrowdSimpleVO.class);
            return Resp.data(list);
        }
    }
}
