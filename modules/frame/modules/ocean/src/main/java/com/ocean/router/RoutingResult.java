package com.ocean.router;

import com.ocean.parser.SqlBuilder;

import java.util.Collection;

/**
 * 路由结果接口
 */
public interface RoutingResult {
    /**
     * 获取SQL执行单元集合
     */
   public Collection<SqlExecutionUnit> getSqlExecutionUnits(SqlBuilder sqlBuilder);
}
