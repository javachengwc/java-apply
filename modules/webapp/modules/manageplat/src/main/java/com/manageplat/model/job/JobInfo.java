package com.manageplat.model.job;

import org.apache.commons.lang3.builder.ToStringBuilder;
import java.io.Serializable;

public class JobInfo implements Serializable {

    private static final long serialVersionUID = -12322292929999l;
    
    private Integer id;

    private Integer parentId;

    private String  jobName;

    private Integer jobStatus;

    private String  expression;

    private String  creater;

    private Integer createTime;

    private String type;
    
    private String exeUrl;
    
    private String runStatus="ready";
    
    private String ip;
    
    //心跳时间
    private Integer runTime;
    
    private String runer;
    
    private String code;

    //联系人
    private String relator;
    
    //是否回调
    private Integer isCallBack=1;
    
    //预计执行时长
    private Integer planCostTime;
    
    //最近监控时间
    private Integer lastedMonitTime;
    
    //监控结果
    private Integer monitResult;

    private transient String parentJobName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExeUrl() {
        return exeUrl;
    }

    public void setExeUrl(String exeUrl) {
        this.exeUrl = exeUrl;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public String getRuner() {
        return runer;
    }

    public void setRuner(String runer) {
        this.runer = runer;
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

    public Integer getIsCallBack() {
        return isCallBack;
    }

    public void setIsCallBack(Integer isCallBack) {
        this.isCallBack = isCallBack;
    }

    public Integer getPlanCostTime() {
        return planCostTime;
    }

    public void setPlanCostTime(Integer planCostTime) {
        this.planCostTime = planCostTime;
    }

    public Integer getLastedMonitTime() {
        return lastedMonitTime;
    }

    public void setLastedMonitTime(Integer lastedMonitTime) {
        this.lastedMonitTime = lastedMonitTime;
    }

    public Integer getMonitResult() {
        return monitResult;
    }

    public void setMonitResult(Integer monitResult) {
        this.monitResult = monitResult;
    }

    public String getParentJobName() {
        return parentJobName;
    }

    public void setParentJobName(String parentJobName) {
        this.parentJobName = parentJobName;
    }

    public String toString() {
    	
        return ToStringBuilder.reflectionToString(this);
    }

}
