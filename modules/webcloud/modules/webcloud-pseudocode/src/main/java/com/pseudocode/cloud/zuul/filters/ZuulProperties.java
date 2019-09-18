package com.pseudocode.cloud.zuul.filters;

import com.pseudocode.netflix.hystrix.core.HystrixCommandProperties;
import com.pseudocode.netflix.hystrix.core.HystrixCommandProperties.ExecutionIsolationStrategy;
import static com.pseudocode.netflix.hystrix.core.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//zuul网关配置
@ConfigurationProperties("zuul")
public class ZuulProperties {

    public static final List<String> SECURITY_HEADERS = Arrays.asList("Pragma",
            "Cache-Control", "X-Frame-Options", "X-Content-Type-Options",
            "X-XSS-Protection", "Expires");

    private String prefix = "";

    private boolean stripPrefix = true;

    private Boolean retryable = false;

    private Map<String, ZuulRoute> routes = new LinkedHashMap<>();

    private boolean addProxyHeaders = true;

    private boolean addHostHeader = false;

    private Set<String> ignoredServices = new LinkedHashSet<>();

    private Set<String> ignoredPatterns = new LinkedHashSet<>();

    private Set<String> ignoredHeaders = new LinkedHashSet<>();

    private boolean ignoreSecurityHeaders = true;

    private boolean forceOriginalQueryStringEncoding = false;

    private String servletPath = "/zuul";

    private boolean ignoreLocalService = true;

    private Host host = new Host();

    private boolean traceRequestBody = true;

    private boolean removeSemicolonContent = true;

    private Set<String> sensitiveHeaders = new LinkedHashSet<>(
            Arrays.asList("Cookie", "Set-Cookie", "Authorization"));

    private boolean sslHostnameValidationEnabled =true;

    //zuul网关默认的隔离策略是信号量 与hystrix的默认隔离策略线程池 不一样，可配置修改
    private ExecutionIsolationStrategy ribbonIsolationStrategy = SEMAPHORE;

    private HystrixSemaphore semaphore = new HystrixSemaphore();

    private HystrixThreadPool threadPool = new HystrixThreadPool();

    private boolean setContentLength = false;

    private boolean includeDebugHeader = false;

    private int initialStreamBufferSize = 8192;

    public Set<String> getIgnoredHeaders() {
        Set<String> ignoredHeaders = new LinkedHashSet<>(this.ignoredHeaders);
        if (ClassUtils.isPresent(
                "org.springframework.security.config.annotation.web.WebSecurityConfigurer",
                null) && Collections.disjoint(ignoredHeaders, SECURITY_HEADERS) && ignoreSecurityHeaders) {
            // Allow Spring Security in the gateway to control these headers
            ignoredHeaders.addAll(SECURITY_HEADERS);
        }
        return ignoredHeaders;
    }

    public void setIgnoredHeaders(Set<String> ignoredHeaders) {
        this.ignoredHeaders.addAll(ignoredHeaders);
    }

    @PostConstruct
    public void init() {
        for (Entry<String, ZuulRoute> entry : this.routes.entrySet()) {
            ZuulRoute value = entry.getValue();
            if (!StringUtils.hasText(value.getLocation())) {
                value.serviceId = entry.getKey();
            }
            if (!StringUtils.hasText(value.getId())) {
                value.id = entry.getKey();
            }
            if (!StringUtils.hasText(value.getPath())) {
                value.path = "/" + entry.getKey() + "/**";
            }
        }
    }

    public static class ZuulRoute {

        private String id;

        private String path;

        private String serviceId;

        private String url;

        private boolean stripPrefix = true;

        private Boolean retryable;

        private Set<String> sensitiveHeaders = new LinkedHashSet<>();

        private boolean customSensitiveHeaders = false;

        public ZuulRoute() {}

        public ZuulRoute(String id, String path, String serviceId, String url,
                         boolean stripPrefix, Boolean retryable, Set<String> sensitiveHeaders) {
            this.id = id;
            this.path = path;
            this.serviceId = serviceId;
            this.url = url;
            this.stripPrefix = stripPrefix;
            this.retryable = retryable;
            this.sensitiveHeaders = sensitiveHeaders;
            this.customSensitiveHeaders = sensitiveHeaders != null;
        }

