package com.ocean.router.mixed;

import com.ocean.parser.SqlBuilder;
import com.ocean.router.RoutingResult;
import com.ocean.router.SqlExecutionUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 笛卡尔积路由结果
 */
public class CartesianResult implements RoutingResult {

    private static Logger logger= LoggerFactory.getLogger(CartesianResult.class);

    private List<CartesianDataSource> routingDataSources = new ArrayList<CartesianDataSource>();

    public List<CartesianDataSource> getRoutingDataSources() {
        return routingDataSources;
    }

    public void setRoutingDataSources(List<CartesianDataSource> routingDataSources) {
        this.routingDataSources = routingDataSources;
    }

    public void merge(String dataSource, final Collection<CartesianTableReference> routingTableReferences) {
        for (CartesianTableReference each : routingTableReferences) {
            merge(dataSource, each);
        }
    }

    private void merge(String dataSource,CartesianTableReference routingTableReference) {
        for (CartesianDataSource each : routingDataSources) {
            if (each.getDataSource().equals(dataSource)) {
                each.getRoutingTableReferences().add(routingTableReference);
                return;
            }
        }
        routingDataSources.add(new CartesianDataSource(dataSource, routingTableReference));
    }

    public Collection<SqlExecutionUnit> getSqlExecutionUnits(SqlBuilder sqlBuilder) {
        Collection<SqlExecutionUnit> result = new ArrayList<SqlExecutionUnit>();
        int routCnt =(routingDataSources==null)?0:routingDataSources.size();
        logger.info("CartesianResult getSqlExecutionUnits routingDataSources count="+routCnt);
        for (CartesianDataSource each : routingDataSources) {
            result.addAll(each.getSqlExecutionUnits(sqlBuilder));
        }
        return result;
    }
}