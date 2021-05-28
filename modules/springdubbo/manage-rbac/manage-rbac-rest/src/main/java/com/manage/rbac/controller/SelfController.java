package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.SystemMenuDTO;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.model.vo.*;
import com.manage.rbac.shiro.LoginContext;
import com.model.base.Resp;
import com.springdubbo.common.RestCode;
import com.springdubbo.controller.BaseController;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 个人信息接口
 */
@RestController
@RequestMapping("/self")
@Api(value="个人信息接口",description="个人信息接口")
@Slf4j
public class SelfController extends BaseController {

    @Autowired
    private DubboFactory dubboFactory;

    @GetMapping("/getSelfInfo")
    @ApiOperation("查询个人信息")
    public Resp<UserVO> getSelfInfo(){
        log.info("SelfController getSelfInfo start......");
        
        UserDTO userDTO=LoginContext.getLoginUser();
        if(userDTO==null) {
            return Resp.error(RestCode.V_NOT_LOGIN);
        }
        Integer userId = userDTO.getId();
        Resp<UserDTO> rt = dubboFactory.getUserProvider().getUserDetail(userId);
        userDTO = rt.getData();
        UserVO userVO = TransUtil.transEntityWithJson(userDTO,UserVO.class);
        Resp<Boolean> superRt = dubboFactory.getUserProvider().isSuperUser(userId);
        boolean isSuper = superRt.getData();
        userVO.setSuperUser(isSuper?1:0);
        return Resp.data(userVO);
    }

    @GetMapping("/listSelfMenu")
    @ApiOperation("查询个人系统菜单树")
    public Resp<List<SystemMenuVo>> listSelfMenu(){
        UserDTO userDTO=LoginContext.getLoginUser();
        if(userDTO==null) {
            return Resp.error(RestCode.V_NOT_LOGIN);
        }
        if(userDTO.getState() == EnableEnum.FORBID.getValue()) {
            return Resp.error(RestCode.V_NOT_LOGIN);
        }
        Integer userId = userDTO.getId();
        Resp<List<SystemMenuDTO>> rt = dubboFactory.getMenuProvider().listSystemMenuByUser(userId);
        List<SystemMenuVo> list= TransUtil.transListWithJson(rt.getData(),SystemMenuVo.class);
        return Resp.data(list);
    }
}
