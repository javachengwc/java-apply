package com.configcenter.dao.biz;

import com.configcenter.dao.IDao;
import com.configcenter.model.App;
import com.configcenter.model.ConfigItem;
import com.configcenter.vo.CommonQueryVo;
import com.configcenter.vo.ConfigItemVo;

import java.util.List;

/**
 * 配置项访问类
 */
public interface ConfigItemDao extends IDao<ConfigItem> {

    public List<ConfigItemVo> queryListExt(CommonQueryVo queryVo);

    public void deleteByApp(App app);

    public List<ConfigItem> queryByApp(App app);

    public ConfigItemVo queryConfigItemFull(ConfigItem configItem);

}