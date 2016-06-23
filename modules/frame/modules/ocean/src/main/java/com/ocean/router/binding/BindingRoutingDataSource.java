package com.ocean.router.binding;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ocean.shard.rule.BindingTableRule;
import com.ocean.router.single.SingleRoutingDataSource;
import com.ocean.router.single.SingleRoutingTableFactor;

/**
 * Binding表路由数据源
 */
public class BindingRoutingDataSource extends SingleRoutingDataSource {

     public BindingRoutingDataSource(SingleRoutingDataSource routingDataSource) {
        super(routingDataSource.getDataSource());
        getRoutingTableFactors().addAll(Lists.transform(routingDataSource.getRoutingTableFactors(), new Function<SingleRoutingTableFactor, BindingRoutingTableFactor>() {

            @Override
            public BindingRoutingTableFactor apply(final SingleRoutingTableFactor input) {
                return new BindingRoutingTableFactor(input.getLogicTable(), input.getActualTable());
            }
        }));
    }

    public void bind(final BindingTableRule bindingTableRule, final String bindingLogicTable) {
        for (SingleRoutingTableFactor each : getRoutingTableFactors()) {
            ((BindingRoutingTableFactor) each).getBindingRoutingTableFactors().add(
                    new BindingRoutingTableFactor(bindingLogicTable, bindingTableRule.getBindingActualTable(getDataSource(), bindingLogicTable, each.getActualTable())));
        }
    }
}
