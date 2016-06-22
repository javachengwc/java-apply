package com.ocean.merger;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 分页对象
 */
public class Limit {

    private  int offset;

    private  int rowCount;

    public Limit()
    {

    }

    public Limit(int offset,int rowCount)
    {
        this.offset=offset;
        this.rowCount=rowCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

}

