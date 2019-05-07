package com.storefront.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "industryQueryVo", description = "行业查询条件")
public class IndustryQueryVo extends PageParam {

    @ApiModelProperty("行业code")
    private String code;

    @ApiModelProperty("行业名称")
    private String name;

    @ApiModelProperty("行业父code")
    private String parentCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
