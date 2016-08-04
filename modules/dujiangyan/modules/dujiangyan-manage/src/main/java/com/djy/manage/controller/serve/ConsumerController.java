package com.djy.manage.controller.serve;

import com.alibaba.fastjson.JSONObject;
import com.djy.manage.controller.BaseController;
import com.djy.manage.model.vo.QueryVo;
import com.djy.manage.model.vo.ServiceVo;
import com.djy.manage.service.serve.ConsumerService;
import com.util.page.Page;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * 消费者维度服务controller类
 */
@Controller
@RequestMapping(value = "/djy/serve")
public class ConsumerController extends BaseController {

    private static final String PREFIX = "/dujiangyan/serve/";

    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value = "/consumer")
    public String consumer(String dimenType,String assignValue) {
        String rt=PREFIX+"consumer";
        if(!StringUtils.isBlank(dimenType) && !StringUtils.isBlank(assignValue))
        {
            rt=PREFIX+"consumer.html?dimenType="+dimenType+"&assignValue="+assignValue;
        }
        return  rt;
    }

    @RequestMapping(value = "/queryConsumerPage")
    public void queryConsumerPage(HttpServletResponse response,QueryVo queryVo) {

        logger.info("ConsumerController queryConsumerPage queryVo:"+queryVo);
        queryVo.genPage();

        Page<ServiceVo> page = consumerService.queryList(queryVo);
        int totalCount=page.getTotalCount();

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,page.getResult());
        map.put(DATAGRID_TOTAL,totalCount);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
