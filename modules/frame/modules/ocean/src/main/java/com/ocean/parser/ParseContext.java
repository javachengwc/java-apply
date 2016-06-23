package com.ocean.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import com.google.common.base.CharMatcher;
import com.google.common.base.Optional;
import com.ocean.shard.DatabaseType;
import com.ocean.merger.aggregation.AggregationColumn;
import com.ocean.merger.groupby.GroupByColumn;
import com.ocean.merger.orderby.OrderByColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 解析过程的上下文
 */
public class ParseContext {

    private static final String SHARDING_GEN_ALIAS = "sharding_gen_%s";

    private SqlParsedResult parsedResult = new SqlParsedResult();

    private Collection<String> shardingColumns;

    private boolean hasOrCondition;

    private ConditionContext currentConditionContext = new ConditionContext();

    private SqlTable currentTable;

    private int selectItemsCount;

    public SqlParsedResult getParsedResult() {
        return parsedResult;
    }

    public Collection<String> getShardingColumns() {
        return shardingColumns;
    }

    public boolean isHasOrCondition() {
        return hasOrCondition;
    }

    public ConditionContext getCurrentConditionContext() {
        return currentConditionContext;
    }

    public SqlTable getCurrentTable() {
        return currentTable;
    }

    public int getSelectItemsCount() {
        return selectItemsCount;
    }

    public void setParsedResult(SqlParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }

    public void setShardingColumns(Collection<String> shardingColumns) {
        this.shardingColumns = shardingColumns;
    }

    public void setHasOrCondition(boolean hasOrCondition) {
        this.hasOrCondition = hasOrCondition;
    }

    public void setCurrentConditionContext(ConditionContext currentConditionContext) {
        this.currentConditionContext = currentConditionContext;
    }

    public void setCurrentTable(SqlTable currentTable) {
        this.currentTable = currentTable;
    }

    public void setSelectItemsCount(int selectItemsCount) {
        this.selectItemsCount = selectItemsCount;
    }

    /**
     * 设置当前正在访问的表
     * @param currentTableName 表名称
     * @param currentAlias 表别名
     */
    public void setCurrentTable(final String currentTableName, final Optional<String> currentAlias) {
        SqlTable table = new SqlTable(getExactlyValue(currentTableName), currentAlias.isPresent() ? Optional.of(getExactlyValue(currentAlias.get())) : currentAlias);
        parsedResult.getRouteContext().getTables().add(table);
        currentTable = table;
    }

    /**
     * 将表对象加入解析上下文
     * @param x 表名表达式, 来源于FROM, INSERT ,UPDATE, DELETE等语句
     */
    public SqlTable addTable(final SQLExprTableSource x) {
        SqlTable result = new SqlTable(getExactlyValue(x.getExpr().toString()), getExactlyValue(x.getAlias()));
        parsedResult.getRouteContext().getTables().add(result);
        return result;
    }

    /**
     * 向解析上下文中添加条件对象
     * @param expr SQL表达式
     * @param operator 操作符
     * @param valueExprs 值对象表达式集合
     * @param databaseType 数据库类型
     * @param paramters 通过占位符传进来的参数
     */
    public void addCondition(final SQLExpr expr, final SqlCondition.BinaryOperator operator, final List<SQLExpr> valueExprs, final DatabaseType databaseType, final List<Object> paramters) {
        Optional<SqlCondition.Column> column = getColumn(expr);
        if (!column.isPresent()) {
            return;
        }
        List<Comparable<?>> values = new ArrayList<Comparable<?>>(valueExprs.size());
        for (SQLExpr each : valueExprs) {
            Comparable<?> evalValue = evalExpression(databaseType, each, paramters);
            if (null != evalValue) {
                values.add(evalValue);
            }
        }
        if (values.isEmpty()) {
            return;
        }
        addCondition(column.get(), operator, values);
    }

    /**
     * 将条件对象加入解析上下文
     */
    public void addCondition(final String columnName, final String tableName, final SqlCondition.BinaryOperator operator, final SQLExpr valueExpr, final DatabaseType databaseType, final List<Object> paramters) {
        Comparable<?> value = evalExpression(databaseType, valueExpr, paramters);
        if (null != value) {
            addCondition(createColumn(columnName, tableName), operator, Arrays.<Comparable<?>>asList(value));
        }
    }

    private void addCondition(final SqlCondition.Column column, final SqlCondition.BinaryOperator operator, final List<Comparable<?>> values) {
        if (!shardingColumns.contains(column.getColumnName())) {
            return;
        }
        Optional<SqlCondition> optionalCondition = currentConditionContext.find(column.getTableName(), column.getColumnName(), operator);
        SqlCondition condition;
        if (optionalCondition.isPresent()) {
            condition = optionalCondition.get();
        } else {
            condition = new SqlCondition(column, operator);
            currentConditionContext.add(condition);
        }
        condition.getValues().addAll(values);
    }

    private Comparable<?> evalExpression(final DatabaseType databaseType, final SQLObject sqlObject, final List<Object> parameters) {
        if (sqlObject instanceof SQLMethodInvokeExpr) {
            return null;
        }
        Object result = SQLEvalVisitorUtils.eval(databaseType.name().toLowerCase(), sqlObject, parameters, false);
        if (null == result) {
            return null;
        }
        if (result instanceof Comparable<?>) {
            return (Comparable<?>) result;
        }
        return "";
    }

    private Optional<SqlCondition.Column> getColumn(final SQLExpr expr) {
        if (expr instanceof SQLPropertyExpr) {
            return Optional.fromNullable(getColumnWithQualifiedName((SQLPropertyExpr) expr));
        }
        if (expr instanceof SQLIdentifierExpr) {
            return Optional.fromNullable(getColumnWithoutAlias((SQLIdentifierExpr) expr));
        }
        return Optional.absent();
    }

