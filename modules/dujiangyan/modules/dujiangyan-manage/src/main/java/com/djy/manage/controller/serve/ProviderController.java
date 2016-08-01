package com.djy.manage.controller.serve;

import com.alibaba.fastjson.JSONObject;
import com.djy.manage.controller.BaseController;
import com.djy.manage.model.vo.QueryVo;
import com.util.web.HttpRenderUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 提供者维度服务controller类
 */
@Controller
@RequestMapping(value = "/djy/serve")
public class ProviderController extends BaseController {

    private static final String PREFIX = "/dujiangyan/serve/";

    @RequestMapping(value = "/provider")
    public String provider() {
        return  PREFIX+"provider";
    }

    @RequestMapping(value = "/queryProviderPage")
    public void queryProviderPage(HttpServletResponse response,QueryVo queryVo) {

        queryVo.genPage();

        List<Object> list = null;
        int count = 0;

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
