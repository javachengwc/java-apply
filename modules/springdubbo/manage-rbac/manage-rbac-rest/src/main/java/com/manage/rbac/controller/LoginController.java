package com.manage.rbac.controller;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.model.vo.LoginVO;
import com.manage.rbac.model.vo.UserVO;
import com.manage.rbac.shiro.ShiroConstant;
import com.manage.rbac.shiro.ShiroManager;
import com.model.base.Req;
import com.model.base.Resp;
import com.springdubbo.common.RestCode;
import com.springdubbo.controller.BaseController;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * 登陆接口
 */
@RestController
@Api(value="登陆接口",description="登陆接口")
@Slf4j
public class LoginController extends BaseController {

    private static Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ShiroManager shiroManager;

    @Autowired
    private DubboFactory dubboFactory;

    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/login")
    public Resp<UserVO> login(@RequestBody Req<LoginVO> req) {
        LoginVO loginVO = req.getData();
        String mobile = loginVO==null?null:loginVO.getMobile();
        String passwd= loginVO==null?null:loginVO.getPasswd();
        logger.info("LoginController login start,mobile={},passwd={}",mobile,passwd);
        if(StringUtils.isBlank(mobile)) {
            return Resp.error(RestCode.V_PARAMS_IS_NULL);
        }

        Resp<UserDTO> rt = dubboFactory.getUserProvider().getUserByMobile(mobile);
        UserDTO user = rt.getData();
        if(user==null) {
            return Resp.error(RestCode.V_NOT_LOGIN);
        }
        Resp<Session> sessionResult = shiroManager.login(mobile,passwd);
        if (!sessionResult.isSuccess()) {
            //登录失败
            return Resp.error(RestCode.V_NOT_LOGIN);
        }

        Session session = sessionResult.getData();

        String token =session.getId().toString();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader(ShiroConstant.TOKEN, token);

        UserVO userVo = TransUtil.transEntity(user,UserVO.class);
        return Resp.data(userVo);

    }

    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/logout")
    public Resp<Void> logout(@RequestBody Req<Void> req) {
        shiroManager.logout();
        return Resp.success();
    }
}
