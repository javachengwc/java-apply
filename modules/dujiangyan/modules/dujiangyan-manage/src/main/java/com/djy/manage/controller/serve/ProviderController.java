package com.djy.manage.controller.serve;

import com.alibaba.fastjson.JSONObject;
import com.djy.manage.controller.BaseController;
import com.djy.manage.model.vo.QueryVo;
import com.djy.manage.model.vo.ServiceVo;
import com.djy.manage.service.serve.ProviderService;
import com.util.page.Page;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 提供者维度服务controller类
 * provider方法返回页面带参数
 */
@Controller
@RequestMapping(value = "/djy/serve")
public class ProviderController extends BaseController {

    private static final String PREFIX = "/dujiangyan/serve/";

    @Autowired
    private ProviderService providerService;

    @RequestMapping(value = "/provider")
    public String provider(String dimenType,String assignValue) {

        String rt=PREFIX+"provider";
        if(!StringUtils.isBlank(dimenType) && !StringUtils.isBlank(assignValue))
        {
            //return new ModelAndView("provider.html?dimenType="+dimenType+"&assignValue="+assignValue);
            rt=PREFIX+"provider.html?dimenType="+dimenType+"&assignValue="+assignValue;
        }
        return  rt;
    }

    @RequestMapping(value = "/queryProviderPage")
    public void queryProviderPage(HttpServletResponse response,QueryVo queryVo) {

        logger.info("ProviderController queryProviderPage queryVo:"+queryVo);
        queryVo.genPage();

        Page<ServiceVo> page = providerService.queryList(queryVo);
        int totalCount=page.getTotalCount();

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,page.getResult());
        map.put(DATAGRID_TOTAL,totalCount);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
