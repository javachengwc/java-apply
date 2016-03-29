package com.configcenter.vo;

import com.configcenter.model.ConfigItem;

/**
 * 配置项
 */
public class ConfigItemVo extends ConfigItem {

    //应用名称
    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
