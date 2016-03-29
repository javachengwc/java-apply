package com.configcenter.template;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class MysqlDb implements Serializable {

    //数据库名
    private String db;

    //用户
    private String user;

    //密码
    private String pwd;

    //mysql url
    private String url;

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
