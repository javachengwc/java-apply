package com.pseudocode.cloud.zuul.filters.route;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.pseudocode.cloud.ribbon.support.RibbonCommandContext;
import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.exception.ZuulException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;

import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.REQUEST_ENTITY_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.RETRYABLE_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.RIBBON_ROUTING_FILTER_ORDER;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.ROUTE_TYPE;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.LOAD_BALANCER_KEY;

//ribbonRoutingFilter：是route阶段第一个执行的过滤器。
//该过滤器只对请求上下文中存在serviceId参数的请求进行处理，即只对通过serviceId配置路由规则的请求生效。
//而该过滤器的执行逻辑就是面向服务路由的核心，它通过使用Ribbon和Hystrix来向服务实例发起请求，并将服务实例的请求结果返回。
public class RibbonRoutingFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(RibbonRoutingFilter.class);

    protected ProxyRequestHelper helper;
    protected RibbonCommandFactory<?> ribbonCommandFactory;
    protected List<RibbonRequestCustomizer> requestCustomizers;
    private boolean useServlet31 = true;

    public RibbonRoutingFilter(ProxyRequestHelper helper,
                               RibbonCommandFactory<?> ribbonCommandFactory,
                               List<RibbonRequestCustomizer> requestCustomizers) {
        this.helper = helper;
        this.ribbonCommandFactory = ribbonCommandFactory;
        this.requestCustomizers = requestCustomizers;
        // To support Servlet API 3.1 we need to check if getContentLengthLong exists
        // Spring 5 minimum support is 3.0, so this stays
        try {
            HttpServletRequest.class.getMethod("getContentLengthLong");
        } catch(NoSuchMethodException e) {
            useServlet31 = false;
        }
    }

    public RibbonRoutingFilter(RibbonCommandFactory<?> ribbonCommandFactory) {
        this(new ProxyRequestHelper(), ribbonCommandFactory, null);
    }

    /* for testing */ boolean isUseServlet31() {
        return useServlet31;
    }

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return RIBBON_ROUTING_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (ctx.getRouteHost() == null && ctx.get(SERVICE_ID_KEY) != null
                && ctx.sendZuulResponse());
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        this.helper.addIgnoredHeaders();
        try {
            RibbonCommandContext commandContext = buildCommandContext(context);
            ClientHttpResponse response = forward(commandContext);
            setResponse(response);
            return response;
        }
        catch (ZuulException ex) {
            throw new ZuulRuntimeException(ex);
        }
        catch (Exception ex) {
            throw new ZuulRuntimeException(ex);
        }
    }

    protected RibbonCommandContext buildCommandContext(RequestContext context) {
        HttpServletRequest request = context.getRequest();

        MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0 && !verb.equalsIgnoreCase("GET")) {
            context.setChunkedRequestBody();
        }

        String serviceId = (String) context.get(SERVICE_ID_KEY);
        //是否可重试,
        Boolean retryable = (Boolean) context.get(RETRYABLE_KEY);
        Object loadBalancerKey = context.get(LOAD_BALANCER_KEY);

        String uri = this.helper.buildZuulRequestURI(request);

        // remove double slashes
        uri = uri.replace("//", "/");

        long contentLength = useServlet31 ? request.getContentLengthLong(): request.getContentLength();

        return new RibbonCommandContext(serviceId, verb, uri, retryable, headers, params,
                requestEntity, this.requestCustomizers, contentLength, loadBalancerKey);
    }

    protected ClientHttpResponse forward(RibbonCommandContext context) throws Exception {
        Map<String, Object> info = this.helper.debug(context.getMethod(),
                context.getUri(), context.getHeaders(), context.getParams(),
                context.getRequestEntity());

        RibbonCommand command = this.ribbonCommandFactory.create(context);
        try {
            //RibbonCommand.execute()同步阻塞执行，hystrix如果是多线程隔离，会在隔离线程池中取线程运行run()，
            //而调用程序要在execute()调用处一直堵塞着，直到run()运行完成返回结果
            ClientHttpResponse response = command.execute();
            this.helper.appendDebug(info, response.getRawStatusCode(), response.getHeaders());
            return response;
        }
        catch (HystrixRuntimeException ex) {
            return handleException(info, ex);
        }

    }

    protected ClientHttpResponse handleException(Map<String, Object> info,
                                                 HystrixRuntimeException ex) throws ZuulException {
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        Throwable cause = ex;
        String message = ex.getFailureType().toString();

        ClientException clientException = findClientException(ex);
        if (clientException == null) {
            clientException = findClientException(ex.getFallbackException());
        }

        if (clientException != null) {
            if (clientException
                    .getErrorType() == ClientException.ErrorType.SERVER_THROTTLED) {
                statusCode = HttpStatus.SERVICE_UNAVAILABLE.value();
            }
            cause = clientException;
            message = clientException.getErrorType().toString();
        }
        info.put("status", String.valueOf(statusCode));
        throw new ZuulException(cause, "Forwarding error", statusCode, message);
    }

    protected ClientException findClientException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof ClientException) {
            return (ClientException) t;
        }
        return findClientException(t.getCause());
    }

    protected InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = (InputStream) RequestContext.getCurrentContext()
                    .get(REQUEST_ENTITY_KEY);
            if (requestEntity == null) {
                requestEntity = request.getInputStream();
            }
        }
        catch (IOException ex) {
            log.error("Error during getRequestBody", ex);
        }
        return requestEntity;
    }

    protected String getVerb(HttpServletRequest request) {
        String method = request.getMethod();
        if (method == null) {
            return "GET";
        }
        return method;
    }

    protected void setResponse(ClientHttpResponse resp) throws ClientException, IOException {
        RequestContext.getCurrentContext().set("zuulResponse", resp);
        this.helper.setResponse(resp.getRawStatusCode(), resp.getBody() == null ? null : resp.getBody(), resp.getHeaders());
    }

}

