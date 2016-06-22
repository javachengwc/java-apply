package com.ocean.parser;

import com.google.common.base.Optional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 条件对象上下文
 */
public class ConditionContext {

    private Map<SqlCondition.Column, SqlCondition> conditions = new LinkedHashMap<SqlCondition.Column, SqlCondition>();

    /**
     * 添加条件对象
     */
    public void add(SqlCondition condition) {
        conditions.put(condition.getColumn(), condition);
    }

    /**
     * 查找条件对象
     * @param table 表名称
     * @param column 列名称
     * @return 条件对象
     */
    public Optional<SqlCondition> find(String table, String column) {
        return Optional.fromNullable(conditions.get(new SqlCondition.Column(column, table)));
    }

    /**
     * 查找条件对象
     * @param table 表名称
     * @param column 列名称
     * @param operator 操作符
     * @return 条件对象
     */
    public Optional<SqlCondition> find(String table, String column, SqlCondition.BinaryOperator operator) {
        Optional<SqlCondition> result = find(table, column);
        if (!result.isPresent()) {
            return result;
        }
        return (result.get().getOperator() == operator) ? result : Optional.<SqlCondition>absent();
    }

    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    public void clear() {
        conditions.clear();
    }

    public Collection<SqlCondition> getAllConditions() {
        return conditions.values();
    }
}

