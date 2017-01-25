package com.manageplat.model.eximport;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class TyExportRecord {

    private Integer id;

    private String exportNo;

    private String operator;

    private Date operateTime;

    private Integer state;

    private Date finishTime;

    private String tableName;

    private String exportSql;

    private String exportFormat;

    private Integer needTitle;

    private String fileName;

    private String filePath;

    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExportNo() {
        return exportNo;
    }

    public void setExportNo(String exportNo) {
        this.exportNo = exportNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getExportSql() {
        return exportSql;
    }

    public void setExportSql(String exportSql) {
        this.exportSql = exportSql;
    }

    public String getExportFormat() {
        return exportFormat;
    }

    public void setExportFormat(String exportFormat) {
        this.exportFormat = exportFormat;
    }

    public Integer getNeedTitle() {
        return needTitle;
    }

    public void setNeedTitle(Integer needTitle) {
        this.needTitle = needTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
