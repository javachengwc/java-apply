package com.configcenter.controller.rbac;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.rbac.Role;
import com.configcenter.service.rbac.RoleService;
import com.configcenter.vo.CommonQueryVo;
import com.configcenter.vo.RoleVo;
import com.util.web.HttpRenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色访问类
 */
@Controller
@RequestMapping(value = "/rbac")
public class RoleController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    private static final String PREFIX = "/rbac/";

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/roleView")
    public String roleView() {

        return PREFIX + "roleView";
    }

    @RequestMapping(value = "/queryRolePage")
    public void queryRolePage(HttpServletResponse response,CommonQueryVo queryVo)
    {
        if(queryVo==null)
        {
            queryVo =new CommonQueryVo();
        }
        queryVo.genPage();

        List<Role> list = roleService.queryList(queryVo);

        int count =roleService.count(queryVo);

        JSONObject map = new JSONObject();

        map.put(Constant.DATAGRID_ROWS,list);
        map.put(Constant.DATAGRID_TOTAL,count);
        map.put("page",queryVo.getPage());

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //增改角色
    @RequestMapping(value = "/addOrUptRole")
    public void addOrUptRole(HttpServletResponse response,Role role)
    {

        JSONObject map = new JSONObject();
        boolean isAdd=(role.getId()==null)?true:false;

        try{
            if(isAdd)
            {
                Integer id =roleService.add(role);
                System.out.println("-----new role id="+id);
            }else
            {
                roleService.update(role);
            }
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("addOrUptRole error,role="+role,e);
            map.put("result",1);
            map.put("msg","角色增改异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //删除角色
    @RequestMapping(value = "/delRole")
    public void delRole(HttpServletResponse response,Integer id)
    {
        JSONObject map = new JSONObject();
        try{
            roleService.delRoleAndRela(id);
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("delRole error,id="+id,e);
            map.put("result",1);
            map.put("msg","角色删除异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //选择资源界面
    @RequestMapping(value = "/selectRs")
    public String selectRs(HttpServletResponse response,Integer roleId)
    {
        return PREFIX+"selectResource";
    }

    //关联资源
    @RequestMapping(value = "/roleRelaResource")
    public void roleRelaResource(HttpServletResponse response,Integer roleId,String resourceIds)
    {
        JSONObject map = new JSONObject();
        try{
            roleService.roleRelaResource(roleId,resourceIds);

            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("roleRelaResource error,roleId="+roleId,e);
            map.put("result",1);
            map.put("msg","角色关联资源异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //获取带用户选择标记的角色列表
    @RequestMapping(value = "/getUserSelectRole")
    public void getUserSelectRole(HttpServletResponse response,Integer userId)
    {

        JSONObject map = new JSONObject();
        try{
            List<RoleVo> list = roleService.getUserSelectRole(userId);
            map.put("result",0);
            map.put("list",list);

        }catch(Exception e)
        {
            logger.error("getUserSelectRole error,userId="+userId,e);
            map.put("result",1);
            map.put("msg","处理异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
