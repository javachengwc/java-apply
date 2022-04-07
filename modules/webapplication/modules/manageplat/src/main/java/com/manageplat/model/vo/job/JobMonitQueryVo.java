package com.manageplat.model.vo.job;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * job监控查询条件类
 */
public class JobMonitQueryVo implements Serializable {

    private Integer jobId;

    private Integer recordTimeBegin;

    private Integer recordTimeEnd;

    private Integer page;

    private Integer start;

    private Integer rows;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getRecordTimeBegin() {
        return recordTimeBegin;
    }

    public void setRecordTimeBegin(Integer recordTimeBegin) {
        this.recordTimeBegin = recordTimeBegin;
    }

    public Integer getRecordTimeEnd() {
        return recordTimeEnd;
    }

    public void setRecordTimeEnd(Integer recordTimeEnd) {
        this.recordTimeEnd = recordTimeEnd;
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