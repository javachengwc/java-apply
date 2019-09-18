package com.pseudocode.netflix.zuul;

import com.pseudocode.netflix.zuul.monitoring.Tracer;
import com.pseudocode.netflix.zuul.monitoring.TracerFactory;

public abstract class ZuulFilter implements IZuulFilter, Comparable<ZuulFilter> {

    //private final DynamicBooleanProperty filterDisabled = DynamicPropertyFactory.getInstance().getBooleanProperty(this.disablePropertyName(), false);

    public ZuulFilter() {
    }

    //过滤类型
    //pre：在请求被路由之前调用
    //routing：在路由请求时候被调用
    //post：在routing和error过滤器之后被调用
    //error：处理请求时发生错误时被调用
    public abstract String filterType();

    //执行顺序,数字越小，优先级越高
    public abstract int filterOrder();

    public boolean isStaticFilter() {
        return true;
    }

    public String disablePropertyName() {
        return "zuul." + this.getClass().getSimpleName() + "." + this.filterType() + ".disable";
    }

    public boolean isFilterDisabled() {
        //return this.filterDisabled.get();
        return true;
    }

    public ZuulFilterResult runFilter() {

        ZuulFilterResult zr = new ZuulFilterResult();
        if (!this.isFilterDisabled()) {
            if (this.shouldFilter()) {
                Tracer t = TracerFactory.instance().startMicroTracer("ZUUL::" + this.getClass().getSimpleName());

                try {
                    Object res = this.run();
                    zr = new ZuulFilterResult(res, ExecutionStatus.SUCCESS);
                } catch (Throwable var7) {
                    t.setName("ZUUL::" + this.getClass().getSimpleName() + " failed");
                    zr = new ZuulFilterResult(ExecutionStatus.FAILED);
                    zr.setException(var7);
                } finally {
                    t.stopAndLog();
                }
            } else {
                zr = new ZuulFilterResult(ExecutionStatus.SKIPPED);
            }
        }

        return zr;
    }

    public int compareTo(ZuulFilter filter) {
        return Integer.compare(this.filterOrder(), filter.filterOrder());
    }

}
