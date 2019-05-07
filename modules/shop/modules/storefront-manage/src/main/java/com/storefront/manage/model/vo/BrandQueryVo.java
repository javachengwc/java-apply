package com.storefront.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "brandQueryVo", description = "品牌查询条件")
public class BrandQueryVo extends PageParam {

    @ApiModelProperty(value = "品牌名")
    private String name;

    @ApiModelProperty(value = "顶级行业code")
    private String firstIdstryCode;

    @ApiModelProperty(value = "直接行业code")
    private String directIdstryCode;

    @ApiModelProperty(value = "公司id")
    private Long companyId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstIdstryCode() {
        return firstIdstryCode;
    }

    public void setFirstIdstryCode(String firstIdstryCode) {
        this.firstIdstryCode = firstIdstryCode;
    }

    public String getDirectIdstryCode() {
        return directIdstryCode;
    }

    public void setDirectIdstryCode(String directIdstryCode) {
        this.directIdstryCode = directIdstryCode;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
