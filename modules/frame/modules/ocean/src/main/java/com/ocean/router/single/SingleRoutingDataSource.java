package com.ocean.router.single;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.ocean.parser.SqlBuilder;
import com.ocean.router.SqlExecutionUnit;

import java.util.*;

/**
 *  单表路由数据源
 */
public class SingleRoutingDataSource {

    //数据库
    private String dataSource;

    //路由的数据表
    private List<SingleRoutingTableFactor> routingTableFactors = new ArrayList<SingleRoutingTableFactor>();

    public SingleRoutingDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public SingleRoutingDataSource(String dataSource, SingleRoutingTableFactor routingTableFactor) {
        this(dataSource);
        routingTableFactors.add(routingTableFactor);
    }

    public String getDataSource() {
        return dataSource;
    }

    public List<SingleRoutingTableFactor> getRoutingTableFactors() {
        return routingTableFactors;
    }

    public void setRoutingTableFactors(List<SingleRoutingTableFactor> routingTableFactors) {
        this.routingTableFactors = routingTableFactors;
    }

    public Collection<SqlExecutionUnit> getSQLExecutionUnits(final SqlBuilder sqlBuilder) {
        Collection<SqlExecutionUnit> result = new ArrayList<SqlExecutionUnit>();
        for (SingleRoutingTableFactor each : routingTableFactors) {
            each.buildSQL(sqlBuilder);
            result.add(new SqlExecutionUnit(dataSource, sqlBuilder.toSql()));
        }
        return result;
    }

    public Set<String> getLogicTables() {
        Set<String> result = new HashSet<String>(routingTableFactors.size());
        result.addAll(Lists.transform(routingTableFactors, new Function<SingleRoutingTableFactor, String>() {

            @Override
            public String apply(final SingleRoutingTableFactor input) {
                return input.getLogicTable();
            }
        }));
        return result;
    }

    //根据逻辑表名批量获取实际表名
    public List<Set<String>> getActualTableGroups(Set<String> logicTables) {
        List<Set<String>> result = new ArrayList<Set<String>>();
        for (String logicTable : logicTables) {
            Set<String> actualTables = getActualTables(logicTable);
            if (!actualTables.isEmpty()) {
                result.add(actualTables);
            }
        }
        return result;
    }

    //根据逻辑表名获取实际表名
    private Set<String> getActualTables(String logicTable) {
        Set<String> result = new HashSet<String>();
        for (SingleRoutingTableFactor each : routingTableFactors) {
            if (each.getLogicTable().equals(logicTable)) {
                result.add(each.getActualTable());
            }
        }
        return result;
    }

    public Optional<SingleRoutingTableFactor> findRoutingTableFactor(final String actualTable) {
        for (SingleRoutingTableFactor each : routingTableFactors) {
            if (each.getActualTable().equals(actualTable)) {
                return Optional.of(each);
            }
        }
        return Optional.absent();
    }
}
