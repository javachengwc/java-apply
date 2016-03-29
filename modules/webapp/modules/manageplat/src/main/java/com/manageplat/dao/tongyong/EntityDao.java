package com.manageplat.dao.tongyong;

import com.manageplat.model.pojo.TyEntity;
import com.manageplat.model.vo.tongyong.TyEntityVo;

import java.util.List;

/**
 * 通用列表实体访问接口类
 */
public interface EntityDao {

    public List<TyEntity> queryPage(TyEntityVo entity,int start,int pageSize);

    public int count(TyEntityVo entity);
}
