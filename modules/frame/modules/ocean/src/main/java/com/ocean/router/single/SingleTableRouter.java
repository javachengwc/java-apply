package com.ocean.router.single;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.ocean.shard.model.ShardValue;
import com.ocean.shard.model.Table;
import com.ocean.shard.rule.ShardRule;
import com.ocean.shard.rule.TableRule;
import com.ocean.shard.strategy.ShardStrategy;
import com.ocean.parser.ConditionContext;
import com.ocean.parser.SqlCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 *  单逻辑表的库表路由
 */
public class SingleTableRouter {

    private static Logger logger = LoggerFactory.getLogger(SingleTableRouter.class);

    private ShardRule shardRule;

    private String logicTable;

    private ConditionContext conditionContext;

    private Optional<TableRule> tableRule;

    public SingleTableRouter()
    {

    }

    public SingleTableRouter(ShardRule shardRule,String logicTable, ConditionContext conditionContext) {
        this.shardRule = shardRule;
        this.logicTable = logicTable;
        this.conditionContext = conditionContext;
        tableRule = shardRule.findTableRule(logicTable);
    }

    public ShardRule getShardRule() {
        return shardRule;
    }

    public void setShardRule(ShardRule shardRule) {
        this.shardRule = shardRule;
    }

    public String getLogicTable() {
        return logicTable;
    }

    public void setLogicTable(String logicTable) {
        this.logicTable = logicTable;
    }

    public ConditionContext getConditionContext() {
        return conditionContext;
    }

    public void setConditionContext(ConditionContext conditionContext) {
        this.conditionContext = conditionContext;
    }

    public Optional<TableRule> getTableRule() {
        return tableRule;
    }

    public void setTableRule(Optional<TableRule> tableRule) {
        this.tableRule = tableRule;
    }

    /**
     * 路由
     */
    public SingleRoutingResult route() {
        if (!tableRule.isPresent()) {
            logger.info("SingleTableRouter route can not find table rule of {}", logicTable);
            return null;
        }
        Collection<String> routedDataSources = routeDataSources();
        Collection<String> routedTables = routeTables(routedDataSources);
        SingleRoutingResult result = generateRoutingResult(routedDataSources, routedTables);

        logger.info("SingleTableRouter route logicTable="+logicTable+",result ="+result);
        return result;
    }

    private Collection<String> routeDataSources() {
        ShardStrategy strategy = shardRule.getDatabaseShardStrategy(tableRule.get());
        List<ShardValue<?>> databaseShardingValues = getShardingValues(strategy.getShardObjs());
        //logBeforeRoute("database", logicTable, shardRule.getDataSourceRule().getDataSourceNames(), strategy.getShardObjs(), databaseShardingValues);
        Collection<String> result = new HashSet<String>(strategy.doShard(shardRule.getDataSourceRule().getDataSourceNames(), databaseShardingValues));
        //logAfterRoute("database", logicTable, result);
        Preconditions.checkState(!result.isEmpty(), "no database route info");
        return result;
    }

    private Collection<String> routeTables(final Collection<String> routedDataSources) {
        ShardStrategy strategy = shardRule.getTableShardingStrategy(tableRule.get());
        List<ShardValue<?>> tableShardValues = getShardingValues(strategy.getShardObjs());
        //logBeforeRoute("table", logicTable, tableRule.get().getActualTables(), strategy.getShardObjs(), tableShardValues);
        Collection<String> result = new HashSet<String>(strategy.doShard(tableRule.get().getActualTableNames(routedDataSources), tableShardValues));
        //logAfterRoute("table", logicTable, result);
        Preconditions.checkState(!result.isEmpty(), "no table route info");
        return result;
    }

    private List<ShardValue<?>> getShardingValues(final Collection<String> shardingColumns) {
        List<ShardValue<?>> result = new ArrayList<ShardValue<?>>(shardingColumns.size());
        for (String each : shardingColumns) {
            Optional<SqlCondition> condition = conditionContext.find(logicTable, each);
            if (condition.isPresent()) {
                result.add(getShardingValue(condition.get()));
            }
        }
        return result;
    }

    private ShardValue<?> getShardingValue(SqlCondition condition) {
        List<Comparable<?>> conditionValues = condition.getValues();
        switch (condition.getOperator()) {
            case EQUAL:
            case IN:
                if (1 == conditionValues.size()) {
                    return new ShardValue<Comparable<?>>(condition.getColumn().getColumnName(), conditionValues.get(0));
                }
                return new ShardValue<Comparable<?>>(condition.getColumn().getColumnName(), conditionValues);
            case BETWEEN:
                return new ShardValue<Comparable<?>>(condition.getColumn().getColumnName(),Range.range(conditionValues.get(0), BoundType.CLOSED, conditionValues.get(1), BoundType.CLOSED));
            default:
                throw new UnsupportedOperationException(condition.getOperator().getExpression());
        }
    }

    private void logBeforeRoute(String type, String logicTable, Collection<?> targets, Collection<String> shardColumns,List<ShardValue<?>> shardValues) {
        logger.info("SingleTableRouter rount before {}", type);
        logger.info("SingleTableRouter route logicTable {}", logicTable);
        logger.info("SingleTableRouter route  db names: {}", targets);
        logger.info("SingleTableRouter route  columns: {}", shardColumns);
        logger.info("SingleTableRouter route  values: {}", shardValues);
    }

    private void logAfterRoute(String type, String logicTable, Collection<String> shardResults) {
        logger.info("SingleTableRouter route after {} ", type);
        logger.info("SingleTableRouter route logicTable {}", logicTable);
        logger.info("SingleTableRouter route result: {}", shardResults);
    }

    private SingleRoutingResult generateRoutingResult(final Collection<String> routedDataSources, final Collection<String> routedTables) {
        SingleRoutingResult result = new SingleRoutingResult();
        for (Table each : tableRule.get().getActualTables(routedDataSources, routedTables)) {
            result.put(each.getDb(), new SingleRoutingTableFactor(logicTable, each.getName()));
        }
        return result;
    }
}