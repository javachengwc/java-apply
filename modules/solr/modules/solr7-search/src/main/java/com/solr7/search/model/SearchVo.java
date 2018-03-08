package com.solr7.search.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(description = "检索对象" , value = "searchVo")
public class SearchVo {

    @ApiModelProperty(name = "city" , value = "城市")
    private String city;

    @ApiModelProperty(name = "minPrice" , value = "最低价格")
    private Integer minPrice;

    @ApiModelProperty(name = "maxPrice" , value = "最高价格")
    private Integer maxPrice;

    @ApiModelProperty(name = "keyword" , value = "关键字")
    private String keyword;

    @ApiModelProperty(name = "order" , value = "排序字段")
    private String order;

    @ApiModelProperty(name = "desc" , value = "升降序 0--升序 1--降序 ")
    private Integer desc;

    @ApiModelProperty(name = "page" , value = "第几页")
    private Integer page;

    @ApiModelProperty(name = "pageSize" , value = "每页条数")
    private Integer pageSize;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getDesc() {
        return desc;
    }

    public void setDesc(Integer desc) {
        this.desc = desc;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
