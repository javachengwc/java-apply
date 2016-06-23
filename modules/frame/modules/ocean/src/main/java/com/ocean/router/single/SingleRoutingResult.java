package com.ocean.router.single;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.ocean.parser.SqlBuilder;
import com.ocean.router.RoutingResult;
import com.ocean.router.SqlExecutionUnit;

import java.util.*;

/**
 * 单表路由结果
 */
public class SingleRoutingResult implements RoutingResult {

    private List<SingleRoutingDataSource> routingDataSources = new ArrayList<SingleRoutingDataSource>();

    public List<SingleRoutingDataSource> getRoutingDataSources() {
        return routingDataSources;
    }

    public void setRoutingDataSources(List<SingleRoutingDataSource> routingDataSources) {
        this.routingDataSources = routingDataSources;
    }

    public void put(final String dataSourceName, final SingleRoutingTableFactor routingTableFactor) {
        for (SingleRoutingDataSource each : routingDataSources) {
            if (each.getDataSource().equals(dataSourceName)) {
                each.getRoutingTableFactors().add(routingTableFactor);
                return;
            }
        }
        routingDataSources.add(new SingleRoutingDataSource(dataSourceName, routingTableFactor));
    }

    /**
     * 根据数据源名称获取数据源和逻辑表名称集合的映射关系.
     *
     * @param dataSources 待获取的数据源名称集合
     * @return 数据源和逻辑表名称集合的映射关系
     */
    public Map<String, Set<String>> getDataSourceLogicTablesMap(final Collection<String> dataSources) {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (SingleRoutingDataSource each : routingDataSources) {
            if (!dataSources.contains(each.getDataSource())) {
                continue;
            }
            Set<String> logicTables = each.getLogicTables();
            if (logicTables.isEmpty()) {
                continue;
            }
            if (result.containsKey(each.getDataSource())) {
                result.get(each.getDataSource()).addAll(logicTables);
            } else {
                result.put(each.getDataSource(), logicTables);
            }
        }
        return result;
    }

    /**
     * 获取全部数据源名称
     */
    public Collection<String> getDataSources() {
        return Lists.transform(routingDataSources, new Function<SingleRoutingDataSource, String>() {

            @Override
            public String apply(final SingleRoutingDataSource input) {
                return input.getDataSource();
            }
        });
    }

    /**
     * 根据数据源和逻辑表名称获取真实表集合组
     * 每一组的真实表集合都属于同一逻辑表
     * @param dataSource 数据源名称
     * @param logicTables 逻辑表名称集合
     * @return 真实表集合组
     */
    public List<Set<String>> getActualTableGroups(final String dataSource, final Set<String> logicTables) {
        Optional<SingleRoutingDataSource> routingDataSource = findRoutingDataSource(dataSource);
        if (!routingDataSource.isPresent()) {
            return Collections.emptyList();
        }
        return routingDataSource.get().getActualTableGroups(logicTables);
    }

    /**
     * 根据数据源和真实表名称查找路由表单元.
     *
     * @param dataSource 数据源名称
     * @param actualTable 真实表名称
     * @return 查找结果
     */
    public Optional<SingleRoutingTableFactor> findRoutingTableFactor(final String dataSource, final String actualTable) {
        Optional<SingleRoutingDataSource> routingDataSource = findRoutingDataSource(dataSource);
        if (!routingDataSource.isPresent()) {
            return Optional.absent();
        }
        return routingDataSource.get().findRoutingTableFactor(actualTable);
    }

    private Optional<SingleRoutingDataSource> findRoutingDataSource(final String dataSource) {
        for (SingleRoutingDataSource each : routingDataSources) {
            if (each.getDataSource().equals(dataSource)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }

    @Override
    public Collection<SqlExecutionUnit> getSqlExecutionUnits(SqlBuilder sqlBuilder) {
        Collection<SqlExecutionUnit> result = new ArrayList<SqlExecutionUnit>();
        for (SingleRoutingDataSource each : routingDataSources) {
            result.addAll(each.getSQLExecutionUnits(sqlBuilder));
        }
        return result;
    }
}
