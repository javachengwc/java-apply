package com.manageplat.controller.tongyong;

import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.vo.tongyong.TyEntityCdnVo;
import com.manageplat.service.tongyong.EntityCdnService;
import com.util.web.HttpRenderUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通用列表实体查询条件controller类
 */
@Controller
@RequestMapping("tongyong")
public class EntityCdnController {

    private static Logger logger = LoggerFactory.getLogger(EntityCdnController.class);

    @Autowired
    private EntityCdnService entityCdnService;

    /**分页查询**/
    @RequestMapping(value = "/queryCdnPage")
    public void queryCdnPage(HttpServletResponse response,TyEntityCdnVo entityCdn,Integer pageNo,Integer pageSize)
    {

        if(pageNo==null)
        {
            pageNo=1;
        }
        if(pageSize==null)
        {
            pageSize=20;
        }
        int start = (pageNo-1)*pageSize;
        if(start<0)
        {
            start=0;
        }
        int count = entityCdnService.count(entityCdn);

        List<TyEntityCdn> list = entityCdnService.queryPage(entityCdn,start,pageSize);

        JSONObject map = new JSONObject();
        map.put("error", 0);
        map.put("count", count);
        map.put("list", list);
        map.put("now", System.currentTimeMillis());
        HttpRenderUtil.renderJSON(map.toString(), response);
    }

}
