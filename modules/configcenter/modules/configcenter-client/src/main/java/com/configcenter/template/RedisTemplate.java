package com.configcenter.template;

/**
 * redis模板
 */
public class RedisTemplate extends BaseTemplate {

    //用户
    private String user;

    //密码
    private String pwd;

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
}
