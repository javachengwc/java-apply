package com.configcenter.service;

import com.configcenter.annotation.LogTag;
import com.configcenter.dao.IDao;
import com.configcenter.dao.biz.ConfigItemDao;
import com.configcenter.model.App;
import com.configcenter.model.ConfigItem;
import com.configcenter.service.core.BaseService;
import com.configcenter.vo.CommonQueryVo;
import com.configcenter.vo.ConfigItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配置项服务类
 */
@Service
@LogTag("add,update,delete")
public class ConfigItemService extends BaseService<ConfigItem> {

    @Autowired
    private ConfigItemDao configItemDao;

    public IDao getDao()
    {
        return configItemDao;
    }

    public List<ConfigItemVo> queryListExt(CommonQueryVo queryVo)
    {
        return configItemDao.queryListExt(queryVo);
    }

    public List<ConfigItem> queryByApp(App app)
    {
        return configItemDao.queryByApp(app);
    }

    public ConfigItemVo queryConfigItemFull(ConfigItem configItem)
    {
        return configItemDao.queryConfigItemFull(configItem);
    }
}
