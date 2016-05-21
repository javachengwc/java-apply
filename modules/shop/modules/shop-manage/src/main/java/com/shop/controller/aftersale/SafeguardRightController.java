package com.shop.controller.aftersale;

import com.alibaba.fastjson.JSONObject;
import com.shop.controller.BaseController;
import com.shop.model.pojo.OdSafeguardRight;
import com.shop.model.vo.AftersaleQueryVo;
import com.shop.service.aftersale.SafeguardRightService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 维权controller类
 */
@Controller
@RequestMapping(value = "/shop/aftersale")
public class SafeguardRightController  extends BaseController {

    private static final String PREFIX = "/shop/aftersale/";

    @Autowired
    private SafeguardRightService safeguardRightService;

    //维权页面
    @RequestMapping(value = "/safeguard")
    public String safeguard()
    {
        return PREFIX + "safeguard";
    }

    //维权列表
    @RequestMapping(value = "/safeguardList")
    public void safeguardList(HttpServletResponse response,AftersaleQueryVo queryVo) {

        queryVo.genPage();

        List<OdSafeguardRight> list = safeguardRightService.queryPage(queryVo);
        int count = safeguardRightService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

}