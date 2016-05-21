package com.shopstat.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 通用统计查询条件类
 */
public class StatQueryVo implements Serializable{

    private static int ZERO_INT=0;

    //统计开始时间  yyyy-MM-dd
    private String statDateBegin;

    //统计结束时间  yyyy-MM-dd
    private String statDateEnd;

    //来源
    private Integer fromSource;

    private Integer tagId;

    private Integer subId;

    private Integer thirdId;

    //扩展查询维度1
    private Integer ext1;

    //扩展查询维度2
    private Integer ext2;

    private Integer page;

    private Integer start;

    private Integer rows;

    public String getStatDateBegin() {
        return statDateBegin;
    }

    public void setStatDateBegin(String statDateBegin) {
        this.statDateBegin = statDateBegin;
    }

    public String getStatDateEnd() {
        return statDateEnd;
    }

    public void setStatDateEnd(String statDateEnd) {
        this.statDateEnd = statDateEnd;
    }

    public Integer getFromSource() {
        return fromSource;
    }

    public void setFromSource(Integer fromSource) {
        this.fromSource = fromSource;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public Integer getThirdId() {
        return thirdId;
    }

    public void setThirdId(Integer thirdId) {
        this.thirdId = thirdId;
    }

    public Integer getExt1() {
        return ext1;
    }

    public void setExt1(Integer ext1) {
        this.ext1 = ext1;
    }

    public Integer getExt2() {
        return ext2;
    }

    public void setExt2(Integer ext2) {
        this.ext2 = ext2;
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

    public void genDef()
    {
        if(fromSource==null)
        {
            fromSource=ZERO_INT;
        }
        if(tagId==null)
        {
            tagId=ZERO_INT;
        }
        if(subId==null)
        {
            subId=ZERO_INT;
        }

        if(thirdId==null)
        {
            thirdId=ZERO_INT;
        }

        if(ext1==null)
        {
            ext1=ZERO_INT;
        }

        if(ext2==null)
        {
            ext2=ZERO_INT;
        }
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

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

}