        public ZuulRoute(String text) {
            String location = null;
            String path = text;
            if (text.contains("=")) {
                String[] values = StringUtils
                        .trimArrayElements(StringUtils.split(text, "="));
                location = values[1];
                path = values[0];
            }
            this.id = extractId(path);
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            setLocation(location);
            this.path = path;
        }

        public ZuulRoute(String path, String location) {
            this.id = extractId(path);
            this.path = path;
            setLocation(location);
        }

        public String getLocation() {
            if (StringUtils.hasText(this.url)) {
                return this.url;
            }
            return this.serviceId;
        }

        public void setLocation(String location) {
            if (location != null
                    && (location.startsWith("http:") || location.startsWith("https:"))) {
                this.url = location;
            }
            else {
                this.serviceId = location;
            }
        }

        private String extractId(String path) {
            path = path.startsWith("/") ? path.substring(1) : path;
            path = path.replace("/*", "").replace("*", "");
            return path;
        }

        public Route getRoute(String prefix) {
            return new Route(this.id, this.path, getLocation(), prefix, this.retryable,
                    isCustomSensitiveHeaders() ? this.sensitiveHeaders : null,
                    this.stripPrefix);
        }

        public void setSensitiveHeaders(Set<String> headers) {
            this.customSensitiveHeaders = true;
            this.sensitiveHeaders = new LinkedHashSet<>(headers);
        }

        public boolean isCustomSensitiveHeaders() {
            return this.customSensitiveHeaders;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public Boolean getRetryable() {
            return retryable;
        }

        public void setRetryable(Boolean retryable) {
            this.retryable = retryable;
        }

        public Set<String> getSensitiveHeaders() {
            return sensitiveHeaders;
        }

        public void setCustomSensitiveHeaders(boolean customSensitiveHeaders) {
            this.customSensitiveHeaders = customSensitiveHeaders;
        }
    }

    public static class Host {

        private int maxTotalConnections = 200;

        private int maxPerRouteConnections = 20;

        private int socketTimeoutMillis = 10000;

        private int connectTimeoutMillis = 2000;

        private int connectionRequestTimeoutMillis = -1;

        private long timeToLive = -1;

        private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        public Host() {
        }

        public Host(int maxTotalConnections, int maxPerRouteConnections,
                    int socketTimeoutMillis, int connectTimeoutMillis, long timeToLive,
                    TimeUnit timeUnit) {
            this.maxTotalConnections = maxTotalConnections;
            this.maxPerRouteConnections = maxPerRouteConnections;
            this.socketTimeoutMillis = socketTimeoutMillis;
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.timeToLive = timeToLive;
            this.timeUnit = timeUnit;
        }

        public int getMaxTotalConnections() {
            return maxTotalConnections;
        }

        public void setMaxTotalConnections(int maxTotalConnections) {
            this.maxTotalConnections = maxTotalConnections;
        }

        public int getMaxPerRouteConnections() {
            return maxPerRouteConnections;
        }

        public void setMaxPerRouteConnections(int maxPerRouteConnections) {
            this.maxPerRouteConnections = maxPerRouteConnections;
        }

        public int getSocketTimeoutMillis() {
            return socketTimeoutMillis;
        }

        public void setSocketTimeoutMillis(int socketTimeoutMillis) {
            this.socketTimeoutMillis = socketTimeoutMillis;
        }

        public int getConnectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        public void setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
        }

        public int getConnectionRequestTimeoutMillis() {
            return connectionRequestTimeoutMillis;
        }

        public void setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
            this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
        }

        public long getTimeToLive() {
            return timeToLive;
        }

