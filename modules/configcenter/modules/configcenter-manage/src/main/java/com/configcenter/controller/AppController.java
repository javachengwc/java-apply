package com.configcenter.controller;

import com.alibaba.fastjson.JSONObject;
import com.configcenter.constant.Constant;
import com.configcenter.model.App;
import com.configcenter.service.AppService;
import com.configcenter.service.LogManager;
import com.configcenter.service.SessionManager;
import com.configcenter.service.SynService;
import com.configcenter.vo.CommonQueryVo;
import com.util.web.HttpRenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 应用访问类
 */
@Controller
public class AppController {

    private static Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppService appService;

    @Autowired
    private SynService synService;

    @RequestMapping(value = "/appView")
    public String appView() {

        System.out.println("appView invoke");
        return  "appView";
    }

    @RequestMapping(value = "/queryAppPage")
    public void queryAppPage(HttpServletResponse response,CommonQueryVo queryVo)
    {
        if(queryVo==null)
        {
            queryVo =new CommonQueryVo();
        }
        queryVo.genPage();

        List<App> list = appService.queryList(queryVo);

        int count =appService.count(queryVo);

        JSONObject map = new JSONObject();

        map.put(Constant.DATAGRID_ROWS,list);
        map.put(Constant.DATAGRID_TOTAL,count);
        map.put("page",queryVo.getPage());

        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //增改应用
    @RequestMapping(value = "/addOrUptApp")
    public void addOrUptApp(HttpServletResponse response,App app)
    {

        JSONObject map = new JSONObject();
        boolean isAdd=(app.getId()==null)?true:false;

        try{
            if(isAdd)
            {
                appService.add(app);
                System.out.println("-----new app id="+app.getId());
            }else
            {
                appService.update(app);
            }
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("addOrUptApp error,app="+app,e);
            map.put("result",1);
            map.put("msg","应用增改异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //删除应用
    @RequestMapping(value = "/delApp")
    public void delApp(HttpServletResponse response,Integer id)
    {
        JSONObject map = new JSONObject();
        try{
            appService.delAppAndRela(id);
            map.put("result",0);
        }catch(Exception e)
        {
            logger.error("delApp error,id="+id,e);
            map.put("result",1);
            map.put("msg","应用删除异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //应用的配置项面板
    @RequestMapping(value = "/appConfigView")
    public ModelAndView appConfigView(HttpServletResponse response,Integer appId,String appName)
    {
        return new ModelAndView("appConfigView.html?appId="+appId+"&appName="+appName);
    }

    //全量同步引用配置到zookeeper
    @RequestMapping(value = "/synAll")
    public void synAll(HttpServletResponse response)
    {
        JSONObject map = new JSONObject();
        try{
            boolean syn =synService.synAll();
            if(syn)
            {
                LogManager.log(SessionManager.getCurUser(), "同步所有应用配置到zookeeper");
            }
            map.put("result",(syn?0:1));
            if(!syn)
            {
                map.put("msg","全量同步失败");
            }
        }catch(Exception e)
        {
            logger.error("synAll error,",e);
            map.put("result",1);
            map.put("msg","全量同步异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }

    //同步app配置到zookeeper
    @RequestMapping(value = "/synApp")
    public void synApp(HttpServletResponse response,Integer id)
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
            App app = new App();
            app.setId(id);
            app =appService.getById(app);

            boolean syn =synService.synApp(app);
            if(syn)
            {
                LogManager.log(SessionManager.getCurUser(), "同步应用"+app.getName()+"配置到zookeeper");
            }

            map.put("result",(syn?0:1));
            if(!syn)
            {
                map.put("msg","同步失败");
            }
        }catch(Exception e)
        {
            logger.error("delApp error,id="+id,e);
            map.put("result",1);
            map.put("msg","同步异常");
        }
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
