package com.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@ApiModel(value = "pageVo", description = "分页信息")
@Data
public class PageVo<T> {

    @ApiModelProperty(name = "totalCount", value = "总数")
    private Integer totalCount=0;

    @ApiModelProperty(name = "list", value = "列表数据")
    private List<T> list= Collections.EMPTY_LIST ;

    public PageVo() {
    }

    public PageVo(Integer totalCount,List<T> list) {
        this.totalCount=totalCount;
        this.list=list;
    }

    //是否越界
    public boolean isBound(int pageNum,int pageSize) {
        return ((pageNum * pageSize - totalCount)-pageSize)>=0;
    }
}
