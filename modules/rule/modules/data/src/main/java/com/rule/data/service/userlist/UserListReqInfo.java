package com.rule.data.service.userlist;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 查询用户列表，基于分页
 */
public class UserListReqInfo {

    private String userName = "";

    private Integer page = 1;

    private Integer size = 20;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page > 1) {
            this.page = page;
        }
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size > 0) {
            this.size = size;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
