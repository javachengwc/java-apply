package com.manageplat.model.eximport;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class TyImportRecord {

    private Integer id;

    private String importNo;

    private String operator;

    private Date operateTime;

    private Integer state;

    private Integer isTmp;

    private String tableName;

    private Date finishTime;

    private String dataFormat;

    private Integer hasTitle;

    private Integer titleCol;

    private Integer fromLine;

    private String lineSepar;

    private String colSepar;

    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImportNo() {
        return importNo;
    }

    public void setImportNo(String importNo) {
        this.importNo = importNo;
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

    public Integer getIsTmp() {
        return isTmp;
    }

    public void setIsTmp(Integer isTmp) {
        this.isTmp = isTmp;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public Integer getHasTitle() {
        return hasTitle;
    }

    public void setHasTitle(Integer hasTitle) {
        this.hasTitle = hasTitle;
    }

    public Integer getTitleCol() {
        return titleCol;
    }

    public void setTitleCol(Integer titleCol) {
        this.titleCol = titleCol;
    }

    public Integer getFromLine() {
        return fromLine;
    }

    public void setFromLine(Integer fromLine) {
        this.fromLine = fromLine;
    }

    public String getLineSepar() {
        return lineSepar;
    }

    public void setLineSepar(String lineSepar) {
        this.lineSepar = lineSepar;
    }

    public String getColSepar() {
        return colSepar;
    }

    public void setColSepar(String colSepar) {
        this.colSepar = colSepar;
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
