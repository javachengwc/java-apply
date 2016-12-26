package com.db.model.vo;

import com.db.util.DateTimeUtil;
import com.util.date.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * 数据库查询条件
 */
public class DbQueryVo  implements Serializable {

    private String host;

    private Integer name;

    private String account;

    private Integer page;

    private Integer start;

    private Integer rows;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public void genPage()
    {
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        if(start==null) {
            start = (page - 1) * rows;
        }
        if (start < 0) {
            start = 0;
        }
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}