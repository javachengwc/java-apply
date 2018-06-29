package com.shop.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "pageParam", description = "分页信息")
public class PageParam {

    @ApiModelProperty(name = "pageNum", value = "第几页")
    protected Integer pageNum = 1;

    @ApiModelProperty(name = "pageSize", value = "每页记录条数")
    protected Integer pageSize = 10;

    @ApiModelProperty(name = "start", value = "开始下标，内部用的")
    protected Integer start = 0;

    //排序字段
    private String orderByColumn;

    //排序顺序 asc--升序,desc--降序
    private String orderBySort;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getOrderBySort() {
        return orderBySort;
    }

    public void setOrderBySort(String orderBySort) {
        this.orderBySort = orderBySort;
    }

    //组装分页
    public void genPage()
    {
        if (pageNum == null || pageNum<=0) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (pageNum - 1) * pageSize;
        if (start < 0) {
            start = 0;
        }
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
