package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.model.param.EnableReq;
import com.manage.rbac.model.param.OrgReq;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 机构接口
 */
@RestController
@RequestMapping("/rbac/org")
@Api(value="机构接口",description="机构接口")
@Slf4j
public class OrgController extends BaseController {

    private static Logger logger= LoggerFactory.getLogger(OrgController.class);

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/listOrgPage")
    @ApiOperation("分页查询机构")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "机构名称", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "pageIndex", value = "第几页", required = false, dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Integer", paramType = "query"),
    })
    public Resp<PageVo<OrgVO>> listOrgPage(@RequestParam(value = "name",required = false) String name,
                                           @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
                                           @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize) {
        Resp<PageVo<OrganizationDTO>> rt =dubboFactory.getOrgProvider().listOrgPage(name,pageIndex,pageSize);
        PageVo<OrganizationDTO> pageInfo =rt.getData();

        PageVo<OrgVO> pageVo = new PageVo<OrgVO>();
        pageVo.setTotalCount(pageInfo.getTotalCount());
        pageVo.setList(TransUtil.transListWithJson(pageInfo.getList(),OrgVO.class));

        return Resp.data(pageVo);
    }

    private OrganizationDTO trans2OrgDTO(OrgReq  orgReq) {
        OrganizationDTO orgDTO = TransUtil.transEntity(orgReq,OrganizationDTO.class);
        orgDTO.setPostList(TransUtil.transList(orgReq.getPostList(),PostDTO.class));
        orgDTO.setCrowdList(TransUtil.transList(orgReq.getCrowdList(),CrowdDTO.class));
        return orgDTO;
    }

    @GetMapping("/getOrg")
    @ApiOperation("查询机构详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "机构Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<OrgVO> getOrg(@RequestParam(value = "id") Integer id){
        if(id==null || id<=0) {
            return Resp.success();
        }
        Resp<OrganizationDTO> rt =dubboFactory.getOrgProvider().getOrgDetail(id);
        OrganizationDTO dto= rt.getData();
        if(dto==null) {
            return Resp.success();
        }
        OrgVO orgVO = TransUtil.transEntityWithJson(dto,OrgVO.class);
        return Resp.data(orgVO);
    }

    @PostMapping("/addOrg")
    @ApiOperation("添加机构")
    public Resp<Void> addOrg(@RequestBody Req<OrgReq> req){
        logger.info("OrgController addOrg start..........");
        OrgReq orgReq = req.getData();
        if(orgReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        String name = orgReq.getName();
        if(StringUtils.isBlank(name)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Resp<Long> nCountRt =dubboFactory.getOrgProvider().countByName(name);
        Long nameCount = nCountRt.getData();
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同名机构");
            return result;
        }
        OrganizationDTO orgDTO = this.trans2OrgDTO(orgReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            orgDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getOrgProvider().addOrg(orgDTO);
        return result;
    }

    @PostMapping("/updateOrg")
    @ApiOperation("修改机构")
    public Resp<Void> updateOrg(@RequestBody Req<OrgReq> req){
        logger.info("OrgController updateOrg start..........");
        OrgReq orgReq = req.getData();
        if(orgReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        String name = orgReq.getName();
        if(StringUtils.isBlank(name)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Integer id = orgReq.getId();
        Resp<OrganizationDTO> rs =dubboFactory.getOrgProvider().getById(id);
        OrganizationDTO orglOrg = rs.getData();
        if(orglOrg==null) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("机构不存在");
            return result;
        }

        Resp<Long> nCountRt =dubboFactory.getOrgProvider().countByName(name);
        Long nameCount = nCountRt.getData();
        if(name.equals(orglOrg.getName())) {
            nameCount-=1;
        }
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同名机构");
            return result;
        }

        OrganizationDTO orgDTO = this.trans2OrgDTO(orgReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            orgDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getOrgProvider().updateOrg(orgDTO);
        return result;
    }

    @PostMapping("/enableOrNotOrg")
    @ApiOperation("启动禁用机构")
    public Resp<Void> enableOrNotOrg(@RequestBody Req<EnableReq> req){
        logger.info("OrgController enableOrNotOrg start..........");
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
            dubboFactory.getOrgProvider().enableOrg(id,operator);
        } else {
            //禁用
            dubboFactory.getOrgProvider().disableOrg(id,operator);
        }
        return Resp.success();
    }

    @GetMapping("/listOrg")
    @ApiOperation("查询机构列表")
    public Resp<List<OrgSimpleVO>> listOrg(){
        Resp<List<OrganizationDTO>> rt =dubboFactory.getOrgProvider().listAbleOrg();
        List<OrgSimpleVO> list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),OrgSimpleVO.class);
        return Resp.data(list);
    }

    @GetMapping("/listOptionalOrgForUser")
    @ApiOperation("添加用户时查询可选的机构列表")
    public Resp<List<OrgSimpleVO>> listOptionalOrgForUser(){

        UserDTO operator = LoginContext.getLoginUser();
        boolean isSuper =true;
        if(operator!=null ) {
            Resp<Boolean> superResult = dubboFactory.getUserProvider().isSuperUser(operator.getId());
            isSuper = superResult.getData()==null?false:superResult.getData();
        }
        if(isSuper) {
            //超级管理员可给人员选择任何机构
            Resp<List<OrganizationDTO>> rt =dubboFactory.getOrgProvider().listAbleOrg();
            List<OrgSimpleVO> list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),OrgSimpleVO.class);
            return Resp.data(list);
        }
        //一般用户只能添加同机构下人员
        Integer orgId = operator.getOrgId();
        Resp<OrganizationDTO> rt = dubboFactory.getOrgProvider().getOrgDetail(orgId);
        OrganizationDTO orgDTO = rt.getData();
        if(orgDTO== null) {
            Resp<List<OrgSimpleVO>> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("普通用户没所在机构,数据异常，请联系管理员");
            return result;
        }
        if(EnableEnum.FORBID.getValue()==orgDTO.getState()) {
            Resp<List<OrgSimpleVO>> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("机构被禁用，不能添加机构人员");
            return result;
        }
        OrgSimpleVO orgSimpleVO = TransUtil.transEntity(orgDTO,OrgSimpleVO.class);
        List<OrgSimpleVO> list = new ArrayList<OrgSimpleVO>(1);
        list.add(orgSimpleVO);
        return Resp.data(list);
    }
}
