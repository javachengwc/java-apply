package com.configcenter.controller.rbac;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.rbac.Resource;
import com.configcenter.service.rbac.ResourceService;
import com.configcenter.vo.CommonQueryVo;
import com.configcenter.vo.NodeState;
import com.configcenter.vo.ResourceVo;
import com.configcenter.vo.TreeNode;
import com.util.BeanCopyUtil;
import com.util.web.HttpRenderUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 资源访问类
 */
@Controller
@RequestMapping(value = "/rbac")
public class ResourceController {

    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    private static final String PREFIX = "/rbac/";

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/resourceView")
    public String resourceView() {

        return PREFIX+"resourceView";
    }

    @RequestMapping(value = "/queryResourcePage")
    public void queryResourcePage(HttpServletResponse response,CommonQueryVo queryVo)
    {
        if(queryVo==null)
        {
            queryVo =new CommonQueryVo();
        }
        queryVo.genPage();

        List<Resource> list = resourceService.queryList(queryVo);

        int count =resourceService.count(queryVo);

        JSONObject map = new JSONObject();

        map.put(Constant.DATAGRID_ROWS,list);
        map.put(Constant.DATAGRID_TOTAL,count);
        map.put("page",queryVo.getPage());

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    @RequestMapping(value = "/queryTreeGridResource")
    public void queryTreeGridResource(HttpServletResponse response)
    {
        JSONObject map = new JSONObject();
        List<Resource> list =  resourceService.queryAll();

        List<ResourceVo> rows = new ArrayList<ResourceVo>();

        if(list!=null)
        {
            try{
                for (Resource resource : list) {
                    ResourceVo resourceVo = new ResourceVo();

                    BeanUtils.copyProperties(resourceVo, resource);
                    resourceVo.set_parentId(resource.getParentId());
                    if(resourceVo.getParentId() == 0){
                        resourceVo.setState( NodeState.CLOSED.getValue());
                    }
                    rows.add(resourceVo);
                }
            }catch(Exception e)
            {
                logger.error("----------queryTreeGridResource error,",e);
            }
            list.clear();
        }

        map.put(Constant.DATAGRID_ROWS,rows);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    @RequestMapping(value = "/addResource")
    public void addResource(HttpServletResponse response,Resource resource)
    {
        if(resource.getParentId()==null)
        {
            resource.setParentId(0);
        }
        JSONObject map = new JSONObject();
        try{
            resourceService.add(resource);
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("addResource error,resource="+resource,e);
            map.put("result",1);
            map.put("msg","资源新增异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    @RequestMapping(value = "/uptResource")
    public void uptResource(HttpServletResponse response,Resource resource)
    {
        JSONObject map = new JSONObject();
        try{
            resourceService.update(resource);
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("uptResource error,resource="+resource,e);
            map.put("result",1);
            map.put("msg","资源修改异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    @RequestMapping(value = "/delResource")
    public void delResource(HttpServletResponse response,Integer id)
    {
        JSONObject map = new JSONObject();
        try{
            resourceService.delResourceAndRela(id);
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("delResource error,id="+id,e);
            map.put("result",1);
            map.put("msg","资源删除异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //获取角色选择资源树
    @RequestMapping(value = "/getRoleSelectTreeNode")
    public void getRoleSelectTreeNode(HttpServletResponse response,Integer roleId)
    {

        JSONObject map = new JSONObject();
        try{
            List<TreeNode> list = resourceService.getRoleSelectTreeNode(roleId);
            map.put("result",0);
            map.put("list",list);

        }catch(Exception e)
        {
            logger.error("getRoleSelectTreeNode error,roleId="+roleId,e);
            map.put("result",1);
            map.put("msg","处理异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
