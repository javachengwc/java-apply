package com.manageplat.dao.tongyong;

import com.manageplat.model.pojo.TyEntityItem;
import com.manageplat.model.vo.tongyong.TyEntityItemVo;

import java.util.List;

/**
 * 通用列表实体列项访问接口类
 */
public interface EntityItemDao {

    public List<TyEntityItem> queryPage(TyEntityItemVo entityItem,int start,int pageSize);

    public int count(TyEntityItemVo entityItem);
}