        public void setTimeToLive(long timeToLive) {
            this.timeToLive = timeToLive;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }

    }

    public static class HystrixSemaphore {

        private int maxSemaphores = 100;

        public HystrixSemaphore() {}

        public HystrixSemaphore(int maxSemaphores) {
            this.maxSemaphores = maxSemaphores;
        }

        public int getMaxSemaphores() {
            return maxSemaphores;
        }

        public void setMaxSemaphores(int maxSemaphores) {
            this.maxSemaphores = maxSemaphores;
        }

    }

    public static class HystrixThreadPool {

        private boolean useSeparateThreadPools = false;

        private String threadPoolKeyPrefix = "";

        public boolean isUseSeparateThreadPools() {
            return useSeparateThreadPools;
        }

        public void setUseSeparateThreadPools(boolean useSeparateThreadPools) {
            this.useSeparateThreadPools = useSeparateThreadPools;
        }

        public String getThreadPoolKeyPrefix() {
            return threadPoolKeyPrefix;
        }

        public void setThreadPoolKeyPrefix(String threadPoolKeyPrefix) {
            this.threadPoolKeyPrefix = threadPoolKeyPrefix;
        }
    }

    public String getServletPattern() {
        String path = this.servletPath;
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains("*")) {
            path = path.endsWith("/") ? (path + "*") : (path + "/*");
        }
        return path;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(boolean stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public Map<String, ZuulRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, ZuulRoute> routes) {
        this.routes = routes;
    }

    public boolean isAddProxyHeaders() {
        return addProxyHeaders;
    }

    public void setAddProxyHeaders(boolean addProxyHeaders) {
        this.addProxyHeaders = addProxyHeaders;
    }

    public boolean isAddHostHeader() {
        return addHostHeader;
    }

    public void setAddHostHeader(boolean addHostHeader) {
        this.addHostHeader = addHostHeader;
    }

    public Set<String> getIgnoredServices() {
        return ignoredServices;
    }

    public void setIgnoredServices(Set<String> ignoredServices) {
        this.ignoredServices = ignoredServices;
    }

    public Set<String> getIgnoredPatterns() {
        return ignoredPatterns;
    }

    public void setIgnoredPatterns(Set<String> ignoredPatterns) {
        this.ignoredPatterns = ignoredPatterns;
    }

    public boolean isIgnoreSecurityHeaders() {
        return ignoreSecurityHeaders;
    }

    public void setIgnoreSecurityHeaders(boolean ignoreSecurityHeaders) {
        this.ignoreSecurityHeaders = ignoreSecurityHeaders;
    }

    public boolean isForceOriginalQueryStringEncoding() {
        return forceOriginalQueryStringEncoding;
    }

    public void setForceOriginalQueryStringEncoding(
            boolean forceOriginalQueryStringEncoding) {
        this.forceOriginalQueryStringEncoding = forceOriginalQueryStringEncoding;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public boolean isIgnoreLocalService() {
        return ignoreLocalService;
    }

    public void setIgnoreLocalService(boolean ignoreLocalService) {
        this.ignoreLocalService = ignoreLocalService;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public boolean isTraceRequestBody() {
        return traceRequestBody;
    }

    public void setTraceRequestBody(boolean traceRequestBody) {
        this.traceRequestBody = traceRequestBody;
    }

    public boolean isRemoveSemicolonContent() {
        return removeSemicolonContent;
    }

    public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
        this.removeSemicolonContent = removeSemicolonContent;
    }

    public Set<String> getSensitiveHeaders() {
        return sensitiveHeaders;
    }

    public void setSensitiveHeaders(Set<String> sensitiveHeaders) {
        this.sensitiveHeaders = sensitiveHeaders;
    }

    public boolean isSslHostnameValidationEnabled() {
        return sslHostnameValidationEnabled;
    }

    public void setSslHostnameValidationEnabled(boolean sslHostnameValidationEnabled) {
        this.sslHostnameValidationEnabled = sslHostnameValidationEnabled;
    }

    public ExecutionIsolationStrategy getRibbonIsolationStrategy() {
        return ribbonIsolationStrategy;
    }

    public void setRibbonIsolationStrategy(ExecutionIsolationStrategy ribbonIsolationStrategy) {
        this.ribbonIsolationStrategy = ribbonIsolationStrategy;
    }

    public HystrixSemaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(HystrixSemaphore semaphore) {
        this.semaphore = semaphore;
    }

    public HystrixThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(HystrixThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public boolean isSetContentLength() {
        return setContentLength;
    }

    public void setSetContentLength(boolean setContentLength) {
        this.setContentLength = setContentLength;
    }

    public boolean isIncludeDebugHeader() {
        return includeDebugHeader;
    }

    public void setIncludeDebugHeader(boolean includeDebugHeader) {
        this.includeDebugHeader = includeDebugHeader;
    }

    public int getInitialStreamBufferSize() {
        return initialStreamBufferSize;
    }

    public void setInitialStreamBufferSize(int initialStreamBufferSize) {
        this.initialStreamBufferSize = initialStreamBufferSize;
    }

}

