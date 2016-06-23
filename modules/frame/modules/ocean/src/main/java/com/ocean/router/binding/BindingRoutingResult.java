package com.ocean.router.binding;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ocean.shard.rule.BindingTableRule;
import com.ocean.router.single.SingleRoutingDataSource;
import com.ocean.router.single.SingleRoutingResult;

/**
 * Binding表路由结果
 */
public class BindingRoutingResult extends SingleRoutingResult {

    public BindingRoutingResult(SingleRoutingResult singleRoutingResult) {
        getRoutingDataSources().addAll(Lists.transform(singleRoutingResult.getRoutingDataSources(), new Function<SingleRoutingDataSource, BindingRoutingDataSource>() {

            @Override
            public BindingRoutingDataSource apply(SingleRoutingDataSource input) {
                return new BindingRoutingDataSource(input);
            }
        }));
    }

    public void bind(BindingTableRule bindingTableRule, String bindingLogicTable) {
        for (SingleRoutingDataSource each : getRoutingDataSources()) {
            ((BindingRoutingDataSource) each).bind(bindingTableRule, bindingLogicTable);
        }
    }
}
