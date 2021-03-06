package com.mountain.manage.controller.serve;

import com.alibaba.fastjson.JSONObject;
import com.mountain.manage.controller.BaseController;
import com.mountain.manage.model.vo.DimenVo;
import com.mountain.manage.service.serve.MachineService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 机器维度服务controller类
 */
@Controller
@RequestMapping(value = "/mountain/serve")
public class MachineController extends BaseController {

    private static final String PREFIX = "/mountain/serve/";

    @Autowired
    private MachineService machineService;

    @RequestMapping(value = "/machine")
    public String machine() {
        return  PREFIX+"machine";
    }

    @RequestMapping(value = "/machineList")
    public void machineList(HttpServletRequest request,HttpServletResponse response)
    {
        List<DimenVo> list = machineService.queryList();
        int count = (list==null)?0:list.size();
        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
