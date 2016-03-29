package com.configcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.ConfigItem;
import com.configcenter.service.ConfigItemService;
import com.configcenter.service.SynService;
import com.configcenter.vo.CommonQueryVo;
import com.configcenter.vo.ConfigItemVo;
import com.util.web.HttpRenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 配置项访问类
 */
@Controller
public class ConfigItemController {

    private static Logger logger = LoggerFactory.getLogger(ConfigItemController.class);

    @Autowired
    private ConfigItemService configItemService;

    @Autowired
    private SynService synService;

    @RequestMapping(value = "/configItemView")
    public String configItemView() {

        return  "configItemView";
    }

    @RequestMapping(value = "/queryConfigItemPage")
    public void queryAppPage(HttpServletResponse response,CommonQueryVo queryVo)
    {
        if(queryVo==null)
        {
            queryVo =new CommonQueryVo();
        }
        queryVo.genPage();

        List<ConfigItemVo> list = configItemService.queryListExt(queryVo);

        int count =configItemService.count(queryVo);

        JSONObject map = new JSONObject();

        map.put(Constant.DATAGRID_ROWS,list);
        map.put(Constant.DATAGRID_TOTAL,count);
        map.put("page",queryVo.getPage());

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //增改配置项
    @RequestMapping(value = "/addOrUptConfigItem")
    public void addOrUptConfigItem(HttpServletResponse response,ConfigItem configItem)
    {

        JSONObject map = new JSONObject();
        boolean isAdd=(configItem.getId()==null)?true:false;

        if(configItem.getAppId()==null || configItem.getKey()==null)
        {
            map.put("result",1);
            map.put("msg","参数错误");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }
        configItem.setUpdateAt(new Date());

        try{
            if(isAdd)
            {
                configItemService.add(configItem);
            }else
            {
                configItemService.update(configItem);
            }
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("addOrUptConfigItem error,configItem="+configItem,e);
            map.put("result",1);
            map.put("msg","配置项增改异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //删除配置项
    @RequestMapping(value = "/delConfigItem")
    public void delConfigItem(HttpServletResponse response,ConfigItem configItem)
    {
        JSONObject map = new JSONObject();
        try{
            if(configItem.getId()!=null) {
                configItemService.delete(configItem);
            }
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("delConfigItem error,id="+configItem.getId(),e);
            map.put("result",1);
            map.put("msg","配置项删除异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //查看zookeeper上对应的配置项
    @RequestMapping(value = "/queryConfigItemOnZooKeeper")
    public void queryConfigItemOnZooKeeper(HttpServletResponse response,Integer id)
    {
        JSONObject map = new JSONObject();
        if(id==null)
        {
            map.put("result",1);
            map.put("msg","参数异常");
            HttpRenderUtil.renderJSON(map.toJSONString(), response);
            return;
        }
        try{
            ConfigItem configItem = new ConfigItem();
            configItem.setId(id);

            ConfigItemVo configItemVo = configItemService.queryConfigItemFull(configItem);

            String value =synService.queryZkConfigItem(configItemVo);
            map.put("result",0);
            map.put("msg",value);

        }catch(RuntimeException re)
        {
            logger.error("queryConfigItemOnZooKeeper runtime exception ,id="+id,re);
            map.put("result",1);
            map.put("msg",re.getMessage());
        }
        catch(Exception e)
        {
            logger.error("queryConfigItemOnZooKeeper error,id="+id,e);
            map.put("result",1);
            map.put("msg","操作异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
