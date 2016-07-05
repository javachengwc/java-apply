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

    public String toString()
    {
        StringBuffer buf=new StringBuffer();
        buf.append("conditionContexts:");
        int cdnCnt =(conditionContexts==null)?0:conditionContexts.size();
        buf.append("count-->"+cdnCnt);
        if(cdnCnt>0)
        {
            buf.append("\r\n");
            for(ConditionContext cdnContext:conditionContexts)
            {
                buf.append(cdnContext.toString()).append("\r\n");
            }
        }
        return buf.toString();
    }
}
