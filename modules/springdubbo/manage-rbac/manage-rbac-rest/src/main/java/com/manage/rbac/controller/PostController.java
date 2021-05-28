package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.*;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.model.param.EnableReq;
import com.manage.rbac.model.param.PostReq;
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
 * 岗位接口
 */
@RestController
@RequestMapping("/rbac/post")
@Api(value="岗位接口",description="岗位接口")
@Slf4j
public class PostController extends BaseController {

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/listPostPage")
    @ApiOperation("分页查询岗位")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "post", value = "岗位", required = false, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "pageIndex", value = "第几页", required = false, dataType = "Integer", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Integer", paramType = "query"),
    })
    public Resp<PageVo<PostVO>> listPostPage(@RequestParam(value = "post",required = false) String post,
                                             @RequestParam(value = "pageIndex",required = false,defaultValue = "1") Integer pageIndex,
                                             @RequestParam(value = "pageSize",required = false,defaultValue = "20") Integer pageSize) {

        Resp<PageVo<PostDTO>> rt =dubboFactory.getPostProvider().listPostPage(post,pageIndex,pageSize);
        PageVo<PostDTO> pageInfo =rt.getData();

        PageVo<PostVO> pageVo = new PageVo<PostVO>();
        pageVo.setTotalCount(pageInfo.getTotalCount());
        pageVo.setList(TransUtil.transListWithJson(pageInfo.getList(), PostVO.class));

        return Resp.data(pageVo);
    }

    @GetMapping("/getPost")
    @ApiOperation("查询岗位详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "岗位Id", required = true, dataType = "Integer", paramType = "query")
    })
    public Resp<PostVO> getPost(@RequestParam(value = "id") Integer id){

        log.info("PostController getPost start,id={}",id);
        if(id==null || id<=0) {
            return Resp.success();
        }
        Resp<PostDTO> rt =dubboFactory.getPostProvider().getPostDetail(id);
        PostDTO dto= rt.getData();
        if(dto==null) {
            return Resp.success();
        }
        PostVO postVO = TransUtil.transEntityWithJson(dto,PostVO.class);
        log.info("PostController getPost end,postVO={}",postVO);
        return Resp.data(postVO);
    }

    @PostMapping("/addPost")
    @ApiOperation("添加岗位")
    public Resp<Void> addPost(@RequestBody Req<PostReq> req){
        PostReq postReq = req.getData();
        if(postReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        String name = postReq.getName();
        String code = postReq.getCode();
        if(StringUtils.isAnyBlank(name,code)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Resp<Long> nCountRt =dubboFactory.getPostProvider().countByName(name);
        Long nameCount = nCountRt.getData();
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同名岗位");
            return result;
        }
        Resp<Long> cCountRt =dubboFactory.getPostProvider().countByCode(code);
        Long codeCount =cCountRt.getData();
        if(codeCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同编码岗位");
            return result;
        }

        PostDTO postDTO = this.trans2PostDTO(postReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            postDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getPostProvider().addPost(postDTO);
        return result;
    }

    private PostDTO trans2PostDTO(PostReq  postReq) {
        PostDTO postDTO = TransUtil.transEntity(postReq,PostDTO.class);
        postDTO.setOrgList(TransUtil.transList(postReq.getOrgList(), OrganizationDTO.class));
        postDTO.setRoleList(TransUtil.transList(postReq.getRoleList(), RoleDTO.class));
        return postDTO;
    }

    @PostMapping("/updatePost")
    @ApiOperation("修改岗位")
    public Resp<Void> updatePost(@RequestBody Req<PostReq> req){
        log.info("PostController updatePost start..........");
        PostReq postReq = req.getData();
        if(postReq==null) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        String name = postReq.getName();
        String code = postReq.getCode();
        if(StringUtils.isAnyBlank(name,code)) {
            //参数有空
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }
        Integer id = postReq.getId();
        Resp<PostDTO> rs =dubboFactory.getPostProvider().getById(id);
        PostDTO orglPost = rs.getData();
        if(orglPost==null) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("岗位不存在");
            return result;
        }

        Resp<Long> nCountRt =dubboFactory.getPostProvider().countByName(name);
        Long nameCount = nCountRt.getData();
        if(name.equals(orglPost.getName())) {
            nameCount-=1;
        }
        if(nameCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同名岗位");
            return result;
        }
        Resp<Long> cCountRt =dubboFactory.getPostProvider().countByCode(code);
        Long codeCount =cCountRt.getData();
        if(code.equals(orglPost.getCode())) {
            codeCount-=1;
        }
        if(codeCount>0L) {
            Resp<Void> result =Resp.error(RestCode.V_PARAM_ERROR);
            result.getHeader().setMsg("已存在同编码岗位");
            return result;
        }

        PostDTO postDTO = this.trans2PostDTO(postReq);
        UserDTO operator = LoginContext.getLoginUser();
        if(operator!=null) {
            postDTO.setOperator(new OperatorDTO(operator.getId(), operator.getNickname()));
        }
        Resp<Void> result =dubboFactory.getPostProvider().updatePost(postDTO);
        return result;
    }

    @PostMapping("/enableOrNotPost")
    @ApiOperation("启动禁用岗位")
    public Resp<Void> enableOrNotPost(@RequestBody Req<EnableReq> req){
        log.info("PostController enableOrNotPost start..........");
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
            dubboFactory.getPostProvider().enablePost(id,operator);
        } else {
            //禁用
            dubboFactory.getPostProvider().disablePost(id,operator);
        }
        return Resp.success();
    }

    @GetMapping("/listOptionalPost")
    @ApiOperation("查询可选岗位列表")
    public Resp<List<PostSimpleVO>> listOptionalPost(){
        Resp<List<PostDTO>> rt =dubboFactory.getPostProvider().listOptionalPost();
        List<PostSimpleVO> list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),PostSimpleVO.class);
        return Resp.data(list);
    }

    @GetMapping("/listOptionalPostForUser")
    @ApiOperation("添加用户时查询可选的岗位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "机构id", required = false, dataType = "Integer", paramType = "query")
    })
    public Resp<List<PostSimpleVO>> listOptionalPostForUser(@RequestParam(value = "orgId",required = false) Integer orgId){

        UserDTO operator = LoginContext.getLoginUser();
        boolean isSuper =true;
        if(operator!=null ) {
            Resp<Boolean> superResult = dubboFactory.getUserProvider().isSuperUser(operator.getId());
            isSuper = superResult.getData()==null?false:superResult.getData();
            if(orgId==null ) {
                orgId = operator.getOrgId();
            }
        }
        List<PostSimpleVO> list = null;
        if(isSuper) {
            //超级管理员
            if(orgId==null) {
                //没指定机构就查全部
                Resp<List<PostDTO>> rt = dubboFactory.getPostProvider().listOptionalPost();
                list = (rt == null || rt.getData() == null) ? Collections.EMPTY_LIST : TransUtil.transList(rt.getData(), PostSimpleVO.class);
                return Resp.data(list);
            }
            Resp<List<PostDTO>> rt =dubboFactory.getPostProvider().listPostByOrg(orgId);
            List<PostDTO> dtoList = rt.getData()==null ? new ArrayList<PostDTO>(): rt.getData();
            Resp<List<PostDTO>> rt2 =dubboFactory.getPostProvider().listPostNotOrg();
            if(rt2.getData()!=null) {
                dtoList.addAll(rt2.getData());
            }
            list = TransUtil.transList(dtoList,PostSimpleVO.class);
            return Resp.data(list);
        } else {
            //一般用户只能添加同机构下岗位
            if(orgId==null) {
                //没指定机构直接返回空列表
                return Resp.success();
            }
            Resp<List<PostDTO>> rt =dubboFactory.getPostProvider().listPostByOrg(orgId);
            list =( rt==null || rt.getData()==null) ? Collections.EMPTY_LIST: TransUtil.transList(rt.getData(),PostSimpleVO.class);
            return Resp.data(list);
        }
    }
}
