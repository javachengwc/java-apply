package com.pseudocode.netflix.zuul.context;

import com.pseudocode.netflix.commons.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.pseudocode.netflix.zuul.constants.ZuulHeaders;
import com.pseudocode.netflix.zuul.util.DeepCopy;

public class RequestContext extends ConcurrentHashMap<String, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(RequestContext.class);

    protected static Class<? extends RequestContext> contextClass = RequestContext.class;

    private static RequestContext testContext = null;

    protected static final ThreadLocal<? extends RequestContext> threadLocal = new ThreadLocal<RequestContext>() {
        @Override
        protected RequestContext initialValue() {
            try {
                return contextClass.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    };


    public RequestContext() {
        super();
    }

    public static void setContextClass(Class<? extends RequestContext> clazz) {
        contextClass = clazz;
    }

    public static void testSetCurrentContext(RequestContext context) {
        testContext = context;
    }

    public static RequestContext getCurrentContext() {
        if (testContext != null) return testContext;

        RequestContext context = threadLocal.get();
        return context;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultResponse) {
        Boolean b = (Boolean) get(key);
        if (b != null) {
            return b.booleanValue();
        }
        return defaultResponse;
    }

    public void set(String key) {
        put(key, Boolean.TRUE);
    }

    public void set(String key, Object value) {
        if (value != null) put(key, value);
        else remove(key);
    }

    public boolean getZuulEngineRan() {
        return getBoolean("zuulEngineRan");
    }

    public void setZuulEngineRan() {
        put("zuulEngineRan", true);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) get("request");
    }

    public void setRequest(HttpServletRequest request) {
        put("request", request);
    }

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) get("response");
    }

    public void setResponse(HttpServletResponse response) {
        set("response", response);
    }

    public Throwable getThrowable() {
        return (Throwable) get("throwable");

    }

    public void setThrowable(Throwable th) {
        put("throwable", th);

    }

    public void setDebugRouting(boolean bDebug) {
        set("debugRouting", bDebug);
    }

    public boolean debugRouting() {
        return getBoolean("debugRouting");
    }

    public void setDebugRequestHeadersOnly(boolean bHeadersOnly) {
        set("debugRequestHeadersOnly", bHeadersOnly);

    }

    public boolean debugRequestHeadersOnly() {
        return getBoolean("debugRequestHeadersOnly");
    }

    public void setDebugRequest(boolean bDebug) {
        set("debugRequest", bDebug);
    }

    public boolean debugRequest() {
        return getBoolean("debugRequest");
    }

    public void removeRouteHost() {
        remove("routeHost");
    }

    public void setRouteHost(URL routeHost) {
        set("routeHost", routeHost);
    }

    public URL getRouteHost() {
        return (URL) get("routeHost");
    }

    public void addFilterExecutionSummary(String name, String status, long time) {
        StringBuilder sb = getFilterExecutionSummary();
        if (sb.length() > 0) sb.append(", ");
        sb.append(name).append('[').append(status).append(']').append('[').append(time).append("ms]");
    }

    public StringBuilder getFilterExecutionSummary() {
        if (get("executedFilters") == null) {
            putIfAbsent("executedFilters", new StringBuilder());
        }
        return (StringBuilder) get("executedFilters");
    }

    public void setResponseBody(String body) {
        set("responseBody", body);
    }

    public String getResponseBody() {
        return (String) get("responseBody");
    }

    public void setResponseDataStream(InputStream responseDataStream) {
        set("responseDataStream", responseDataStream);
    }

    public void setResponseGZipped(boolean gzipped) {
        put("responseGZipped", gzipped);
    }

    public boolean getResponseGZipped() {
        return getBoolean("responseGZipped", true);
    }

    public InputStream getResponseDataStream() {
        return (InputStream) get("responseDataStream");
    }

    public boolean sendZuulResponse() {
        return getBoolean("sendZuulResponse", true);
    }

    public void setSendZuulResponse(boolean bSend) {
        set("sendZuulResponse", Boolean.valueOf(bSend));
    }

    public int getResponseStatusCode() {
        return get("responseStatusCode") != null ? (Integer) get("responseStatusCode") : 500;
    }

    public void setResponseStatusCode(int nStatusCode) {
        getResponse().setStatus(nStatusCode);
        set("responseStatusCode", nStatusCode);
    }

    public void addZuulRequestHeader(String name, String value) {
        getZuulRequestHeaders().put(name.toLowerCase(), value);
    }

    public Map<String, String> getZuulRequestHeaders() {
        if (get("zuulRequestHeaders") == null) {
            HashMap<String, String> zuulRequestHeaders = new HashMap<String, String>();
            putIfAbsent("zuulRequestHeaders", zuulRequestHeaders);
        }
        return (Map<String, String>) get("zuulRequestHeaders");
    }

    public void addZuulResponseHeader(String name, String value) {
        getZuulResponseHeaders().add(new Pair<String, String>(name, value));
    }

    public List<Pair<String, String>> getZuulResponseHeaders() {
        if (get("zuulResponseHeaders") == null) {
            List<Pair<String, String>> zuulRequestHeaders = new ArrayList<Pair<String, String>>();
            putIfAbsent("zuulResponseHeaders", zuulRequestHeaders);
        }
        return (List<Pair<String, String>>) get("zuulResponseHeaders");
    }

    public List<Pair<String, String>> getOriginResponseHeaders() {
        if (get("originResponseHeaders") == null) {
            List<Pair<String, String>> originResponseHeaders = new ArrayList<Pair<String, String>>();
            putIfAbsent("originResponseHeaders", originResponseHeaders);
        }
        return (List<Pair<String, String>>) get("originResponseHeaders");
    }

    public void addOriginResponseHeader(String name, String value) {
        getOriginResponseHeaders().add(new Pair<String, String>(name, value));
    }

    public Long getOriginContentLength() {
        return (Long) get("originContentLength");
    }

    public void setOriginContentLength(Long v) {
        set("originContentLength", v);
    }

    public void setOriginContentLength(String v) {
        try {
            final Long i = Long.valueOf(v);
            set("originContentLength", i);
        } catch (NumberFormatException e) {
            LOG.warn("error parsing origin content length", e);
        }
    }

    public boolean isChunkedRequestBody() {
        final Object v = get("chunkedRequestBody");
        return (v != null) ? (Boolean) v : false;
    }

    public void setChunkedRequestBody() {
        this.set("chunkedRequestBody", Boolean.TRUE);
    }

    public boolean isGzipRequested() {
        final String requestEncoding = this.getRequest().getHeader(ZuulHeaders.ACCEPT_ENCODING);
        return requestEncoding != null && requestEncoding.toLowerCase().contains("gzip");
    }

    public void unset() {
        threadLocal.remove();
    }

    public RequestContext copy() {
        RequestContext copy = new RequestContext();
        Iterator<String> it = keySet().iterator();
        String key = it.next();
        while (key != null) {
            Object orig = get(key);
            try {
                Object copyValue = DeepCopy.copy(orig);
                if (copyValue != null) {
                    copy.set(key, copyValue);
                } else {
                    copy.set(key, orig);
                }
            } catch (NotSerializableException e) {
                copy.set(key, orig);
            }
            if (it.hasNext()) {
                key = it.next();
            } else {
                key = null;
            }
        }
        return copy;
    }

    public Map<String, List<String>> getRequestQueryParams() {
        return (Map<String, List<String>>) get("requestQueryParams");
    }

    public void setRequestQueryParams(Map<String, List<String>> qp) {
        put("requestQueryParams", qp);
    }

}
