package com.ocean.router.binding;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.ocean.shard.rule.BindingTableRule;
import com.ocean.shard.rule.ShardRule;
import com.ocean.parser.ConditionContext;
import com.ocean.router.single.SingleTableRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Binding库表路由类
 */
public class BindingTablesRouter {

    private static Logger logger = LoggerFactory.getLogger(BindingTablesRouter.class);

    private ShardRule shardRule;

    private Collection<String> logicTables;

    private ConditionContext conditionContext;

    private BindingTableRule bindingTableRule;

    public BindingTablesRouter(ShardRule shardRule, final Collection<String> logicTables, final ConditionContext conditionContext) {
        this.shardRule = shardRule;
        this.logicTables = logicTables;
        this.conditionContext = conditionContext;
        Optional<BindingTableRule> optionalBindingTableRule = shardRule.getBindingTableRule(logicTables.iterator().next());
        Preconditions.checkState(optionalBindingTableRule.isPresent());
        bindingTableRule = optionalBindingTableRule.get();
    }

    /**
     * 路由
     */
    public BindingRoutingResult route() {
        BindingRoutingResult result = null;
        for (final String each : logicTables) {
            if (null == result) {
                result = new BindingRoutingResult(new SingleTableRouter(shardRule, each, conditionContext).route());
            } else {
                result.bind(bindingTableRule, each);
            }
        }
        logger.trace("binding table sharding result: {}", result);
        return result;
    }
}
