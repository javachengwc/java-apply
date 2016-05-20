package com.shop.controller.aftersale;

import com.alibaba.fastjson.JSONObject;
import com.shop.controller.BaseController;
import com.shop.model.pojo.OdAftersale;
import com.shop.model.vo.AftersaleQueryVo;
import com.shop.service.aftersale.AftersaleService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 售后controller类
 */
@Controller
@RequestMapping(value = "/shop/aftersale")
public class AftersaleController extends BaseController {

    private static final String PREFIX = "/shop/aftersale/";

    @Autowired
    private AftersaleService aftersaleService;

    //售后页面
    @RequestMapping(value = "/aftersale")
    public String aftersale()
    {
        return PREFIX + "aftersale";
    }

    //售后列表
    @RequestMapping(value = "/aftersaleList")
    public void aftersaleList(HttpServletResponse response,AftersaleQueryVo queryVo) {

        queryVo.genPage();

        List<OdAftersale> list = aftersaleService.queryPage(queryVo);
        int count = aftersaleService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

}
