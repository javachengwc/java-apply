package com.manageplat.model.vo.tongyong;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 通用列表实体列项
 */
public class TyEntityItemVo {

    private Integer id;

    private Integer entityId;

    //列项名
    private String itemName;

    //列项对应的结果集列
    private String itemCol;

    //排序号
    private String sort;

    //展示格式化，比如类似123123-->25小时16分
    private String format;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCol() {
        return itemCol;
    }

    public void setItemCol(String itemCol) {
        this.itemCol = itemCol;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
