package com.ocean.router.mixed;

import com.ocean.parser.SqlBuilder;
import com.ocean.router.single.SingleRoutingTableFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 笛卡尔积表路由
 */
public class CartesianTableReference {

    private static Logger logger = LoggerFactory.getLogger(CartesianTableReference.class);

    private List<SingleRoutingTableFactor> routingTableFactors;

    public CartesianTableReference(List<SingleRoutingTableFactor> routingTableFactors) {
        this.routingTableFactors = new ArrayList<SingleRoutingTableFactor>(routingTableFactors);
    }

    public List<SingleRoutingTableFactor> getRoutingTableFactors() {
        return routingTableFactors;
    }

    public void setRoutingTableFactors(List<SingleRoutingTableFactor> routingTableFactors) {
        this.routingTableFactors = routingTableFactors;
    }

    public void buildSQL(SqlBuilder builder) {
        for (SingleRoutingTableFactor each : routingTableFactors) {
            each.buildSQL(builder);
            logger.info("CartesianTableReference buildSQL routingTableFactors ,builder="+builder.toString());
        }
    }
}

