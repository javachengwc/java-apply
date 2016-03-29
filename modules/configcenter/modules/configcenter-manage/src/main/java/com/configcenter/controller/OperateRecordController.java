package com.configcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.OperateRecord;
import com.configcenter.service.OperateRecordService;
import com.configcenter.vo.CommonQueryVo;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作记录访问类
 */
@Controller
public class OperateRecordController {

    @Autowired
    private OperateRecordService operateRecordService;

    @RequestMapping(value = "/operateView")
    public String operateView() {

        return "operateView";
    }

    @RequestMapping(value = "/queryOperatePage")
    public void queryOperatePage(HttpServletResponse response,CommonQueryVo queryVo)
    {
        if(queryVo==null)
        {
            queryVo =new CommonQueryVo();
        }
        queryVo.genPage();

        List<OperateRecord> list = operateRecordService.queryList(queryVo);

        int count =operateRecordService.count(queryVo);

        JSONObject map = new JSONObject();

        map.put(Constant.DATAGRID_ROWS,list);
        map.put(Constant.DATAGRID_TOTAL,count);
        map.put("page",queryVo.getPage());

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }



}
