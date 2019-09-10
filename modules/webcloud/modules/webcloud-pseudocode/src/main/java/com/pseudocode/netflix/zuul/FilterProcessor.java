package com.pseudocode.netflix.zuul;

import com.pseudocode.netflix.zuul.context.Debug;
import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.exception.ZuulException;
import com.netflix.servo.monitor.DynamicCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FilterProcessor {

    static FilterProcessor INSTANCE = new FilterProcessor();
    protected static final Logger logger = LoggerFactory.getLogger(FilterProcessor.class);

    private FilterUsageNotifier usageNotifier;


    public FilterProcessor() {
        usageNotifier = new BasicFilterUsageNotifier();
    }

    public static FilterProcessor getInstance() {
        return INSTANCE;
    }

    public static void setProcessor(FilterProcessor processor) {
        INSTANCE = processor;
    }

    public void setFilterUsageNotifier(FilterUsageNotifier notifier) {
        this.usageNotifier = notifier;
    }

    public void postRoute() throws ZuulException {
        try {
            runFilters("post");
        } catch (ZuulException e) {
            throw e;
        } catch (Throwable e) {
            throw new ZuulException(e, 500, "UNCAUGHT_EXCEPTION_IN_POST_FILTER_" + e.getClass().getName());
        }
    }

    public void error() {
        try {
            runFilters("error");
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void route() throws ZuulException {
        try {
            runFilters("route");
        } catch (ZuulException e) {
            throw e;
        } catch (Throwable e) {
            throw new ZuulException(e, 500, "UNCAUGHT_EXCEPTION_IN_ROUTE_FILTER_" + e.getClass().getName());
        }
    }

    public void preRoute() throws ZuulException {
        try {
            runFilters("pre");
        } catch (ZuulException e) {
            throw e;
        } catch (Throwable e) {
            throw new ZuulException(e, 500, "UNCAUGHT_EXCEPTION_IN_PRE_FILTER_" + e.getClass().getName());
        }
    }

    public Object runFilters(String sType) throws Throwable {
        if (RequestContext.getCurrentContext().debugRouting()) {
            Debug.addRoutingDebug("Invoking {" + sType + "} type filters");
        }
        boolean bResult = false;
        List<ZuulFilter> list = FilterLoader.getInstance().getFiltersByType(sType);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ZuulFilter zuulFilter = list.get(i);
                Object result = processZuulFilter(zuulFilter);
                if (result != null && result instanceof Boolean) {
                    bResult |= ((Boolean) result);
                }
            }
        }
        return bResult;
    }

    public Object processZuulFilter(ZuulFilter filter) throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();
        boolean bDebug = ctx.debugRouting();
        final String metricPrefix = "zuul.filter-";
        long execTime = 0;
        String filterName = "";
        try {
            long ltime = System.currentTimeMillis();
            filterName = filter.getClass().getSimpleName();

            RequestContext copy = null;
            Object o = null;
            Throwable t = null;

            if (bDebug) {
                Debug.addRoutingDebug("Filter " + filter.filterType() + " " + filter.filterOrder() + " " + filterName);
                copy = ctx.copy();
            }

            ZuulFilterResult result = filter.runFilter();
            ExecutionStatus s = result.getStatus();
            execTime = System.currentTimeMillis() - ltime;

            switch (s) {
                case FAILED:
                    t = result.getException();
                    ctx.addFilterExecutionSummary(filterName, ExecutionStatus.FAILED.name(), execTime);
                    break;
                case SUCCESS:
                    o = result.getResult();
                    ctx.addFilterExecutionSummary(filterName, ExecutionStatus.SUCCESS.name(), execTime);
                    if (bDebug) {
                        Debug.addRoutingDebug("Filter {" + filterName + " TYPE:" + filter.filterType() + " ORDER:" + filter.filterOrder() + "} Execution time = " + execTime + "ms");
                        Debug.compareContextState(filterName, copy);
                    }
                    break;
                default:
                    break;
            }

            if (t != null) throw t;

            usageNotifier.notify(filter, s);
            return o;

        } catch (Throwable e) {
            if (bDebug) {
                Debug.addRoutingDebug("Running Filter failed " + filterName + " type:" + filter.filterType() + " order:" + filter.filterOrder() + " " + e.getMessage());
            }
            usageNotifier.notify(filter, ExecutionStatus.FAILED);
            if (e instanceof ZuulException) {
                throw (ZuulException) e;
            } else {
                ZuulException ex = new ZuulException(e, "Filter threw Exception", 500, filter.filterType() + ":" + filterName);
                ctx.addFilterExecutionSummary(filterName, ExecutionStatus.FAILED.name(), execTime);
                throw ex;
            }
        }
    }

    public static class BasicFilterUsageNotifier implements FilterUsageNotifier {
        private static final String METRIC_PREFIX = "zuul.filter-";

        @Override
        public void notify(ZuulFilter filter, ExecutionStatus status) {
            DynamicCounter.increment(METRIC_PREFIX + filter.getClass().getSimpleName(), "status", status.name(), "filtertype", filter.filterType());
        }
    }
}