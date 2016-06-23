package com.ocean.shard.rule;

import com.google.common.base.Optional;
import com.ocean.shard.strategy.BaseShardAlgorithm;
import com.ocean.shard.strategy.BaseShardStrategy;
import com.ocean.shard.strategy.ShardStrategy;

import java.util.*;

/**
 * 分片规则
 */
public class ShardRule {

    private  DataSourceRule dataSourceRule;

    private Collection<TableRule> tableRules;

    private Collection<BindingTableRule> bindingTableRules;

    private ShardStrategy databaseShardStrategy;

    private ShardStrategy tableShardStrategy;

    public DataSourceRule getDataSourceRule() {
        return dataSourceRule;
    }

    public void setDataSourceRule(DataSourceRule dataSourceRule) {
        this.dataSourceRule = dataSourceRule;
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

    public ShardRule(DataSourceRule dataSourceRule, Collection<TableRule> tableRules) {
        this(dataSourceRule, tableRules, Collections.<BindingTableRule>emptyList(),
                new BaseShardStrategy(Collections.<String>emptyList(), new BaseShardAlgorithm()),
                new BaseShardStrategy(Collections.<String>emptyList(), new BaseShardAlgorithm()));
    }

    public ShardRule(DataSourceRule dataSourceRule, Collection<TableRule> tableRules, Collection<BindingTableRule> bindingTableRules) {
        this(dataSourceRule, tableRules, bindingTableRules,
                new BaseShardStrategy(Collections.<String>emptyList(), new BaseShardAlgorithm()),
                new BaseShardStrategy(Collections.<String>emptyList(), new BaseShardAlgorithm()));
    }

    public ShardRule(DataSourceRule dataSourceRule, Collection<TableRule> tableRules,Collection<BindingTableRule>  bindingTableRules,
                        ShardStrategy databaseShardingStrategy, ShardStrategy tableShardingStrategy) {
        this.dataSourceRule=dataSourceRule;
        this.tableRules=tableRules;
        this.bindingTableRules=bindingTableRules;
        this.databaseShardStrategy=databaseShardingStrategy;
        this.tableShardStrategy=tableShardingStrategy;

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
    //目前使用分片列名称, 为了进一步提升解析性能，应考虑使用表名 + 列名
    public Collection<String> getAllShardingColumns() {
        Set<String> result = new HashSet<String>();
        if (null != databaseShardStrategy) {
            result.addAll(databaseShardStrategy.getShardObjs());
        }
        if (null != tableShardStrategy) {
            result.addAll(tableShardStrategy.getShardObjs());
        }
        for (TableRule each : tableRules) {
            if (null != each.getDatabaseShardStrategy()) {
                result.addAll(each.getDatabaseShardStrategy().getShardObjs());
            }
            if (null != each.getTableShardStrategy()) {
                result.addAll(each.getTableShardStrategy().getShardObjs());
            }
        }
        return result;
    }
}