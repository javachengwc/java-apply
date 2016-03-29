package com.manageplat.model.vo.tongyong;

import com.manageplat.model.pojo.TyEntity;
import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.pojo.TyEntityItem;

import java.util.List;
import java.util.Map;

/**
 * 通用查询结果实体
 */
public class TyResultVo {

    private TyEntity entity;

    private List<TyEntityItem> entityItem;

    private List<TyEntityCdn> entityCdn;

    private Map<String,Object> paramMap;

    private int count;

    private List<Map<String,Object>> list;

    public TyEntity getEntity() {
        return entity;
    }

    public void setEntity(TyEntity entity) {
        this.entity = entity;
    }

    public List<TyEntityItem> getEntityItem() {
        return entityItem;
    }

    public void setEntityItem(List<TyEntityItem> entityItem) {
        this.entityItem = entityItem;
    }

    public List<TyEntityCdn> getEntityCdn() {
        return entityCdn;
    }

    public void setEntityCdn(List<TyEntityCdn> entityCdn) {
        this.entityCdn = entityCdn;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
