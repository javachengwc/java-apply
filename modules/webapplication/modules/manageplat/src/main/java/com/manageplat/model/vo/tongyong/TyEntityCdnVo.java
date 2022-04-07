package com.manageplat.model.vo.tongyong;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 通用列表实体项查询条件
 */
public class TyEntityCdnVo {

    private Integer id;

    private Integer entityId;

    //条件名
    private String cdnName;

    //条件列
    private String cdnCol;

    //条件类型 0--int 1--string 就这2类
    //int 类型在组装sql语句查询条件参数的时候 不加引号
    //string 类型在组装sql语句查询时，加引号
    private Integer cdnType=0;

    //是否显示
    private Integer isShow=1;

    //显示顺序
    private Integer sort;

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

    public String getCdnName() {
        return cdnName;
    }

    public void setCdnName(String cdnName) {
        this.cdnName = cdnName;
    }

    public String getCdnCol() {
        return cdnCol;
    }

    public void setCdnCol(String cdnCol) {
        this.cdnCol = cdnCol;
    }

    public Integer getCdnType() {
        return cdnType;
    }

    public void setCdnType(Integer cdnType) {
        this.cdnType = cdnType;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
