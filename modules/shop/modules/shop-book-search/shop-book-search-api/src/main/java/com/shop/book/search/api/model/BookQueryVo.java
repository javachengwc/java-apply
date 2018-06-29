package com.shop.book.search.api.model;

import com.shop.base.model.PageParam;
import com.shop.book.search.api.enums.BookSortEnum;
import com.shop.book.search.api.enums.BookStatuEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "bookQueryVo", description = "书籍分页查询条件")
public class BookQueryVo extends PageParam {

    @ApiModelProperty(name = "keyword", value = "关键字")
    private String keyword;

    @ApiModelProperty(name = "minPrice", value = "最低价格")
    private Integer minPrice;

    @ApiModelProperty(name = "maxPrice", value = "最高价格")
    private Integer maxPrice;

    @ApiModelProperty(name = "sortValue", value = "排序值")
    private String sortValue;

    private Integer statu;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    //查询准备
    public void ready() {
        //组装条件
        this.genCdn();
        //组装分页
        this.genPage();
        //组装排序
        this.genOrderBy();
    }

    //组装条件
    public void genCdn() {
        if(statu==null) {
            statu= BookStatuEnum.UP_SHELF.getValue();
        }
    }

    //组装排序
    public void genOrderBy() {
        BookSortEnum sortEnum = BookSortEnum.getSortEnumByValue(sortValue);
        if(sortEnum==null) {
            sortEnum= BookSortEnum.LASTED_SHELF;
        }
        this.setOrderByColumn(sortEnum.getOrderByColumn());
        this.setOrderBySort(sortEnum.getOrderBySort().getValue());
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
