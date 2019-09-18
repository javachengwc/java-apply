package com.pseudocode.cloud.zuul.filters.route;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.context.RequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.Host;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;


import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.REQUEST_ENTITY_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.ROUTE_TYPE;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.SIMPLE_HOST_ROUTING_FILTER_ORDER;

//SimpleHostRoutingFilter：是route阶段第二个执行的过滤器。
//该过滤器只对请求上下文中存在routeHost参数的请求进行处理，即只对通过url配置路由规则的请求生效。
//而该过滤器的执行逻辑就是直接向routeHost参数的物理地址发起请求，
//该请求是直接通过httpclient包实现的，而没有使用Hystrix命令进行包装，所以这类请求并没有线程隔离和断路器的保护
public class SimpleHostRoutingFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(SimpleHostRoutingFilter.class);

    private final Timer connectionManagerTimer = new Timer(
            "SimpleHostRoutingFilter.connectionManagerTimer", true);

    private boolean sslHostnameValidationEnabled;
    private boolean forceOriginalQueryStringEncoding;

    private ProxyRequestHelper helper;
    private Host hostProperties;
    private ApacheHttpClientConnectionManagerFactory connectionManagerFactory;
    private ApacheHttpClientFactory httpClientFactory;
    private HttpClientConnectionManager connectionManager;
    private CloseableHttpClient httpClient;
    private boolean customHttpClient = false;
    private boolean useServlet31 = true;

    @EventListener
    public void onPropertyChange(EnvironmentChangeEvent event) {
        if(!customHttpClient) {
            boolean createNewClient = false;

            for (String key : event.getKeys()) {
                if (key.startsWith("zuul.host.")) {
                    createNewClient = true;
                    break;
                }
            }

            if (createNewClient) {
                try {
                    SimpleHostRoutingFilter.this.httpClient.close();
                } catch (IOException ex) {
                    log.error("error closing client", ex);
                }
                SimpleHostRoutingFilter.this.httpClient = newClient();
            }
        }
    }

    public SimpleHostRoutingFilter(ProxyRequestHelper helper, ZuulProperties properties,
                                   ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
                                   ApacheHttpClientFactory httpClientFactory) {
        this.helper = helper;
        this.hostProperties = properties.getHost();
        this.sslHostnameValidationEnabled = properties.isSslHostnameValidationEnabled();
        this.forceOriginalQueryStringEncoding = properties
                .isForceOriginalQueryStringEncoding();
        this.connectionManagerFactory = connectionManagerFactory;
        this.httpClientFactory = httpClientFactory;
        checkServletVersion();
    }

    public SimpleHostRoutingFilter(ProxyRequestHelper helper, ZuulProperties properties,
                                   CloseableHttpClient httpClient) {
        this.helper = helper;
        this.hostProperties = properties.getHost();
        this.sslHostnameValidationEnabled = properties.isSslHostnameValidationEnabled();
        this.forceOriginalQueryStringEncoding = properties
                .isForceOriginalQueryStringEncoding();
        this.httpClient = httpClient;
        this.customHttpClient = true;
        checkServletVersion();
    }

    @PostConstruct
    private void initialize() {
        if(!customHttpClient) {
            this.connectionManager = connectionManagerFactory.newConnectionManager(
                    !this.sslHostnameValidationEnabled,
                    this.hostProperties.getMaxTotalConnections(),
                    this.hostProperties.getMaxPerRouteConnections(),
                    this.hostProperties.getTimeToLive(), this.hostProperties.getTimeUnit(),
                    null);
            this.httpClient = newClient();
            this.connectionManagerTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (SimpleHostRoutingFilter.this.connectionManager == null) {
                        return;
                    }
                    SimpleHostRoutingFilter.this.connectionManager.closeExpiredConnections();
                }
            }, 30000, 5000);
        }
    }

    @PreDestroy
    public void stop() {
        this.connectionManagerTimer.cancel();
    }

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SIMPLE_HOST_ROUTING_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getRouteHost() != null
                && RequestContext.getCurrentContext().sendZuulResponse();
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        if (getContentLength(request) < 0) {
            context.setChunkedRequestBody();
        }

        String uri = this.helper.buildZuulRequestURI(request);
        this.helper.addIgnoredHeaders();

        try {
            CloseableHttpResponse response = forward(this.httpClient, verb, uri, request,
                    headers, params, requestEntity);
            setResponse(response);
        }
        catch (Exception ex) {
            throw new ZuulRuntimeException(ex);
        }
        return null;
    }

    protected void checkServletVersion() {
        // To support Servlet API 3.1 we need to check if getContentLengthLong exists
        // Spring 5 minimum support is 3.0, so this stays
        try {
            HttpServletRequest.class.getMethod("getContentLengthLong");
            useServlet31 = true;
        } catch(NoSuchMethodException e) {
            useServlet31 = false;
        }
    }

    protected void setUseServlet31(boolean useServlet31) {
        this.useServlet31 = useServlet31;
    }

    protected HttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    protected CloseableHttpClient newClient() {
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(this.hostProperties.getConnectionRequestTimeoutMillis())
                .setSocketTimeout(this.hostProperties.getSocketTimeoutMillis())
                .setConnectTimeout(this.hostProperties.getConnectTimeoutMillis())
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        return httpClientFactory.createBuilder().
                setDefaultRequestConfig(requestConfig).
                setConnectionManager(this.connectionManager).disableRedirectHandling().build();
    }

    private CloseableHttpResponse forward(CloseableHttpClient httpclient, String verb,
                                          String uri, HttpServletRequest request, MultiValueMap<String, String> headers,
                                          MultiValueMap<String, String> params, InputStream requestEntity)
            throws Exception {
        Map<String, Object> info = this.helper.debug(verb, uri, headers, params,
                requestEntity);
        URL host = RequestContext.getCurrentContext().getRouteHost();
        HttpHost httpHost = getHttpHost(host);
        uri = StringUtils.cleanPath((host.getPath() + uri).replaceAll("/{2,}", "/"));
        long contentLength = getContentLength(request);

        ContentType contentType = null;

        if (request.getContentType() != null) {
            contentType = ContentType.parse(request.getContentType());
        }

        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength,
                contentType);

        HttpRequest httpRequest = buildHttpRequest(verb, uri, entity, headers, params,
                request);
        try {
            log.debug(httpHost.getHostName() + " " + httpHost.getPort() + " "
                    + httpHost.getSchemeName());
            CloseableHttpResponse zuulResponse = forwardRequest(httpclient, httpHost,
                    httpRequest);
            this.helper.appendDebug(info, zuulResponse.getStatusLine().getStatusCode(),
                    revertHeaders(zuulResponse.getAllHeaders()));
            return zuulResponse;
        }
        finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            // httpclient.getConnectionManager().shutdown();
        }
    }

    protected HttpRequest buildHttpRequest(String verb, String uri,
                                           InputStreamEntity entity, MultiValueMap<String, String> headers,
                                           MultiValueMap<String, String> params, HttpServletRequest request) {
        HttpRequest httpRequest;
        String uriWithQueryString = uri + (this.forceOriginalQueryStringEncoding
                ? getEncodedQueryString(request) : this.helper.getQueryString(params));

        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uriWithQueryString);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uriWithQueryString);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uriWithQueryString);
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            case "DELETE":
                BasicHttpEntityEnclosingRequest entityRequest = new BasicHttpEntityEnclosingRequest(
                        verb, uriWithQueryString);
                httpRequest = entityRequest;
                entityRequest.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uriWithQueryString);
                log.debug(uriWithQueryString);
        }

        httpRequest.setHeaders(convertHeaders(headers));
        return httpRequest;
    }

    private String getEncodedQueryString(HttpServletRequest request) {
        String query = request.getQueryString();
        return (query != null) ? "?" + query : "";
    }

    private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<String>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new BasicHeader[0]);
    }

    private CloseableHttpResponse forwardRequest(CloseableHttpClient httpclient,
                                                 HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }

    private HttpHost getHttpHost(URL host) {
        HttpHost httpHost = new HttpHost(host.getHost(), host.getPort(),
                host.getProtocol());
        return httpHost;
    }

    protected InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = (InputStream) RequestContext.getCurrentContext().get(REQUEST_ENTITY_KEY);
            if (requestEntity == null) {
                requestEntity = request.getInputStream();
            }
        }
        catch (IOException ex) {
            log.error("error during getRequestBody", ex);
        }
        return requestEntity;
    }

    private String getVerb(HttpServletRequest request) {
        String sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    private void setResponse(HttpResponse response) throws IOException {
        RequestContext.getCurrentContext().set("zuulResponse", response);
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    protected void addIgnoredHeaders(String... names) {
        this.helper.addIgnoredHeaders(names);
    }

    boolean isSslHostnameValidationEnabled() {
        return this.sslHostnameValidationEnabled;
    }

    // Get the header value as a long in order to more correctly proxy very large requests
    protected long getContentLength(HttpServletRequest request) {
        if(useServlet31){
            return request.getContentLengthLong();
        }
        String contentLengthHeader = request.getHeader(HttpHeaders.CONTENT_LENGTH);
        if (contentLengthHeader != null) {
            try {
                return Long.parseLong(contentLengthHeader);
            }
            catch (NumberFormatException e){}
        }
        return request.getContentLength();
    }
}
