package com.storefront.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "roleQueryVo", description = "角色查询条件vo")
public class RoleQueryVo extends PageParam {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "角色code")
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
