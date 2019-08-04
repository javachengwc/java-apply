package com.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "pageParam", description = "分页参数")
@Data
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
}
