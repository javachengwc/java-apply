package com.shop.book.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "dictQueryVo", description = "字典查询条件")
public class DictQueryVo extends PageParam {

    @ApiModelProperty(value = "配置项key")
    private String dictKey;

    @ApiModelProperty(value = "配置项类型")
    private Integer dictType;

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public Integer getDictType() {
        return dictType;
    }

    public void setDictType(Integer dictType) {
        this.dictType = dictType;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
