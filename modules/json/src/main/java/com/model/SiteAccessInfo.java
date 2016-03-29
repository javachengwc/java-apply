package com.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 站点访问信息
 */
public class SiteAccessInfo {

    //站点网址
    private String site;

    //站点名称
    private String name;

    private Long uv;

    private Long out;

    //pc曝光数
    private Long pcExp;

    //无线曝光数
    private Long wxExp;

    private Date addTime;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUv() {
        return uv;
    }

    public void setUv(Long uv) {
        this.uv = uv;
    }

    public Long getOut() {
        return out;
    }

    public void setOut(Long out) {
        this.out = out;
    }

    public Long getPcExp() {
        return pcExp;
    }

    public void setPcExp(Long pcExp) {
        this.pcExp = pcExp;
    }

    public Long getWxExp() {
        return wxExp;
    }

    public void setWxExp(Long wxExp) {
        this.wxExp = wxExp;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteAccessInfo that = (SiteAccessInfo) o;

        if (site != null ? !site.equals(that.site) : that.site != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return site != null ? site.hashCode() : 0;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
