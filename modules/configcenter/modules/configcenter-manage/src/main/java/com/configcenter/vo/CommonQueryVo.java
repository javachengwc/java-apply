package com.configcenter.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 通用查询条件载体类
 */
public class CommonQueryVo {

    private String statDateBegin; //yyyy-MM-dd

    private String statDateEnd;   //yyyy-MM-dd

    private String statDate; //yyyy-MM-dd

    private String name;

    private String nameCh;

    private String key;

    private String value;

    private Integer parentId;

    private Integer rows;

    private Integer page;

    private Integer start;

    public String getStatDateBegin() {
        return statDateBegin;
    }

    public void setStatDateBegin(String statDateBegin) {
        this.statDateBegin = statDateBegin;
    }

    public String getStatDateEnd() {
        return statDateEnd;
    }

    public void setStatDateEnd(String statDateEnd) {
        this.statDateEnd = statDateEnd;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void genPage()
    {
        if(page==null)
        {
            page=1;
        }
        if(rows==null)
        {
            rows=20;
        }
        if(start==null) {
            start = (page - 1) * rows;
        }
        if(start<0)
        {
            start=0;
        }
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}

