package com.mybatis.springboot.autoconfigure;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@ConfigurationProperties(prefix="pagehelper")
public class PageHelperProperties
{
    public static final String PAGEHELPER_PREFIX = "pagehelper";
    private Properties properties = new Properties();

    public Properties getProperties() {
        return this.properties;
    }

    public String getOffsetAsPageNum() {
        return this.properties.getProperty("offsetAsPageNum");
    }

    public void setOffsetAsPageNum(String offsetAsPageNum) {
        this.properties.setProperty("offsetAsPageNum", offsetAsPageNum);
    }

    public String getRowBoundsWithCount() {
        return this.properties.getProperty("rowBoundsWithCount");
    }

    public void setRowBoundsWithCount(String rowBoundsWithCount) {
        this.properties.setProperty("rowBoundsWithCount", rowBoundsWithCount);
    }

    public String getPageSizeZero() {
        return this.properties.getProperty("pageSizeZero");
    }

    public void setPageSizeZero(String pageSizeZero) {
        this.properties.setProperty("pageSizeZero", pageSizeZero);
    }

    public String getReasonable() {
        return this.properties.getProperty("reasonable");
    }

    public void setReasonable(String reasonable) {
        this.properties.setProperty("reasonable", reasonable);
    }

    public String getSupportMethodsArguments() {
        return this.properties.getProperty("supportMethodsArguments");
    }

    public void setSupportMethodsArguments(String supportMethodsArguments) {
        this.properties.setProperty("supportMethodsArguments", supportMethodsArguments);
    }

    public String getDialect() {
        return this.properties.getProperty("dialect");
    }

    public void setDialect(String dialect) {
        this.properties.setProperty("dialect", dialect);
    }

    public String getHelperDialect() {
        return this.properties.getProperty("helperDialect");
    }

    public void setHelperDialect(String helperDialect) {
        this.properties.setProperty("helperDialect", helperDialect);
    }

    public String getAutoRuntimeDialect() {
        return this.properties.getProperty("autoRuntimeDialect");
    }

    public void setAutoRuntimeDialect(String autoRuntimeDialect) {
        this.properties.setProperty("autoRuntimeDialect", autoRuntimeDialect);
    }

    public String getAutoDialect() {
        return this.properties.getProperty("autoDialect");
    }

    public void setAutoDialect(String autoDialect) {
        this.properties.setProperty("autoDialect", autoDialect);
    }

    public String getCloseConn() {
        return this.properties.getProperty("closeConn");
    }

    public void setCloseConn(String closeConn) {
        this.properties.setProperty("closeConn", closeConn);
    }

    public String getParams() {
        return this.properties.getProperty("params");
    }

    public void setParams(String params) {
        this.properties.setProperty("params", params);
    }
}
