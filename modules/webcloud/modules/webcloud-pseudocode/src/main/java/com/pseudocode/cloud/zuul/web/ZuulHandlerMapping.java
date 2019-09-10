package com.pseudocode.cloud.zuul.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.pseudocode.cloud.zuul.filters.RefreshableRouteLocator;
import com.pseudocode.cloud.zuul.filters.Route;
import com.pseudocode.cloud.zuul.filters.RouteLocator;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

import com.pseudocode.netflix.zuul.context.RequestContext;

public class ZuulHandlerMapping extends AbstractUrlHandlerMapping {

    private final RouteLocator routeLocator;

    private final ZuulController zuul;

    private ErrorController errorController;

    private PathMatcher pathMatcher = new AntPathMatcher();

    private volatile boolean dirty = true;

    public ZuulHandlerMapping(RouteLocator routeLocator, ZuulController zuul) {
        this.routeLocator = routeLocator;
        this.zuul = zuul;
        setOrder(-200);
    }

    @Override
    protected HandlerExecutionChain getCorsHandlerExecutionChain(HttpServletRequest request,
                                                                 HandlerExecutionChain chain, CorsConfiguration config) {
        if (config == null) {
            // Allow CORS requests to go to the backend
            return chain;
        }
        return super.getCorsHandlerExecutionChain(request, chain, config);
    }

    public void setErrorController(ErrorController errorController) {
        this.errorController = errorController;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        if (this.routeLocator instanceof RefreshableRouteLocator) {
            ((RefreshableRouteLocator) this.routeLocator).refresh();
        }
    }

    @Override
    protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
        if (this.errorController != null && urlPath.equals(this.errorController.getErrorPath())) {
            return null;
        }
        if (isIgnoredPath(urlPath, this.routeLocator.getIgnoredPaths())) return null;
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.containsKey("forward.to")) {
            return null;
        }
        if (this.dirty) {
            synchronized (this) {
                if (this.dirty) {
                    registerHandlers();
                    this.dirty = false;
                }
            }
        }
        return super.lookupHandler(urlPath, request);
    }

    private boolean isIgnoredPath(String urlPath, Collection<String> ignored) {
        if (ignored != null) {
            for (String ignoredPath : ignored) {
                if (this.pathMatcher.match(ignoredPath, urlPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void registerHandlers() {
        Collection<Route> routes = this.routeLocator.getRoutes();
        if (routes.isEmpty()) {
            this.logger.warn("No routes found from RouteLocator");
        }
        else {
            for (Route route : routes) {
                registerHandler(route.getFullPath(), this.zuul);
            }
        }
    }

}
