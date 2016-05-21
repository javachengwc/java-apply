package com.shop.controller.aftersale;

import com.alibaba.fastjson.JSONObject;
import com.shop.controller.BaseController;
import com.shop.model.pojo.OdComplain;
import com.shop.model.vo.AftersaleQueryVo;
import com.shop.service.aftersale.ComplainService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 投诉controller类
 */
@Controller
@RequestMapping(value = "/shop/aftersale")
public class ComplainController extends BaseController {

    private static final String PREFIX = "/shop/aftersale/";

    @Autowired
    private ComplainService complainService;

    //投诉页面
    @RequestMapping(value = "/complain")
    public String complain()
    {
        return PREFIX + "complain";
    }

    //售后列表
    @RequestMapping(value = "/complainList")
    public void complainList(HttpServletResponse response,AftersaleQueryVo queryVo) {

        queryVo.genPage();

        List<OdComplain> list = complainService.queryPage(queryVo);
        int count = complainService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

}