package com.ocean.router.mixed;

import com.ocean.parser.SqlBuilder;
import com.ocean.router.SqlExecutionUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 笛卡尔积路由数据源
 */
public class CartesianDataSource {

    private static Logger logger = LoggerFactory.getLogger(CartesianDataSource.class);

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
        //产生各对应数据库真正的sql语句
        for (CartesianTableReference each : routingTableReferences) {
            each.buildSQL(sqlBuilder);
            String sql = sqlBuilder.toSql();
            result.add(new SqlExecutionUnit(dataSource, sql));
            logger.info("----------------------------------------------");
            logger.info("CartesianDataSource getSqlExecutionUnits result add one,dataSource="+dataSource+",sql="+sql);
            logger.info("----------------------------------------------");
        }
        logger.info("CartesianDataSource getSqlExecutionUnits run end ");
        return result;
    }
}
