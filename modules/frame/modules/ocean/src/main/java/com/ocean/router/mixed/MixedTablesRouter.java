package com.ocean.router.mixed;

import com.ocean.shard.rule.ShardRule;
import com.ocean.parser.ConditionContext;
import com.ocean.router.RoutingResult;
import com.ocean.router.binding.BindingTablesRouter;
import com.ocean.router.single.SingleRoutingResult;
import com.ocean.router.single.SingleTableRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 混合多库表路由类
 */
public class MixedTablesRouter {

    private static Logger logger = LoggerFactory.getLogger(MixedTablesRouter.class);

    private ShardRule shardRule;

    private Collection<String> logicTables;

    private ConditionContext conditionContext;

    public MixedTablesRouter()
    {

    }

    public MixedTablesRouter(ShardRule shardRule,Collection<String> logicTables,ConditionContext conditionContext)
    {
        this.shardRule=shardRule;
        this.logicTables=logicTables;
        this.conditionContext=conditionContext;
    }

    public ShardRule getShardRule() {
        return shardRule;
    }

    public void setShardRule(ShardRule shardRule) {
        this.shardRule = shardRule;
    }

    public Collection<String> getLogicTables() {
        return logicTables;
    }

    public void setLogicTables(Collection<String> logicTables) {
        this.logicTables = logicTables;
    }

    public ConditionContext getConditionContext() {
        return conditionContext;
    }

    public void setConditionContext(ConditionContext conditionContext) {
        this.conditionContext = conditionContext;
    }

    /**
     * 路由
     */
    public RoutingResult route() {
        Collection<String> bindingTables = shardRule.filterAllBindingTables(logicTables);
        Collection<String> remainingTables = new ArrayList<String>(logicTables);
        Collection<SingleRoutingResult> result = new ArrayList<SingleRoutingResult>(logicTables.size());
        if (1 < bindingTables.size()) {
            result.add(new BindingTablesRouter(shardRule, bindingTables, conditionContext).route());
            remainingTables.removeAll(bindingTables);
        }
        for (String each : remainingTables) {
            SingleRoutingResult routingResult = new SingleTableRouter(shardRule, each, conditionContext).route();
            if (null != routingResult) {
                result.add(routingResult);
            }
        }
        logger.trace("mixed tables sharding result: {}", result);
        if (result.isEmpty()) {
            return null;
        }
        if (1 == result.size()) {
            return result.iterator().next();
        }
        return new CartesianTablesRouter(result).route();
    }
}
