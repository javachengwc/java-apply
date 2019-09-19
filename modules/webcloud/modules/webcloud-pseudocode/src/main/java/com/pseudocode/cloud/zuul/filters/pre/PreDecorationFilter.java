package com.pseudocode.cloud.zuul.filters.pre;


import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.pseudocode.cloud.zuul.filters.Route;
import com.pseudocode.cloud.zuul.filters.RouteLocator;
import com.pseudocode.cloud.zuul.filters.ZuulProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.FORWARD_LOCATION_PREFIX;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.FORWARD_TO_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.HTTPS_PORT;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.HTTPS_SCHEME;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.HTTP_PORT;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.HTTP_SCHEME;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.PRE_TYPE;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.PROXY_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.RETRYABLE_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.SERVICE_HEADER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.SERVICE_ID_HEADER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.X_FORWARDED_FOR_HEADER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.X_FORWARDED_HOST_HEADER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.X_FORWARDED_PORT_HEADER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.X_FORWARDED_PREFIX_HEADER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.X_FORWARDED_PROTO_HEADER;

//PreDecorationFilter是pre最重要的过滤器
//该过滤器为当前请求做一些预处理，比如说，进行路由规则的匹配，在请求上下文中设置该请求的基本信息以及将路由匹配结果等一些设置信息等，
//这些信息将是后续过滤器进行处理的重要依据，可以通过RequestContext.getCurrentContext()来访问这些信息。
//另外，还对HTTP头请求进行处理，其中包含了一些头域，比如X-Forwarded-Host,X-Forwarded-Port。
//对于这些头域是通过zuul.addProxyHeaders参数进行控制的，而这个参数默认值是true，
//所以zuul在请求跳转时默认会为请求增加X-Forwarded-*头域，包括X-Forwarded-Host,X-Forwarded-Port，X-Forwarded-For，X-Forwarded-Prefix,X-Forwarded-Proto。
public class PreDecorationFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(PreDecorationFilter.class);

    @Deprecated
    public static final int FILTER_ORDER = PRE_DECORATION_FILTER_ORDER;

    private RouteLocator routeLocator;

    private String dispatcherServletPath;

    private ZuulProperties properties;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private ProxyRequestHelper proxyRequestHelper;

    public PreDecorationFilter(RouteLocator routeLocator, String dispatcherServletPath, ZuulProperties properties,
                               ProxyRequestHelper proxyRequestHelper) {
        this.routeLocator = routeLocator;
        this.properties = properties;
        this.urlPathHelper.setRemoveSemicolonContent(properties.isRemoveSemicolonContent());
        this.dispatcherServletPath = dispatcherServletPath;
        this.proxyRequestHelper = proxyRequestHelper;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return !ctx.containsKey(FORWARD_TO_KEY) // a filter has already forwarded
                && !ctx.containsKey(SERVICE_ID_KEY); // a filter has already determined serviceId
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        final String requestURI = this.urlPathHelper.getPathWithinApplication(ctx.getRequest());
        //路由规则的匹配Route
        Route route = this.routeLocator.getMatchingRoute(requestURI);
        if (route != null) {
            String location = route.getLocation();
            if (location != null) {
                ctx.put(REQUEST_URI_KEY, route.getPath());
                ctx.put(PROXY_KEY, route.getId());
                //是否添加敏感头信息
                if (!route.isCustomSensitiveHeaders()) {
                    this.proxyRequestHelper.addIgnoredHeaders(this.properties.getSensitiveHeaders().toArray(new String[0]));
                }
                else {
                    this.proxyRequestHelper.addIgnoredHeaders(route.getSensitiveHeaders().toArray(new String[0]));
                }

                if (route.getRetryable() != null) {
                    ctx.put(RETRYABLE_KEY, route.getRetryable());
                }

                if (location.startsWith(HTTP_SCHEME+":") || location.startsWith(HTTPS_SCHEME+":")) {
                    ctx.setRouteHost(getUrl(location));
                    ctx.addOriginResponseHeader(SERVICE_HEADER, location);
                }
                else if (location.startsWith(FORWARD_LOCATION_PREFIX)) {
                    ctx.set(FORWARD_TO_KEY,
                            StringUtils.cleanPath(location.substring(FORWARD_LOCATION_PREFIX.length()) + route.getPath()));
                    ctx.setRouteHost(null);
                    return null;
                }
                else {
                    // set serviceId for use in filters.route.RibbonRequest
                    //微服务
                    ctx.set(SERVICE_ID_KEY, location);
                    ctx.setRouteHost(null);
                    ctx.addOriginResponseHeader(SERVICE_ID_HEADER, location);
                }
                //是否添加代理头
                if (this.properties.isAddProxyHeaders()) {
                    addProxyHeaders(ctx, route);
                    String xforwardedfor = ctx.getRequest().getHeader(X_FORWARDED_FOR_HEADER);
                    String remoteAddr = ctx.getRequest().getRemoteAddr();
                    if (xforwardedfor == null) {
                        xforwardedfor = remoteAddr;
                    }
                    else if (!xforwardedfor.contains(remoteAddr)) { // Prevent duplicates
                        xforwardedfor += ", " + remoteAddr;
                    }
                    ctx.addZuulRequestHeader(X_FORWARDED_FOR_HEADER, xforwardedfor);
                }
                //添加host头
                if (this.properties.isAddHostHeader()) {
                    ctx.addZuulRequestHeader(HttpHeaders.HOST, toHostHeader(ctx.getRequest()));
                }
            }
        }
        else {
            //Route为null，进行相应的fallback处理
            log.warn("No route found for uri: " + requestURI);
            String fallBackUri = requestURI;
            String fallbackPrefix = this.dispatcherServletPath; // default fallback
            // servlet is
            // DispatcherServlet

            if (RequestUtils.isZuulServletRequest()) {
                // remove the Zuul servletPath from the requestUri
                log.debug("zuulServletPath=" + this.properties.getServletPath());
                fallBackUri = fallBackUri.replaceFirst(this.properties.getServletPath(), "");
                log.debug("Replaced Zuul servlet path:" + fallBackUri);
            }
            else {
                // remove the DispatcherServlet servletPath from the requestUri
                log.debug("dispatcherServletPath=" + this.dispatcherServletPath);
                fallBackUri = fallBackUri.replaceFirst(this.dispatcherServletPath, "");
                log.debug("Replaced DispatcherServlet servlet path:" + fallBackUri);
            }
            if (!fallBackUri.startsWith("/")) {
                fallBackUri = "/" + fallBackUri;
            }
            String forwardURI = fallbackPrefix + fallBackUri;
            forwardURI = forwardURI.replaceAll("//", "/");
            ctx.set(FORWARD_TO_KEY, forwardURI);
        }
        return null;
    }

    private void addProxyHeaders(RequestContext ctx, Route route) {
        HttpServletRequest request = ctx.getRequest();
        String host = toHostHeader(request);
        String port = String.valueOf(request.getServerPort());
        String proto = request.getScheme();
        if (hasHeader(request, X_FORWARDED_HOST_HEADER)) {
            host = request.getHeader(X_FORWARDED_HOST_HEADER) + "," + host;
        }
        if (!hasHeader(request, X_FORWARDED_PORT_HEADER)) {
            if (hasHeader(request, X_FORWARDED_PROTO_HEADER)) {
                StringBuilder builder = new StringBuilder();
                for (String previous : StringUtils.commaDelimitedListToStringArray(request.getHeader(X_FORWARDED_PROTO_HEADER))) {
                    if (builder.length()>0) {
                        builder.append(",");
                    }
                    builder.append(HTTPS_SCHEME.equals(previous) ? HTTPS_PORT : HTTP_PORT);
                }
                builder.append(",").append(port);
                port = builder.toString();
            }
        } else {
            port = request.getHeader(X_FORWARDED_PORT_HEADER) + "," + port;
        }
        if (hasHeader(request, X_FORWARDED_PROTO_HEADER)) {
            proto = request.getHeader(X_FORWARDED_PROTO_HEADER) + "," + proto;
        }
        ctx.addZuulRequestHeader(X_FORWARDED_HOST_HEADER, host);
        ctx.addZuulRequestHeader(X_FORWARDED_PORT_HEADER, port);
        ctx.addZuulRequestHeader(X_FORWARDED_PROTO_HEADER, proto);
        addProxyPrefix(ctx, route);
    }

    private boolean hasHeader(HttpServletRequest request, String name) {
        return StringUtils.hasLength(request.getHeader(name));
    }

    private void addProxyPrefix(RequestContext ctx, Route route) {
        String forwardedPrefix = ctx.getRequest().getHeader(X_FORWARDED_PREFIX_HEADER);
        String contextPath = ctx.getRequest().getContextPath();
        String prefix = StringUtils.hasLength(forwardedPrefix) ? forwardedPrefix
                : (StringUtils.hasLength(contextPath) ? contextPath : null);
        if (StringUtils.hasText(route.getPrefix())) {
            StringBuilder newPrefixBuilder = new StringBuilder();
            if (prefix != null) {
                if (prefix.endsWith("/") && route.getPrefix().startsWith("/")) {
                    newPrefixBuilder.append(prefix, 0, prefix.length() - 1);
                }
                else {
                    newPrefixBuilder.append(prefix);
                }
            }
            newPrefixBuilder.append(route.getPrefix());
            prefix = newPrefixBuilder.toString();
        }
        if (prefix != null) {
            ctx.addZuulRequestHeader(X_FORWARDED_PREFIX_HEADER, prefix);
        }
    }

    private String toHostHeader(HttpServletRequest request) {
        int port = request.getServerPort();
        if ((port == HTTP_PORT && HTTP_SCHEME.equals(request.getScheme()))
                || (port == HTTPS_PORT && HTTPS_SCHEME.equals(request.getScheme()))) {
            return request.getServerName();
        }
        else {
            return request.getServerName() + ":" + port;
        }
    }

    private URL getUrl(String target) {
        try {
            return new URL(target);
        }
        catch (MalformedURLException ex) {
            throw new IllegalStateException("Target URL is malformed", ex);
        }
    }
}
