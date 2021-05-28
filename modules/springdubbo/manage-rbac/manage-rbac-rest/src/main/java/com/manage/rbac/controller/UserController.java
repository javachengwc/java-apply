package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.model.param.EnableReq;
import com.manage.rbac.model.param.UserReq;
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

@RestController
@RequestMapping("/user")
@Api(value="用户接口",description="用户接口")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/listUserPage")
    @ApiOperation("分页查询用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user", value = "人员", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "orgId", value = "机构Id", required = false, dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "orgName", value = "机构名称", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "pageIndex", value = "第几页", required = false, dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Integer", paramType = "query"),
    })
    public Resp<PageVo<UserVO>> listUserPage(@RequestParam(value = "user",required = false) String user,
                                             @RequestParam(value = "orgId",required = false) Integer orgId,
                                             @RequestParam(value = "orgName",required = false) String orgName,
                                             @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
                                             @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize) {
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null && orgId==null ) {
            Resp<Boolean> superResult = dubboFactory.getUserProvider().isSuperUser(operator.getId());
            boolean isSuper = superResult.getData()==null?false:superResult.getData();
            if(!isSuper) {
                //非超级管理员，并且分页条件中没传orgId, 则只能查询同机构下的人员
                orgId = operator.getOrgId();
            }
        }

        Resp<PageVo<UserDTO>> rt =dubboFactory.getUserProvider().listUserPage(user,orgId,orgName,pageIndex,pageSize);
        PageVo<UserDTO> pageInfo =rt.getData();

        PageVo<UserVO> pageVo = new PageVo<UserVO>();
        pageVo.setTotalCount(pageInfo.getTotalCount());
        pageVo.setList(TransUtil.transList(pageInfo.getList(),UserVO.class));

        return Resp.data(pageVo);
    }

    @GetMapping("/getUser")
    @ApiOperation("查询用户详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<UserVO> getUser(@RequestParam(value = "id") Integer id){
        if(id==null || id<=0) {
            return Resp.success();
        }
        Resp<UserDTO> rt =dubboFactory.getUserProvider().getUserDetail(id);
        UserDTO dto= rt.getData();
        if(dto==null) {
            return Resp.success();
        }
        UserVO userVO = TransUtil.transEntityWithJson(dto,UserVO.class);
        return Resp.data(userVO);
    }

    @PostMapping("/addUser")
    @ApiOperation("添加用户")
    public Resp<Void> addUser(@RequestBody Req<UserReq> req){
        log.info("UserController addUser start..........");
        UserReq userReq = req.getData();
        if(userReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Long uid = userReq.getUid();
        String name = userReq.getName();
        String nickname = userReq.getNickname();

        if(uid==null || StringUtils.isAnyBlank(name,nickname)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Resp<Long> uidCountRt =dubboFactory.getUserProvider().countByUid(uid);
        Long uidCount = uidCountRt.getData();
        if(uidCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同平台账号");
            return result;
        }
        Resp<Long> nnCountRt =dubboFactory.getUserProvider().countByNickname(nickname);
        Long nnCount = nnCountRt.getData();
        if(nnCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同网名账号");
            return result;
        }
        Integer orgId = userReq.getOrgId();
        if(orgId!=null ) {
            Resp<OrganizationDTO> orgRt= dubboFactory.getOrgProvider().getById(orgId);
            OrganizationDTO orgDTO = orgRt.getData();
            if(orgDTO==null) {
                Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
                result.getHeader().setMsg("机构不存在");
                return result;
            }
            if(EnableEnum.FORBID.getValue()==orgDTO.getState()) {
                Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
                result.getHeader().setMsg("机构被禁用，不能添加机构人员");
                return result;
            }
        }
        UserDTO userDTO = this.trans2UserDTO(userReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            userDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getUserProvider().addUser(userDTO);
        return result;
    }

    private UserDTO trans2UserDTO(UserReq  userReq) {
        UserDTO userDTO = TransUtil.transEntity(userReq,UserDTO.class);
        userDTO.setPostList(TransUtil.transList(userReq.getPostList(), PostDTO.class));
        userDTO.setCrowdList(TransUtil.transList(userReq.getCrowdList(), CrowdDTO.class));
        return userDTO;
    }

    @PostMapping("/updateUser")
    @ApiOperation("修改用户")
    public Resp<Void> updateUser(@RequestBody Req<UserReq> req){
        log.info("UserController updateUser start..........");
        UserReq userReq = req.getData();
        if(userReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        Long uid = userReq.getUid();
        String name = userReq.getName();
        String nickname = userReq.getNickname();

        if(uid==null || StringUtils.isAnyBlank(name,nickname)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Integer id = userReq.getId();
        Resp<UserDTO> rs =dubboFactory.getUserProvider().getById(id);
        UserDTO userOrgl = rs.getData();
        if(userOrgl==null) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("账号不存在");
            return result;
        }

        Resp<Long> uidCountRt =dubboFactory.getUserProvider().countByUid(uid);
        Long uidCount = uidCountRt.getData();
        if(uid==userOrgl.getUid().longValue()) {
            uidCount-=1;
        }
        if(uidCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同平台账号");
            return result;
        }

        Resp<Long> nnCountRt =dubboFactory.getUserProvider().countByNickname(nickname);
        Long nnCount = nnCountRt.getData();
        if(nickname.equals(userOrgl.getNickname())) {
            nnCount-=1;
        }
        if(nnCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同网名账号");
            return result;
        }

        UserDTO userDTO = this.trans2UserDTO(userReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            userDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getUserProvider().updateUser(userDTO);
        return result;
    }

    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    public Resp<Void> deleteUser(@RequestBody Req<EntityVO> req){
        EntityVO entityVO = req.getData();
        Integer id = entityVO.getId();
        if(id==null || id<=0) {
            return Resp.success();
        }
        UserDTO curUser = LoginContext.getLoginUser();
        OperatorDTO operator=null;
        if(curUser!=null) {
            operator =new OperatorDTO(curUser.getId(), curUser.getNickname());
        }
        dubboFactory.getUserProvider().deleteUser(id,operator);
        return Resp.success();
    }

    @PostMapping("/enableOrNotUser")
    @ApiOperation("启动禁用用户")
    public Resp<Void> enableOrNotUser(@RequestBody Req<EnableReq> req){
        log.info("UserController enableOrNotUser start..........");
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
            dubboFactory.getUserProvider().enableUser(id,operator);
        } else {
            //禁用
            dubboFactory.getUserProvider().disableUser(id,operator);
        }
        return Resp.success();
    }

    @GetMapping("/listUserByCdn")
    @ApiOperation("根据手机号或网名查询账号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "condition", value = "条件", required = true, dataType = "String", paramType = "query")
    })
    public Resp<List<UserSimpleVO>> listUserByCdn(@RequestParam(value = "condition",required = true) String condition) {
        if(StringUtils.isBlank(condition)) {
            return Resp.data(Collections.EMPTY_LIST);
        }
        UserDTO operator = LoginContext.getLoginUser();
        boolean isSuper =true;
        if(operator!=null ) {
            Resp<Boolean> superResult = dubboFactory.getUserProvider().isSuperUser(operator.getId());
            isSuper = superResult.getData()==null?false:superResult.getData();
        }
        Integer orgId = operator==null?null:operator.getOrgId();
        if(isSuper) {
            orgId=null;
        }
        Resp<List<UserDTO>> rt =dubboFactory.getUserProvider().listUserByMobileOrNickname(condition,orgId);
        List<UserSimpleVO> list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),UserSimpleVO.class);
        return Resp.data(list);
    }

}
