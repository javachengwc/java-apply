package com.shop.book.api.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "dictVo", description = "字典信息")
public class DictVo {

    @ApiModelProperty( value = "id")
    private Long id;

    @ApiModelProperty( value = "字典的Key")
    private String dictKey;

    @ApiModelProperty( value = "字典标签")
    private String dictLabel;

    @ApiModelProperty( value = "字典值")
    private String dictValue;

    @ApiModelProperty( value = "字典扩展信息")
    private String dictExt;

    @ApiModelProperty( value = "字典类型")
    private Integer dictType;

    @ApiModelProperty( value = "字典类型名称")
    private String dictTypeName;

    @ApiModelProperty( value = "是否启用 0--否 1--是")
    private Integer isUse;

    @ApiModelProperty( value = "顺序")
    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictExt() {
        return dictExt;
    }

    public void setDictExt(String dictExt) {
        this.dictExt = dictExt;
    }

    public Integer getDictType() {
        return dictType;
    }

    public void setDictType(Integer dictType) {
        this.dictType = dictType;
    }

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
