package com.shopstat.controller.aftersale;

import com.alibaba.fastjson.JSONObject;
import com.shopstat.controller.BaseController;
import com.shopstat.model.pojo.StatSafeguard;
import com.shopstat.model.vo.StatQueryVo;
import com.shopstat.service.aftersale.SafeguardStatService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 维权统计controller类
 */
@Controller
@RequestMapping(value = "/shopstat/aftersale")
public class SafeguardStatController extends BaseController {

    private static final String PREFIX = "/shopstat/aftersale/";

    @Autowired
    private SafeguardStatService safeguardStatService;

    //维权统计页面
    @RequestMapping(value = "/safeguardStat")
    public String safeguardStat()
    {
        return PREFIX + "safeguardStat";
    }

    //维权统计列表
    @RequestMapping(value = "/safeguardStatList")
    public void safeguardStatList(HttpServletResponse response,StatQueryVo queryVo) {

        queryVo.genDef();
        queryVo.genPage();

        List<StatSafeguard> list = safeguardStatService.queryPage(queryVo);
        int count = safeguardStatService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

}