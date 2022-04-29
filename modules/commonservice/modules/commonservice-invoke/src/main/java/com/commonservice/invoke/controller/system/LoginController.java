package com.commonservice.invoke.controller.system;

import com.commonservice.invoke.model.entity.system.Menu;
import com.commonservice.invoke.model.param.LoginParam;
import com.commonservice.invoke.model.vo.*;
import com.commonservice.invoke.service.system.MenuService;
import com.commonservice.invoke.util.MenuRouterUtil;
import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Api(description = "登录接口")
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public Resp<LoginResultVo> login(@RequestBody LoginParam loginParam) {
        String token = UUID.randomUUID().toString();
        LoginResultVo loginResultVo = new LoginResultVo();
        loginResultVo.setToken(token);
        return Resp.data(loginResultVo);
    }

    @GetMapping("/getInfo")
    @ApiOperation("获取登录用户信息接口")
    public Resp<UserLoginVo> getInfo() {
        UserVo userVo = this.genUser();
        // 角色集合
        Set<String> roles = Collections.emptySet();
        // 权限集合
        Set<String> permissions =  Collections.emptySet();

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUser(userVo);
        userLoginVo.setRoles(roles);
        userLoginVo.setPermissions(permissions);

        return Resp.data(userLoginVo);
    }

    private UserVo genUser() {
        UserVo userVo = new UserVo();
        userVo.setUserId(1L);
        userVo.setUserName("666");
        userVo.setNickName("666");
        userVo.setAvatar("");
        userVo.setEmail("");
        userVo.setLoginIp("");
        userVo.setSex(1);
        userVo.setStatus(0);
        return  userVo;
    }

    @GetMapping("/getRouters")
    @ApiOperation("获取登录用户菜单路由信息")
    public Resp<List<RouterVo>> getRouters()
    {
        Long userId = 1L;
        List<Menu> list = menuService.queryMenuTreeByUserId(userId);
        List<RouterVo> rtList = MenuRouterUtil.buildMenuRouter(list);
        return Resp.data(rtList);
    }

    @PostMapping("/logout")
    @ApiOperation("登出接口")
    public Resp<Void> logout() {
        return Resp.success("");
    }
}
