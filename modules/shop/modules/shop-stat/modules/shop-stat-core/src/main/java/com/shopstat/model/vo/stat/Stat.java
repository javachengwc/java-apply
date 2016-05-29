package com.shopstat.model.vo.stat;

import com.util.StringUtil;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 统计类
 */
public class Stat implements Serializable {

    //统计实体类
    public Class statClazz;

    //统计对应的表
    public String table;

    //统计日期
    public Date statDate;

    //统计日期field名
    public String dateName;

    //统计日期列名
    public String dateColumn;

    //统计的维度列表
    public List<Dimen> dimenList;

    //统计的指标列表
    public List<Norm> normList;

    public Class getStatClazz() {
        return statClazz;
    }

    public void setStatClazz(Class statClazz) {
        this.statClazz = statClazz;
        this.table=StringUtil.field2Col(statClazz.getSimpleName());
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
        this.dateColumn=StringUtil.field2Col(dateName);
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public List<Dimen> getDimenList() {
        return dimenList;
    }

    public void setDimenList(List<Dimen> dimenList) {
        this.dimenList = dimenList;
    }

    public List<Norm> getNormList() {
        return normList;
    }

    public void setNormList(List<Norm> normList) {
        this.normList = normList;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
