package com.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

@ApiModel(value = "pageVo", description = "分页查询结果")
public class PageVo<T> {

    @ApiModelProperty(name = "totalCount", value = "总数")
    private Integer totalCount;

    @ApiModelProperty(name = "list", value = "列表数据")
    private List<T> list ;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
