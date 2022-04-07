package com.manageplat.dao.tongyong;

import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.vo.tongyong.TyEntityCdnVo;

import java.util.List;

/**
 * 通用列表实体条件访问接口类
 */
public interface EntityCdnDao {

    public List<TyEntityCdn> queryPage(TyEntityCdnVo entityCdn,int start,int pageSize);

    public int count(TyEntityCdnVo entityCdn);
}
