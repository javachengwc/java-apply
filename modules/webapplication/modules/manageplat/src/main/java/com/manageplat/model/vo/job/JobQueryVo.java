package com.manageplat.model.vo.job;

import com.util.date.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * job查询条件类
 */
public class JobQueryVo implements Serializable {

    private Integer id;

    private String jobName;

    private Integer jobStatus;

    private String creater;

    private String type;

    private String runStatus;

    private String code;

    private String relator;

    private String createTimeBegin;

    private String createTimeEnd;

    private Integer page;

    private Integer start;

    private Integer rows;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRelator() {
        return relator;
    }

    public void setRelator(String relator) {
        this.relator = relator;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
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

    public void genDate()
    {
        if(!StringUtils.isBlank(createTimeBegin))
        {
            try {
                Date begin = DateUtil.getDate(createTimeBegin,DateUtil.FMT_YMD);
                createTimeBegin = ""+(begin.getTime()/1000);
            }catch(Exception e)
            {

            }
        }
        if(!StringUtils.isBlank(createTimeEnd))
        {
            try {
                Date end = DateUtil.getDate(createTimeEnd,DateUtil.FMT_YMD);
                createTimeEnd = ""+(end.getTime()/1000);
            }catch(Exception e)
            {

            }
        }
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

}
