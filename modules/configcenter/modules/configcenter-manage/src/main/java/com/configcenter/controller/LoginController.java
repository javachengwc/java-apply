package com.configcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.rbac.User;
import com.configcenter.service.LogManager;
import com.configcenter.service.SessionManager;
import com.configcenter.service.rbac.UserService;
import com.configcenter.vo.OnlineUser;
import com.util.date.DateUtil;
import com.util.encrypt.EncodeUtil;
import com.util.http.UrlUtil;
import com.util.web.CookieUtil;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登陆登出访问类
 */
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public void login(HttpServletRequest request,HttpServletResponse response,
                      String account,String passwd )
    {
        JSONObject map = new JSONObject();

        if(StringUtils.isBlank(account))
        {
            map.put("result",1);
            map.put("msg","账号不能为空");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }

        User user =userService.getByAccount(account);

        if(user ==null)
        {
            map.put("result",1);
            map.put("msg","账号不存在");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }

        String realPwd = user.getPassword();

        if(StringUtils.isBlank(passwd) || !realPwd.equals(passwd))
        {
            map.put("result",1);
            map.put("msg","密码错误");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }

        if(user.getUseable()==null || user.getUseable()!=1)
        {
            map.put("result",1);
            map.put("msg","账号不可用");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }

        saveToSession(request.getSession(),user);
        //创建会话cookie
        String domain = UrlUtil.getDomain(request.getRequestURL().toString());
        CookieUtil.setCookie(response,Constant.COOKIE_USERNAME, EncodeUtil.urlEncode(user.getName()),domain,"/",0);

        OnlineUser onlineUser = SessionManager.getInstance().getOnlineUser(account);
        LogManager.log(onlineUser,"用户登陆");

        map.put("result",0);
        map.put("msg","登陆成功");
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    // 保存到session
    private void saveToSession(HttpSession session,User user)
    {
        String sessionKey = Constant.USER_SESSION_ACCOUNT;
        if(session.getAttribute(sessionKey)==null)
        {
            session.setAttribute(sessionKey, user.getAccount());
            OnlineUser onlineUser =transOnline(user);
            SessionManager.getInstance().addSession(onlineUser);
        }
    }

    public OnlineUser transOnline(User user)
    {
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setId(user.getId());
        onlineUser.setAccount(user.getAccount());
        onlineUser.setName(user.getName());
        int now =DateUtil.getNowInt();
        onlineUser.setLoginTime(now);
        onlineUser.setLastedHeartbeat(now);

        return onlineUser;
    }

    //登出
    @RequestMapping("/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response)
    {
        String sessionKey =  Constant.USER_SESSION_ACCOUNT;
        String account =( request.getSession().getAttribute(sessionKey)==null)?"": request.getSession().getAttribute(sessionKey).toString();
        logger.info("----------user:"+ account + " login out------------");

        if(!StringUtils.isBlank(account)) {
            OnlineUser onlineUser = SessionManager.getInstance().getOnlineUser(account);
            SessionManager.getInstance().delSession(account);

            LogManager.log(onlineUser,"用户登出");
        }
        request.getSession().invalidate();

        JSONObject result = new JSONObject();
        result.put("result", 0);

        HttpRenderUtil.renderJSON(result.toString(), response);
    }
}
