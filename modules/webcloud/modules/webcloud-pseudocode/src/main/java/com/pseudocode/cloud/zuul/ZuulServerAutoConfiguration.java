package com.pseudocode.cloud.zuul;


import java.util.Collection;
import java.util.Map;

import com.pseudocode.cloud.ribbon.SpringClientFactory;
import com.pseudocode.cloud.zuul.filters.ZuulProperties;
import com.pseudocode.cloud.zuul.filters.post.SendErrorFilter;
import com.pseudocode.cloud.zuul.filters.post.SendResponseFilter;
import com.pseudocode.cloud.zuul.filters.pre.DebugFilter;
import com.pseudocode.cloud.zuul.filters.pre.ServletDetectionFilter;
import com.pseudocode.cloud.zuul.web.ZuulController;
import com.pseudocode.cloud.zuul.web.ZuulHandlerMapping;
import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.http.ZuulServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;


@Configuration
@EnableConfigurationProperties({ ZuulProperties.class })
@ConditionalOnClass(ZuulServlet.class)
@ConditionalOnBean(ZuulServerMarkerConfiguration.Marker.class)
// Make sure to get the ServerProperties from the same place as a normal web app would
// FIXME @Import(ServerPropertiesAutoConfiguration.class)
public class ZuulServerAutoConfiguration {

    @Autowired
    protected ZuulProperties zuulProperties;

    @Autowired
    protected ServerProperties server;

    @Autowired(required = false)
    private ErrorController errorController;

    @Bean
    public HasFeatures zuulFeature() {
        return HasFeatures.namedFeature("Zuul (Simple)", ZuulServerAutoConfiguration.class);
    }

    @Bean
    @Primary
    public CompositeRouteLocator primaryRouteLocator(
            Collection<RouteLocator> routeLocators) {
        return new CompositeRouteLocator(routeLocators);
    }

    @Bean
    @ConditionalOnMissingBean(SimpleRouteLocator.class)
    public SimpleRouteLocator simpleRouteLocator() {
        return new SimpleRouteLocator(this.server.getServlet().getServletPrefix(),
                this.zuulProperties);
    }

    @Bean
    public ZuulController zuulController() {
        return new ZuulController();
    }

    @Bean
    public ZuulHandlerMapping zuulHandlerMapping(RouteLocator routes) {
        ZuulHandlerMapping mapping = new ZuulHandlerMapping(routes, zuulController());
        mapping.setErrorController(this.errorController);
        return mapping;
    }

    @Bean
    public ApplicationListener<ApplicationEvent> zuulRefreshRoutesListener() {
        return new ZuulRefreshListener();
    }

    @Bean
    @ConditionalOnMissingBean(name = "zuulServlet")
    public ServletRegistrationBean zuulServlet() {
        ServletRegistrationBean<ZuulServlet> servlet = new ServletRegistrationBean<>(new ZuulServlet(),
                this.zuulProperties.getServletPattern());
        // The whole point of exposing this servlet is to provide a route that doesn't
        // buffer requests.
        servlet.addInitParameter("buffer-requests", "false");
        return servlet;
    }

    // pre filters

    @Bean
    public ServletDetectionFilter servletDetectionFilter() {
        return new ServletDetectionFilter();
    }

    @Bean
    public FormBodyWrapperFilter formBodyWrapperFilter() {
        return new FormBodyWrapperFilter();
    }

    @Bean
    public DebugFilter debugFilter() {
        return new DebugFilter();
    }

    @Bean
    public Servlet30WrapperFilter servlet30WrapperFilter() {
        return new Servlet30WrapperFilter();
    }

    // post filters

    @Bean
    public SendResponseFilter sendResponseFilter(ZuulProperties properties) {
        return new SendResponseFilter(zuulProperties);
    }

    @Bean
    public SendErrorFilter sendErrorFilter() {
        return new SendErrorFilter();
    }

    @Bean
    public SendForwardFilter sendForwardFilter() {
        return new SendForwardFilter();
    }

    @Bean
    @ConditionalOnProperty(value = "zuul.ribbon.eager-load.enabled")
    public ZuulRouteApplicationContextInitializer zuulRoutesApplicationContextInitiazer(SpringClientFactory springClientFactory) {
        return new ZuulRouteApplicationContextInitializer(springClientFactory, zuulProperties);
    }

    @Configuration
    protected static class ZuulFilterConfiguration {

        @Autowired
        private Map<String, ZuulFilter> filters;

        @Bean
        public ZuulFilterInitializer zuulFilterInitializer(
                CounterFactory counterFactory, TracerFactory tracerFactory) {
            FilterLoader filterLoader = FilterLoader.getInstance();
            FilterRegistry filterRegistry = FilterRegistry.instance();
            return new ZuulFilterInitializer(this.filters, counterFactory, tracerFactory, filterLoader, filterRegistry);
        }

    }

    @Configuration
    @ConditionalOnClass(MeterRegistry.class)
    protected static class ZuulCounterFactoryConfiguration {

        @Bean
        @ConditionalOnBean(MeterRegistry.class)
        public CounterFactory counterFactory(MeterRegistry meterRegistry) {
            return new DefaultCounterFactory(meterRegistry);
        }
    }

    @Configuration
    protected static class ZuulMetricsConfiguration {

        @Bean
        @ConditionalOnMissingBean(CounterFactory.class)
        public CounterFactory counterFactory() {
            return new EmptyCounterFactory();
        }

        @ConditionalOnMissingBean(TracerFactory.class)
        @Bean
        public TracerFactory tracerFactory() {
            return new EmptyTracerFactory();
        }

    }

    private static class ZuulRefreshListener
            implements ApplicationListener<ApplicationEvent> {

        @Autowired
        private ZuulHandlerMapping zuulHandlerMapping;

        private HeartbeatMonitor heartbeatMonitor = new HeartbeatMonitor();

        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            if (event instanceof ContextRefreshedEvent
                    || event instanceof RefreshScopeRefreshedEvent
                    || event instanceof RoutesRefreshedEvent
                    || event instanceof InstanceRegisteredEvent) {
                reset();
            }
            else if (event instanceof ParentHeartbeatEvent) {
                ParentHeartbeatEvent e = (ParentHeartbeatEvent) event;
                resetIfNeeded(e.getValue());
            }
            else if (event instanceof HeartbeatEvent) {
                HeartbeatEvent e = (HeartbeatEvent) event;
                resetIfNeeded(e.getValue());
            }
        }

        private void resetIfNeeded(Object value) {
            if (this.heartbeatMonitor.update(value)) {
                reset();
            }
        }

        private void reset() {
            this.zuulHandlerMapping.setDirty(true);
        }
    }
}
