package com.manageplat.controller;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.manageplat.service.AuthManager;
import com.util.PropertiesLoader;
import com.util.web.HttpRenderUtil;
import com.util.web.RequestUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static String USER_SESSION_ID="user_session_id";

    //用户配置
    private static String allUserPath="userinfo.properties";

    public Properties getAllUser() throws Exception
    {
        return PropertiesLoader.load(allUserPath);
    }

    /**用户登录**/
    @RequestMapping("login")
    public void login(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
        JSONObject json = new JSONObject();
        try
        {
            String username = RequestUtil.getParameter(request,"username");
            String passwd = RequestUtil.getParameter(request, "passwd");

            Properties allUser =getAllUser();

            if(validate(username, passwd, allUser))
            {
                logger.info("----------user:" + username + " login success-------------");
                saveToSession(request.getSession(), username);
                json.put("userName", username);
                json.put("flag", "true");
            }
            else
            {
                logger.info("----------user:" + username+ " login fail-------------");
                json.put("flag", "false");
            }
        }
        catch(Exception e)
        {
            logger.error("login error,", e);
            json.put("flag", "false");
        }

        HttpRenderUtil.renderJSON(json.toString(), response);
    }

    // 保存到session
    private void saveToSession(HttpSession session,String userName)
    {
        String sessionKey = USER_SESSION_ID;
        if(session.getAttribute(sessionKey)==null)
        {
            session.setAttribute(sessionKey, userName);
        }
    }

    // 验证用户是否存在
    private boolean validate(String userName, String password,Properties userinfo) throws Exception
    {
        if(userName == null || password == null || userinfo==null)
        {
            return false;
        }
        Object pwd = userinfo.get(userName);
        if(pwd==null)
        {
            return false;
        }

        if(password.equals(pwd.toString()))
        {
            AuthManager.getInstance().assignAuth(userName);
            return true;
        }
        return false;
    }

    /**登出**/
    @RequestMapping("logout")
    public void logout(HttpServletRequest request,HttpServletResponse response)
    {
        String sessionKey = "";

        logger.info("----------user:"+ request.getSession().getAttribute(sessionKey) + " login out------------");
        request.getSession().invalidate();

        JSONObject result = new JSONObject();
        result.put("flag", 0);

        HttpRenderUtil.renderJSON(result.toString(), response);
    }

    /**首页**/
    @RequestMapping("mainPage")
    public void mainPage(HttpServletRequest request,HttpServletResponse response)
    {
        JSONObject main = new JSONObject();

        String userName = request.getSession().getAttribute(USER_SESSION_ID).toString();
        main.put("userName", userName);

        List<Map<String, List<String>>> menus = AuthManager.getInstance().getUserMenus(userName);
        main.put("menus", menus);

        HttpRenderUtil.renderJSON(main.toString(), response);
    }

}
