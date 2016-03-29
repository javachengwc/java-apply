package com.rule.data.service.userlist;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UserInfo {

    private String userName;

    private String realName;

    private String jobType;

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
