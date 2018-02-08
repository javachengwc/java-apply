package com.shop.order.api.model.base;

public class ReqHeader {

    private String app;

    private String token;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
