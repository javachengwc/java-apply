package com.ocean.parser;

import com.ocean.merger.MergeContext;

import java.util.ArrayList;
import java.util.List;

/**
 * sql解析结果
 */
public class SqlParsedResult {

    private RouteContext routeContext = new RouteContext();

    private List<ConditionContext> conditionContexts = new ArrayList<ConditionContext>();

    private MergeContext mergeContext = new MergeContext();

    public RouteContext getRouteContext() {
        return routeContext;
    }

    public void setRouteContext(RouteContext routeContext) {
        this.routeContext = routeContext;
    }

    public List<ConditionContext> getConditionContexts() {
        return conditionContexts;
    }

    public void setConditionContexts(List<ConditionContext> conditionContexts) {
        this.conditionContexts = conditionContexts;
    }

    public MergeContext getMergeContext() {
        return mergeContext;
    }

    public void setMergeContext(MergeContext mergeContext) {
        this.mergeContext = mergeContext;
    }
}
