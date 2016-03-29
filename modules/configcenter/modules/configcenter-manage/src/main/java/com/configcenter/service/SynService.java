package com.configcenter.service;

import com.configcenter.model.App;
import com.configcenter.model.ConfigItem;
import com.configcenter.vo.ConfigItemVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据同步到zk服务类
 */
@Service
public class SynService {

    private static Logger logger = LoggerFactory.getLogger(SynService.class);

    @Autowired
    private AppService appService;

    @Autowired
    private ConfigItemService configItemService;

    //同步app的配置
    public boolean synApp(App app)
    {
        if(app==null || app.getName()==null)
        {
            return true;
        }
        List<ConfigItem> list = configItemService.queryByApp(app);
        boolean rt= synAppConfig(app,list);
        logger.info("SynService synApp "+rt+",app= "+app.getName());
        return rt;
    }

    //同步所有应用的配置
    public boolean synAll()
    {
        List<App> appList = appService.queryAll();
        if(appList==null || appList.size()<=0)
        {
            return true;
        }
        boolean rt=true;
        for(App app:appList)
        {
            boolean syn=synApp(app);
            if(!syn)
            {
                rt=false;
            }
        }

        String appRootPath = ZookeeperManager.APP_ROOT;
        List<String> appZkList =ZookeeperManager.getInstance().getChildrenPath(appRootPath);
        List<String> delApps= findDelApp(appZkList,appList);
        if(delApps!=null)
        {
            for(String delApp:delApps) {
                String appPath = ZookeeperManager.APP_ROOT.concat("/").concat(delApp);
                boolean del= ZookeeperManager.getInstance().deletePath(appPath);
                if(!del)
                {
                    rt=false;
                }
            }
            delApps.clear();
        }
        if(appZkList!=null)
        {
            appZkList.clear();
        }

        logger.info("SynService synAll "+rt);
        return rt;
    }

    public  List<String>  findDelApp( List<String> appZkList, List<App> appList)
    {
        List<String> rtList = new ArrayList<String>();

        int hasCount= (appZkList==null)?0:appZkList.size();

        if(hasCount>0)
        {
            for(String appZk:appZkList)
            {
                boolean has=false;
                if(appList!=null) {
                    for (App app : appList) {
                        if (app.getName().equals(appZk)) {
                            has = true;
                            break;
                        }
                    }
                }
                if(!has)
                {
                    rtList.add(appZk);
                }
            }
        }
        return rtList;
    }

    //同步具体配置
    public boolean synAppConfig(App app,List<ConfigItem> list)
    {
        String appName = app.getName();
        String appPath = ZookeeperManager.APP_ROOT+"/"+appName;
        List<String> zkPathList =ZookeeperManager.getInstance().getChildrenPath(appPath);

        List<ConfigItem> addConfigList =findAddOrUpt(list, zkPathList, 0);
        List<ConfigItem> uptConfigList =findAddOrUpt(list,zkPathList,1);
        List<String> subPaths =findDel(list,zkPathList);

        boolean addSyn =appOrUptConfigOnZk(app, addConfigList);
        boolean uptSyn =appOrUptConfigOnZk(app, uptConfigList);
        boolean delSyn=delConfigOnZk(app,subPaths);

        boolean rt= (addSyn && uptSyn && delSyn);

        return rt;

    }

    public boolean appOrUptConfigOnZk(App app,List<ConfigItem> list)
    {
        String appName = app.getName();
        String appPath = ZookeeperManager.APP_ROOT+"/"+appName;

        boolean rt=true;
        if(list!=null && list.size()>0) {
            for(ConfigItem config:list) {
                String keyPath = appPath + "/" + config.getKey();
                String value = config.getValue();

                boolean syn = ZookeeperManager.getInstance().writeData(keyPath, value);
                if(!syn)
                {
                    rt=false;
                }
            }
        }
        return rt;
    }

    public boolean delConfigOnZk(App app,List<String> subPaths)
    {
        String appName = app.getName();
        String appPath = ZookeeperManager.APP_ROOT+"/"+appName;

        boolean rt=true;
        if(subPaths!=null && subPaths.size()>0) {
            for(String subPath:subPaths) {
                String keyPath = appPath + "/" + subPath;

                boolean syn = ZookeeperManager.getInstance().deletePath(keyPath);
                if(!syn)
                {
                    rt=false;
                }
            }
        }
        return rt;
    }

    //flag--0,找增加的. 1,找相同的
    public List<ConfigItem>  findAddOrUpt(List<ConfigItem> list, List<String> paths,int flag)
    {
        List<ConfigItem> rtList = new ArrayList<ConfigItem>();

        int hasCount= (list==null)?0:list.size();

        if(hasCount>0)
        {
            for(ConfigItem rr:list)
            {
                boolean has=false;
                if(paths!=null) {
                    for (String path : paths) {
                        if (rr.getKey().equals(path)) {
                            has = true;
                            if (flag == 1) {
                                rtList.add(rr);
                            }
                            break;
                        }
                    }
                }
                if( flag==0  && !has)
                {
                    rtList.add(rr);
                }
            }
        }
        return rtList;
    }

    public  List<String>  findDel( List<ConfigItem> list, List<String> paths)
    {
        List<String> rtList = new ArrayList<String>();

        int hasCount= (paths==null)?0:paths.size();

        if(hasCount>0)
        {
            for(String path:paths)
            {
                boolean has=false;
                if(list!=null) {
                    for (ConfigItem config : list) {
                        if (config.getKey().equals(path)) {
                            has = true;
                            break;
                        }
                    }
                }
                if(!has)
                {
                    rtList.add(path);
                }
            }
        }
        return rtList;
    }

    //获取zookeeper中配置项信息
    public String queryZkConfigItem(ConfigItemVo configItemVo) throws Exception
    {
        if(configItemVo==null || StringUtils.isBlank(configItemVo.getAppName()) || StringUtils.isBlank(configItemVo.getKey()))
        {
            throw new RuntimeException("配置项信息不全");
        }
        String path = ZookeeperManager.APP_ROOT+"/"+configItemVo.getAppName()+"/"+configItemVo.getKey();

        return ZookeeperManager.getInstance().readData(path);
    }
}
