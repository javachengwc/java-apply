package com.shop.web.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
public class SofaQueryVo {

    private Long brandId;

    private Long materId;

    private Long typeId;

    private Long styleId;

    private Integer page;

    private Integer start;

    private Integer rows;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getMaterId() {
        return materId;
    }

    public void setMaterId(Long materId) {
        this.materId = materId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
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

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
