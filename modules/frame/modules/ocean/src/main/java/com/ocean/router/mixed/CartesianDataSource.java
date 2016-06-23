package com.ocean.router.mixed;

import com.ocean.parser.SqlBuilder;
import com.ocean.router.SqlExecutionUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 笛卡尔积路由数据源
 */
public class CartesianDataSource {

    private String dataSource;

    private List<CartesianTableReference> routingTableReferences;

    public CartesianDataSource()
    {

    }

    public CartesianDataSource(String dataSource, CartesianTableReference routingTableReference) {
        this.dataSource = dataSource;
        routingTableReferences = new ArrayList<CartesianTableReference>(Arrays.asList(routingTableReference));
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<CartesianTableReference> getRoutingTableReferences() {
        return routingTableReferences;
    }

    public void setRoutingTableReferences(List<CartesianTableReference> routingTableReferences) {
        this.routingTableReferences = routingTableReferences;
    }

    public Collection<SqlExecutionUnit> getSqlExecutionUnits(SqlBuilder sqlBuilder) {
        Collection<SqlExecutionUnit> result = new ArrayList<SqlExecutionUnit>();
        for (CartesianTableReference each : routingTableReferences) {
            each.buildSQL(sqlBuilder);
            result.add(new SqlExecutionUnit(dataSource, sqlBuilder.toSql()));
        }
        return result;
    }
}
