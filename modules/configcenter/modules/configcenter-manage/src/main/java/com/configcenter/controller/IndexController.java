package com.configcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.rbac.User;
import com.configcenter.service.manager.LogManager;
import com.configcenter.service.SessionManager;
import com.configcenter.service.rbac.UserService;
import com.configcenter.vo.OnlineUser;
import com.configcenter.vo.TreeNode;
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
import java.util.List;

/**
 * 入口访问类
 */
@Controller
public class IndexController {

    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index")
    public String indexView() {
        return  "index";
    }

    @RequestMapping(value = "/menuList")
    public void menuList(HttpServletRequest request,HttpServletResponse response)
    {
        JSONObject map = new JSONObject();

        OnlineUser user = getUserFromSession(request.getSession());

        if(user==null)
        {
            map.put("result",1);
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }

        List<TreeNode> list= userService.queryMenuList(user.getAccount());
        System.out.println(user.getAccount()+" menu list");

        map.put("list",list);
        map.put("result",0);

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    private OnlineUser getUserFromSession(HttpSession session)
    {
        String sessionKey = Constant.USER_SESSION_ACCOUNT;

        if(session.getAttribute(sessionKey)!=null)
        {
            String account =(String)session.getAttribute(sessionKey);

            OnlineUser onlineUser = SessionManager.getInstance().getOnlineUser(account);

            return onlineUser;
        }
        return null;
    }

    //修改密码
    @RequestMapping(value = "/changePwd")
    public void changePwd(HttpServletRequest request,HttpServletResponse response,
                          String pwdOld,String pwdNew)
    {
        JSONObject result = new JSONObject();

        if(StringUtils.isBlank(pwdNew))
        {
            result.put("result",1);
            result.put("msg","新秘密不能为空");
            HttpRenderUtil.renderJSON(result.toString(), response);
            return;
        }

        String sessionKey =  Constant.USER_SESSION_ACCOUNT;
        String account =( request.getSession().getAttribute(sessionKey)==null)?"": request.getSession().getAttribute(sessionKey).toString();

        if(StringUtils.isBlank(account))
        {
            result.put("result",1);
            result.put("msg","获取登陆态信息失败，请重登陆");
            HttpRenderUtil.renderJSON(result.toString(), response);
            return;
        }
        User user = userService.getByAccount(account);
        if(user==null)
        {
            result.put("result",1);
            result.put("msg","账号已不存在");
            HttpRenderUtil.renderJSON(result.toString(), response);
            return;
        }
        if(StringUtils.isBlank(pwdOld) || !user.getPassword().equals(pwdOld))
        {
            result.put("result",1);
            result.put("msg","原密码错误");
            HttpRenderUtil.renderJSON(result.toString(), response);
            return;
        }

        try {
            user.setPassword(pwdNew);
            userService.update(user);
            LogManager.log(SessionManager.getCurUser(), "修改密码");
            result.put("result", 0);
        }catch (Exception e)
        {
            logger.error("IndexController changePwd error,",e);
            result.put("result",1);
            result.put("msg","操作失败，请稍后重试");
        }
        HttpRenderUtil.renderJSON(result.toString(), response);
    }
}
