package com.shop.book.manage.user.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "shop.manage.user")
public class ShopManageUserProperties {

    //服务url
    private String serverUrl = "";

    //需要登陆验证的path,依","号拼接
    private String needLoginAuthPath = "";

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getNeedLoginAuthPath() {
        return needLoginAuthPath;
    }

    public void setNeedLoginAuthPath(String needLoginAuthPath) {
        this.needLoginAuthPath = needLoginAuthPath;
    }
}