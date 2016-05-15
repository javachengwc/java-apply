package com.shopstat.controller;

import com.alibaba.fastjson.JSONObject;
import com.shopstat.model.vo.TreeNode;
import com.shopstat.service.rbac.ResourceService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 入口访问类
 */
@Controller
@RequestMapping(value = "/shopstat")
public class IndexController {

    private static final String PREFIX = "/shopstat/";

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/index")
    public String indexView() {
        return  PREFIX+"index";
    }

    @RequestMapping(value = "/menuList")
    public void menuList(HttpServletRequest request,HttpServletResponse response)
    {
        JSONObject map = new JSONObject();

        List<TreeNode> list= resourceService.queryMenuList();

        map.put("list",list);
        map.put("result",0);

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
