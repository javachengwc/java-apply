package com.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.model.pojo.TbDb;
import com.db.model.vo.DbQueryVo;
import com.db.service.DbService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * db controller类
 */
@Controller
@RequestMapping(value = "/db")
public class DbController extends BaseController {

    private static final String PREFIX = "/db/";

    @Autowired
    private DbService dbService;

    //db页面
    @RequestMapping(value = "/dbView")
    public String dbView()
    {
        return PREFIX + "dbview";
    }

    //db列表
    @RequestMapping(value = "/dbList")
    public void dbList(HttpServletResponse response,DbQueryVo queryVo) {
        queryVo.genPage();
        List<TbDb> list =dbService.queryPage(queryVo);
        int count = dbService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}