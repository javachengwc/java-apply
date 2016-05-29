package com.shopstat.model.vo.stat;

import com.util.StringUtil;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 维度类
 */
public class Dimen implements Serializable {

    //维度名
    private String dimenName;

    //列名
    private String columnName;

    //维度类型
    private Class clazz;

    //维护选项值列表
    private List<String> options= Collections.emptyList();

    //选项值中代表全部的选项值
    private String totalOption;

    public Dimen()
    {

    }

    public Dimen(String dimenName,Class clazz, List<String> options,String totalOption)
    {
        this.dimenName=dimenName;
        this.columnName= StringUtil.field2Col(dimenName);
        this.clazz=clazz;
        this.options=options;
        this.totalOption=totalOption;
    }

    public String getDimenName() {
        return dimenName;
    }

    public void setDimenName(String dimenName) {
        this.dimenName = dimenName;
        this.columnName= StringUtil.field2Col(dimenName);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getTotalOption() {
        return totalOption;
    }

    public void setTotalOption(String totalOption) {
        this.totalOption = totalOption;
    }

    public void addOption(String option)
    {
        if(options==null)
        {
            options= new ArrayList<String>();
        }
        options.add(option);
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
