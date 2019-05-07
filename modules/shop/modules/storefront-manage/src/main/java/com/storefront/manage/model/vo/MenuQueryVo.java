package com.storefront.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "menuQueryVo", description = "菜单查询条件vo")
public class MenuQueryVo extends PageParam {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty("类型：0--目录，1--菜单，2--按钮")
    private Integer type;

    @ApiModelProperty("菜单url")
    private String url;

    @ApiModelProperty("父菜单id")
    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
