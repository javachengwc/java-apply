package com.manageplat.dao.tongyong;

import java.util.List;
import java.util.Map;

/**
 *通用查询访问接口类
 */
public interface TyQueryDao {

    /**
     * 列表查询
     * @param querySql
     * @return
     */
    public List<Map<String,Object>> queryList(String querySql);

    /**
     * 总数
     * @param countSql
     * @return
     */
    public int count(String countSql);

}
