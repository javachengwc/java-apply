package com.rule.data.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SerDb {

    private String dbID;

    private String driver;

    private String url;

    private String user;

    private String password;


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SerDb)) return false;

        SerDb dbPo = (SerDb) o;

        if (dbID != null ? !dbID.equals(dbPo.dbID) : dbPo.dbID != null)
            return false;
        if (driver != null ? !driver.equals(dbPo.driver) : dbPo.driver != null)
            return false;
        if (password != null ? !password.equals(dbPo.password) : dbPo.password != null)
            return false;
        if (url != null ? !url.equals(dbPo.url) : dbPo.url != null)
            return false;
        if (user != null ? !user.equals(dbPo.user) : dbPo.user != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dbID != null ? dbID.hashCode() : 0;
        return result;
    }
}