    private SqlCondition.Column getColumnWithQualifiedName(final SQLPropertyExpr expr) {
        Optional<SqlTable> table = findTable(((SQLIdentifierExpr) expr.getOwner()).getName());
        return expr.getOwner() instanceof SQLIdentifierExpr && table.isPresent() ? createColumn(expr.getName(), table.get().getName()) : null;
    }

    private SqlCondition.Column getColumnWithoutAlias(final SQLIdentifierExpr expr) {
        return null != currentTable ? createColumn(expr.getName(), currentTable.getName()) : null;
    }

    private SqlCondition.Column createColumn(final String columName, final String tableName) {
        return new SqlCondition.Column(getExactlyValue(columName), getExactlyValue(tableName));
    }

    private Optional<SqlTable> findTable(final String tableNameOrAlias) {
        Optional<SqlTable> tableFromName = findTableFromName(tableNameOrAlias);
        return tableFromName.isPresent() ? tableFromName : findTableFromAlias(tableNameOrAlias);
    }

    /**
     * 判断SQL表达式是否为二元操作且带有别名.
     *
     * @param x 待判断的SQL表达式
     * @param tableOrAliasName 表名称或别名
     * @return 是否为二元操作且带有别名
     */
    public boolean isBinaryOperateWithAlias(final SQLPropertyExpr x, final String tableOrAliasName) {
        return x.getParent() instanceof SQLBinaryOpExpr && findTableFromAlias(getExactlyValue(tableOrAliasName)).isPresent();
    }

    private Optional<SqlTable> findTableFromName(final String name) {
        for (SqlTable each : parsedResult.getRouteContext().getTables()) {
            if (each.getName().equalsIgnoreCase(getExactlyValue(name))) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    private Optional<SqlTable> findTableFromAlias(final String alias) {
        for (SqlTable each : parsedResult.getRouteContext().getTables()) {
            if (each.getAlias().isPresent() && each.getAlias().get().equalsIgnoreCase(getExactlyValue(alias))) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    /**
     * 将求平均值函数的补列加入解析上下文.
     * @param avgColumn 求平均值的列
     */
    public void addDerivedColumnsForAvgColumn(final AggregationColumn avgColumn) {
        addDerivedColumnForAvgColumn(avgColumn, getDerivedCountColumn(avgColumn));
        addDerivedColumnForAvgColumn(avgColumn, getDerivedSumColumn(avgColumn));
    }

    private void addDerivedColumnForAvgColumn(final AggregationColumn avgColumn, final AggregationColumn derivedColumn) {
        avgColumn.getDerivedColumns().add(derivedColumn);
        parsedResult.getMergeContext().getAggregationColumns().add(derivedColumn);
    }

    private AggregationColumn getDerivedCountColumn(final AggregationColumn avgColumn) {
        String expression = avgColumn.getExpression().replaceFirst(AggregationColumn.AggregationType.AVG.toString(), AggregationColumn.AggregationType.COUNT.toString());
        return new AggregationColumn(expression, AggregationColumn.AggregationType.COUNT, Optional.of(generateDerivedColumnAlias()), avgColumn.getOption());
    }

    private AggregationColumn getDerivedSumColumn(final AggregationColumn avgColumn) {
        String expression = avgColumn.getExpression().replaceFirst(AggregationColumn.AggregationType.AVG.toString(), AggregationColumn.AggregationType.SUM.toString());
        if (avgColumn.getOption().isPresent()) {
            expression = expression.replaceFirst(avgColumn.getOption().get() + " ", "");
        }
        return new AggregationColumn(expression, AggregationColumn.AggregationType.SUM, Optional.of(generateDerivedColumnAlias()), Optional.<String>absent());
    }

    /**
     * 将排序列加入解析上下文.
     *
     * @param index 列顺序索引
     * @param orderByType 排序类型
     */
    public void addOrderByColumn(final int index, final OrderByColumn.OrderByType orderByType) {
        parsedResult.getMergeContext().getOrderByColumns().add(new OrderByColumn(index, orderByType));
    }

    /**
     * 将排序列加入解析上下文.
     *
     * @param name 列名称
     * @param orderByType 排序类型
     */
    public void addOrderByColumn(final String name, final OrderByColumn.OrderByType orderByType) {
        parsedResult.getMergeContext().getOrderByColumns().add(new OrderByColumn(getExactlyValue(name), orderByType));
    }

    /**
     * 将分组列加入解析上下文.
     *
     * @param name 列名称
     * @param alias 列别名
     * @param orderByType 排序类型
     */
    public void addGroupByColumns(final String name, final String alias, final OrderByColumn.OrderByType orderByType) {
        parsedResult.getMergeContext().getGroupByColumns().add(new GroupByColumn(getExactlyValue(name), alias, orderByType));
    }

    /**
     * 生成补列别名.
     *
     * @return 补列的别名
     */
    public String generateDerivedColumnAlias() {
        return String.format(SHARDING_GEN_ALIAS, ++selectItemsCount);
    }

    /**
     * 去掉SQL表达式的特殊字符.
     *
     * @param value SQL表达式
     * @return 去掉SQL特殊字符的表达式
     */
    public String getExactlyValue(final String value) {
        return null == value ? null : CharMatcher.anyOf("[]`'\"").removeFrom(value);
    }

    /**
     * 将当前解析的条件对象归并入解析结果.
     */
    public void mergeCurrentConditionContext() {
        parsedResult.getConditionContexts().add(currentConditionContext);
    }
}
