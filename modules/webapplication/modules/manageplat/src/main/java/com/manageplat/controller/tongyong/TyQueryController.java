package com.manageplat.controller.tongyong;

import com.manageplat.exception.TyException;
import com.manageplat.model.pojo.TyEntity;
import com.manageplat.model.vo.tongyong.TyResultVo;
import com.manageplat.service.tongyong.EntityService;
import com.manageplat.service.tongyong.TyQueryService;
import com.util.base.NumberUtil;
import com.util.web.HttpRenderUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用列表
 */
@Controller
@RequestMapping("tongyong")
public class TyQueryController {

    private static Logger logger = LoggerFactory.getLogger(TyQueryController.class);

    @Autowired
    private EntityService entityService;

    @Autowired
    private TyQueryService tyQueryService;

    /**
     * 通用查询
     * @param request
     * @param response
     * @param tyQueryId  实例id
     */
    @RequestMapping(value = "/tyQuery")
    public void tyQuery(HttpServletRequest request,HttpServletResponse response,Integer tyQueryId)
    {
        logger.info("ListQueryController tyQuery tyQueryId= "+tyQueryId);

        JSONObject map = new JSONObject();
        if(tyQueryId==null)
        {
            map.put("error", 1);
            map.put("msg", "查询实例id为空");
            HttpRenderUtil.renderJSON(map.toString(), response);
            return;
        }

        TyEntity entity = entityService.getById(tyQueryId);
        if(entity==null)
        {
            map.put("error", 1);
            map.put("msg", "无此查询实例");
            HttpRenderUtil.renderJSON(map.toString(), response);
        }

        Map<String,Object> paramMap =getRequestMap(request);
        //处理参数
        Object pageSizeObj=paramMap.get("pageSize");

        Integer pageSize= entity.getPageSize();
        if(pageSizeObj!=null && NumberUtil.isNumeric(pageSizeObj.toString()))
        {
            pageSize=Integer.parseInt(pageSizeObj.toString());
        }

        pageSize = (pageSize==null?20:pageSize);

        paramMap.put("pageSize",pageSize);

        Object pgeNoObj= paramMap.get("pageNo");
        int pageNo=1;
        if(pgeNoObj!=null && NumberUtil.isNumeric(pgeNoObj.toString()))
        {
            pageNo = Integer.parseInt(pgeNoObj.toString());
        }
        int start = (pageNo-1)*pageSize;
        start = (start<0?0:start);

        paramMap.put("start",start);

        TyResultVo result =null;

        try{
            result=tyQueryService.queryPage(tyQueryId,paramMap);
        }catch(TyException tye)
        {
            map.put("error", 1);
            map.put("msg", tye.getMessage());
            HttpRenderUtil.renderJSON(map.toString(), response);
            return;
        }catch(Exception e)
        {
            map.put("error", 1);
            map.put("msg", "处理异常");
            logger.error("TyQueryController tyQuery error,",e);
            HttpRenderUtil.renderJSON(map.toString(), response);
            return;
        }

        map.put("error", 0);
        map.put("result", result);
        map.put("now", System.currentTimeMillis());

        HttpRenderUtil.renderJSON(map.toString(), response);
    }

    /**
     * 获取请求参数
     */
    public Map<String,Object> getRequestMap(HttpServletRequest request)
    {
        Map<String,Object> map = new HashMap<String,Object>();
        Map paramMap =request.getParameterMap();//string-->string []
        if(paramMap!=null && paramMap.size()>0)
        {
            for(Object key:paramMap.keySet())
            {
                String keyStr =key.toString();
                String [] values= (String [])paramMap.get(keyStr);
                String value=null;
                if(values!=null && values.length>0)
                {
                   if(values.length==1)
                   {
                       value =values[0];
                   }else
                   {
                       StringBuffer buf = new StringBuffer();
                       for(String per:values)
                       {
                           buf.append(per).append(",");
                       }
                       value = buf.toString();
                       if(value.endsWith(","))
                       {
                           value = value.substring(0,value.lastIndexOf(","));
                       }
                   }
                }
                map.put(keyStr,value);

            }
        }
        logger.info("TyQueryController getRequestMap: ");
        for(String key:map.keySet())
        {
            logger.info("    "+key+"->"+map.get(key));
        }
        return map;
    }
}
