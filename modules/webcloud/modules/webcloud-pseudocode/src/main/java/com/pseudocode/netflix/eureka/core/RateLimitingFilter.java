package com.pseudocode.netflix.eureka.core;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pseudocode.netflix.eureka.client.discovery.util.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//限流过滤器
public class RateLimitingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    private static final Set<String> DEFAULT_PRIVILEGED_CLIENTS = new HashSet<String>(
            //Arrays.asList(EurekaClientIdentity.DEFAULT_CLIENT_NAME, EurekaServerIdentity.DEFAULT_SERVER_NAME)
    );

    private static final Pattern TARGET_RE = Pattern.compile("^.*/apps(/[^/]*)?$");

    enum Target {FullFetch, DeltaFetch, Application, Other}

    private static final RateLimiter registryFetchRateLimiter = new RateLimiter(TimeUnit.SECONDS);

    private static final RateLimiter registryFullFetchRateLimiter = new RateLimiter(TimeUnit.SECONDS);

    private EurekaServerConfig serverConfig;

    public RateLimitingFilter(EurekaServerContext server) {
        this.serverConfig = server.getServerConfig();
    }

    public RateLimitingFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (serverConfig == null) {
            EurekaServerContext serverContext = (EurekaServerContext) filterConfig.getServletContext().getAttribute(EurekaServerContext.class.getName());
            serverConfig = serverContext.getServerConfig();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Target target = getTarget(request);
        if (target == Target.Other) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (isRateLimited(httpRequest, target)) {
            incrementStats(target);
            if (serverConfig.isRateLimiterEnabled()) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private static Target getTarget(ServletRequest request) {
        Target target = Target.Other;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String pathInfo = httpRequest.getRequestURI();

            if ("GET".equals(httpRequest.getMethod()) && pathInfo != null) {
                Matcher matcher = TARGET_RE.matcher(pathInfo);
                if (matcher.matches()) {
                    if (matcher.groupCount() == 0 || matcher.group(1) == null || "/".equals(matcher.group(1))) {
                        target = Target.FullFetch;
                    } else if ("/delta".equals(matcher.group(1))) {
                        target = Target.DeltaFetch;
                    } else {
                        target = Target.Application;
                    }
                }
            }
            if (target == Target.Other) {
                logger.debug("URL path {} not matched by rate limiting filter", pathInfo);
            }
        }
        return target;
    }

    private boolean isRateLimited(HttpServletRequest request, Target target) {
        if (isPrivileged(request)) {
            logger.debug("Privileged {} request", target);
            return false;
        }
        if (isOverloaded(target)) {
            logger.debug("Overloaded {} request; discarding it", target);
            return true;
        }
        logger.debug("{} request admitted", target);
        return false;
    }

    private boolean isPrivileged(HttpServletRequest request) {
        if (serverConfig.isRateLimiterThrottleStandardClients()) {
            return false;
        }
        Set<String> privilegedClients = serverConfig.getRateLimiterPrivilegedClients();
        //String clientName = request.getHeader(AbstractEurekaIdentity.AUTH_NAME_HEADER_KEY);
        String clientName = request.getHeader("DiscoveryIdentity-Name");
        return privilegedClients.contains(clientName) || DEFAULT_PRIVILEGED_CLIENTS.contains(clientName);
    }

    //是否超载
    private boolean isOverloaded(Target target) {
        int maxInWindow = serverConfig.getRateLimiterBurstSize();
        int fetchWindowSize = serverConfig.getRateLimiterRegistryFetchAverageRate();
        boolean overloaded = !registryFetchRateLimiter.acquire(maxInWindow, fetchWindowSize);

        if (target == Target.FullFetch) {
            int fullFetchWindowSize = serverConfig.getRateLimiterFullFetchAverageRate();
            overloaded |= !registryFullFetchRateLimiter.acquire(maxInWindow, fullFetchWindowSize);
        }
        return overloaded;
    }

    private void incrementStats(Target target) {
//        if (serverConfig.isRateLimiterEnabled()) {
//            EurekaMonitors.RATE_LIMITED.increment();
//            if (target == Target.FullFetch) {
//                EurekaMonitors.RATE_LIMITED_FULL_FETCH.increment();
//            }
//        } else {
//            EurekaMonitors.RATE_LIMITED_CANDIDATES.increment();
//            if (target == Target.FullFetch) {
//                EurekaMonitors.RATE_LIMITED_FULL_FETCH_CANDIDATES.increment();
//            }
//        }
    }

    @Override
    public void destroy() {
    }

    static void reset() {
//        registryFetchRateLimiter.reset();
//        registryFullFetchRateLimiter.reset();
    }
}

