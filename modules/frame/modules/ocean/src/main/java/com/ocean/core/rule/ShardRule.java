package com.ocean.core.rule;

import com.google.common.base.Optional;
import com.ocean.core.strategy.BaseShardAlgorithm;
import com.ocean.core.strategy.BaseShardStrategy;
import com.ocean.core.strategy.ShardStrategy;

import javax.sql.DataSource;
import java.util.*;

/**
 * 分片规则
 */
public class ShardRule {

    private Map<String, DataSource> dataSourceMap;

    private Collection<TableRule> tableRules;

    private ShardStrategy databaseShardStrategy;

    private ShardStrategy tableShardStrategy;

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public Collection<TableRule> getTableRules() {
        return tableRules;
    }

    public void setTableRules(Collection<TableRule> tableRules) {
        this.tableRules = tableRules;
    }

    public ShardStrategy getDatabaseShardStrategy() {
        return databaseShardStrategy;
    }

    public void setDatabaseShardStrategy(ShardStrategy databaseShardStrategy) {
        this.databaseShardStrategy = databaseShardStrategy;
    }

    public ShardStrategy getTableShardStrategy() {
        return tableShardStrategy;
    }

    public void setTableShardStrategy(ShardStrategy tableShardStrategy) {
        this.tableShardStrategy = tableShardStrategy;
    }

    public ShardRule(Map<String, DataSource> dataSourceMap, Collection<TableRule> tableRules) {
        this(dataSourceMap, tableRules,
            new BaseShardStrategy(Collections.<String>emptyList(), new BaseShardAlgorithm()),
            new BaseShardStrategy(Collections.<String>emptyList(), new BaseShardAlgorithm()));
    }

    public ShardRule(Map<String, DataSource> dataSourceMap, Collection<TableRule> tableRules, ShardStrategy databaseShardStrategy) {
        this(dataSourceMap, tableRules,databaseShardStrategy,
            new BaseShardStrategy(Collections.<String>emptyList(), new BaseShardAlgorithm()));
    }

    public ShardRule(Map<String, DataSource> dataSourceMap, Collection<TableRule> tableRules,ShardStrategy databaseShardStrategy,ShardStrategy tableShardStrategy) {

        this.dataSourceMap =dataSourceMap;
        this.tableRules=tableRules;
        this.databaseShardStrategy=databaseShardStrategy;
        this.tableShardStrategy =tableShardStrategy;

    }

    //根据逻辑表名称查找分片规则
    public Optional<TableRule> findTableRule(final String logicTableName) {
        for (TableRule each : tableRules) {
            if (each.getLogicTable().equals(logicTableName)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    //获取数据库分片策略
    public ShardStrategy getDatabaseShardStrategy(TableRule tableRule) {
        ShardStrategy result = tableRule.getDatabaseShardStrategy();
        if (null == result) {
            result = databaseShardStrategy;
        }
        return result;
    }

    //获取表分片策略
    public ShardStrategy getTableShardingStrategy(final TableRule tableRule) {
        ShardStrategy result = tableRule.getTableShardStrategy();
        if (null == result) {
            result = tableShardStrategy;
        }
        return result;
    }

    /**
     * 根据逻辑表名称获取binding表配置的逻辑表名称集合.
     *
     * @param logicTable 逻辑表名称
     * @return binding表配置的逻辑表名称集合
     */
    public Optional<BindingTableRule> getBindingTableRule(final String logicTable) {
        if (null == bindingTableRules) {
            return Optional.absent();
        }
        for (BindingTableRule each : bindingTableRules) {
            if (each.hasLogicTable(logicTable)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    /**
     * 过滤出所有的Binding表名称.
     *
     * @param logicTables 逻辑表名称集合
     * @return 所有的Binding表名称集合
     */
    public Collection<String> filterAllBindingTables(final Collection<String> logicTables) {
        if (logicTables.isEmpty()) {
            return Collections.emptyList();
        }
        Optional<BindingTableRule> bindingTableRule = Optional.absent();
        for (String each : logicTables) {
            bindingTableRule = getBindingTableRule(each);
            if (bindingTableRule.isPresent()) {
                break;
            }
        }
        if (!bindingTableRule.isPresent()) {
            return Collections.emptyList();
        }
        Collection<String> result = new ArrayList<String>(bindingTableRule.get().getAllLogicTables());
        result.retainAll(logicTables);
        return result;
    }

    /**
     * 判断逻辑表名称集合是否全部属于Binding表.
     *
     * @param logicTables 逻辑表名称集合
     * @return 是否全部属于Binding表
     */
    public boolean isAllBindingTable(final Collection<String> logicTables) {
        Collection<String> bindingTables = filterAllBindingTables(logicTables);
        return !bindingTables.isEmpty() && bindingTables.containsAll(logicTables);
    }

    /**
     * 获取所有的分片列名
     */
    // TODO 目前使用分片列名称, 为了进一步提升解析性能，应考虑使用表名 + 列名
    public Collection<String> getAllShardingColumns() {
        Set<String> result = new HashSet<String>();
        if (null != databaseShardStrategy) {
            result.addAll(databaseShardStrategy.getShardingColumns());
        }
        if (null != tableShardingStrategy) {
            result.addAll(tableShardingStrategy.getShardingColumns());
        }
        for (TableRule each : tableRules) {
            if (null != each.getDatabaseShardingStrategy()) {
                result.addAll(each.getDatabaseShardingStrategy().getShardingColumns());
            }
            if (null != each.getTableShardingStrategy()) {
                result.addAll(each.getTableShardingStrategy().getShardingColumns());
            }
        }
        return result;
    }
}