package com.configcenter.controller.rbac;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.rbac.User;
import com.configcenter.service.rbac.UserService;
import com.configcenter.vo.CommonQueryVo;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户访问类
 */
@Controller
@RequestMapping(value = "/rbac")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String PREFIX = "/rbac/";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userView")
    public String userView() {

        return PREFIX + "userView";
    }

    @RequestMapping(value = "/queryUserPage")
    public void queryUserPage(HttpServletResponse response,CommonQueryVo queryVo)
    {
        if(queryVo==null)
        {
            queryVo =new CommonQueryVo();
        }
        queryVo.genPage();

        List<User> list = userService.queryList(queryVo);

        int count =userService.count(queryVo);

        JSONObject map = new JSONObject();

        map.put(Constant.DATAGRID_ROWS,list);
        map.put(Constant.DATAGRID_TOTAL,count);
        map.put("page",queryVo.getPage());

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //增改用户
    @RequestMapping(value = "/addOrUptUser")
    public void addOrUptUser(HttpServletResponse response,User user)
    {

        JSONObject map = new JSONObject();
        boolean isAdd=(user.getId()==null)?true:false;

        if(isAdd && StringUtils.isBlank(user.getPassword()))
        {
            map.put("result",1);
            map.put("msg","新用户密码不能为空");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return ;
        }
        try{
            if(isAdd)
            {
                userService.add(user);
            }else
            {
                userService.update(user);
            }
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("addOrUptUser error,user="+user,e);
            map.put("result",1);
            map.put("msg","用户增改异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //删除用户
    @RequestMapping(value = "/delUser")
    public void delUser(HttpServletResponse response,Integer id)
    {
        JSONObject map = new JSONObject();
        try{
            userService.delUserAndRela(id);
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("delUser error,id="+id,e);
            map.put("result",1);
            map.put("msg","用户删除异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //禁用或启用用户
    @RequestMapping(value = "/useableOrNoUser")
    public void useableOrNoUser(HttpServletResponse response,Integer id,Integer flag)
    {

        JSONObject map = new JSONObject();
        if(id==null || flag==null)
        {
            map.put("result",1);
            map.put("msg","参数错误");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }
        User user = new User();
        user.setId(id);
        if(flag==1)
        {
            //可用
            user.setUseable(1);
        }else
        {
            //不可用
            user.setUseable(0);
        }
        try{
            userService.update(user);
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("useableOrNoUser error,user="+user,e);
            map.put("result",1);
            map.put("msg","用户禁用或启用异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //选择角色界面
    @RequestMapping(value = "/selectRole")
    public String selectRole(HttpServletResponse response,Integer userId)
    {
        return PREFIX+"selectRole";
    }

    //授权用户
    @RequestMapping(value = "/authUser")
    public void authUser(HttpServletResponse response,Integer userId,String roleIds)
    {
        JSONObject map = new JSONObject();
        try{
            userService.authUser(userId,roleIds);

            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("authUser error,userId="+userId,e);
            map.put("result",1);
            map.put("msg","授权用户异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
