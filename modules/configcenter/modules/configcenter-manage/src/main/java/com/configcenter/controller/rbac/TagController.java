package com.configcenter.controller.rbac;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.OperateRecord;
import com.configcenter.service.OperateRecordService;
import com.configcenter.service.rbac.TagService;
import com.configcenter.vo.CommonQueryVo;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限标签访问类
 */
@Controller
@RequestMapping(value = "/rbac")
public class TagController {

    private static final String PREFIX = "/rbac/";

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/tagView")
    public String tagView() {

        return PREFIX+"tagView";
    }

    @RequestMapping(value = "/queryTagPage")
    public void queryTagPage(HttpServletResponse response,CommonQueryVo queryVo)
    {
        if(queryVo==null)
        {
            queryVo =new CommonQueryVo();
        }
        queryVo.genPage();

        List<String> list = tagService.queryPage(queryVo);
        int queryCount = (list==null)?0:list.size();
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        if(queryCount>0)
        {
            for(String tag:list)
            {
                Map<String,String> map = new HashMap<String,String>(1);
                map.put("tag",tag);
                mapList.add(map);
            }
            list.clear();
        }

        JSONObject map = new JSONObject();
        map.put(Constant.DATAGRID_ROWS,mapList);
        map.put(Constant.DATAGRID_TOTAL,(queryCount+1));

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

}
