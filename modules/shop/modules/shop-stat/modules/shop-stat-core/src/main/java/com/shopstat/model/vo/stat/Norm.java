package com.shopstat.model.vo.stat;

import com.util.base.StringUtil;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 指标类
 */
public class Norm implements Serializable {

    public enum NormType
    {
        SUM("sum"),AVG("avg"),MAX("max"),MIN("min");

        private String funcName;

        private NormType(String funcName)
        {
            this.funcName=funcName;
        }

        public String getFuncName() {
            return funcName;
        }

        public void setFuncName(String funcName) {
            this.funcName = funcName;
        }
    }

    //指标名
    private String normName;

    //指标列
    private String normColumn;

    //指标值类
    private Class clazz;

    //指标集合类型
    public NormType type;

    public Norm()
    {

    }

    public Norm(String normName,Class clazz)
    {
        this(normName,clazz,NormType.SUM);
    }

    public Norm(String normName,Class clazz,NormType type)
    {
        this.normName=normName;
        this.normColumn= StringUtil.field2Col(normName);
        this.clazz=clazz;
        this.type=type;
    }

    public String getNormName() {
        return normName;
    }

    public void setNormName(String normName) {
        this.normName = normName;
        this.normColumn= StringUtil.field2Col(normName);
    }

    public String getNormColumn() {
        return normColumn;
    }

    public void setNormColumn(String normColumn) {
        this.normColumn = normColumn;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public NormType getType() {
        return type;
    }

    public void setType(NormType type) {
        this.type = type;
    }

    public String getQueryFragment()
    {
        return type.getFuncName()+"("+normColumn+") as "+normColumn;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
