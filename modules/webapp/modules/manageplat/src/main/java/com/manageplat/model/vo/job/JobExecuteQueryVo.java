package com.manageplat.model.vo.job;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * job执行记录查询条件类
 */
public class JobExecuteQueryVo implements Serializable{

    private Integer jobId;

    private String jobName;

    private Integer startTimeBegin;

    private Integer startTimeEnd;

    private Integer page;

    private Integer start;

    private Integer rows;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getStartTimeBegin() {
        return startTimeBegin;
    }

    public void setStartTimeBegin(Integer startTimeBegin) {
        this.startTimeBegin = startTimeBegin;
    }

    public Integer getStartTimeEnd() {
        return startTimeEnd;
    }

    public void setStartTimeEnd(Integer startTimeEnd) {
        this.startTimeEnd = startTimeEnd;
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

    public void genPage() {
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        if (start == null) {
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
