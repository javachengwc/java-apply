package com.configcenter.service;

import com.configcenter.annotation.LogTag;
import com.configcenter.dao.IDao;
import com.configcenter.dao.biz.AppDao;
import com.configcenter.dao.biz.ConfigItemDao;
import com.configcenter.model.App;
import com.configcenter.service.core.BaseService;
import com.configcenter.vo.CommonQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用服务类
 */
@Service
@LogTag("add,update")
public class AppService extends BaseService<App> {

    @Autowired
    private AppDao appDao;

    @Autowired
    private ConfigItemDao configItemDao;

    public IDao getDao()
    {
        return appDao;
    }

    public void delAppAndRela(Integer id)
    {
        if(id==null)
        {
            return;
        }
        App app = new App();
        app.setId(id);
        app=getById(app);
        if(app==null)
        {
            return;
        }

        delete(app);

        configItemDao.deleteByApp(app);
        LogManager.log(SessionManager.getCurUser(), "删除应用" + app.getName());
    }

    public List<App> queryAll()
    {
        List<App> allList = new ArrayList<App>();
        int start =0;
        int pageSize=5000;
        int queryCount=pageSize;

        CommonQueryVo queryVo=new CommonQueryVo();
        queryVo.setStart(start);
        queryVo.setRows(pageSize);

        while(queryCount>=pageSize)
        {
            List<App> perList=queryList(queryVo);
            queryCount = (perList==null)?0:perList.size();
            if(queryCount>0)
            {
                start =start+queryCount;
                queryVo.setStart(start);

                allList.addAll(perList);
                perList.clear();
            }
        }
        return allList;
    }

}
