package com.pseudocode.cloud.eurekaserver;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import com.pseudocode.netflix.eureka.client.appinfo.ApplicationInfoManager;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClient;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClientConfig;
import com.pseudocode.netflix.eureka.core.EurekaServerConfig;
import com.pseudocode.netflix.eureka.core.EurekaServerContext;
import com.pseudocode.netflix.eureka.core.cluster.PeerEurekaNodes;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistry;
import com.pseudocode.netflix.eureka.core.resources.ServerCodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Import(EurekaServerInitializerConfiguration.class)
@ConditionalOnBean(EurekaServerMarkerConfiguration.Marker.class)
@EnableConfigurationProperties({ EurekaDashboardProperties.class, InstanceRegistryProperties.class })
@PropertySource("classpath:/eureka/server.properties")
public class EurekaServerAutoConfiguration extends WebMvcConfigurerAdapter {

    private static final String[] EUREKA_PACKAGES = new String[] { "com.netflix.discovery", "com.netflix.eureka" };

    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    @Autowired
    private EurekaServerConfig eurekaServerConfig;

    @Autowired
    private EurekaClientConfig eurekaClientConfig;

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private InstanceRegistryProperties instanceRegistryProperties;

    public static final CloudJacksonJson JACKSON_JSON = new CloudJacksonJson();

    @Bean
    public HasFeatures eurekaServerFeature() {
        return HasFeatures.namedFeature("Eureka Server", EurekaServerAutoConfiguration.class);
    }

    @Configuration
    protected static class EurekaServerConfigBeanConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public EurekaServerConfig eurekaServerConfig(EurekaClientConfig clientConfig) {
            EurekaServerConfigBean server = new EurekaServerConfigBean();
            if (clientConfig.shouldRegisterWithEureka()) {
                // Set a sensible default if we are supposed to replicate
                server.setRegistrySyncRetries(5);
            }
            return server;
        }
    }

    @Bean
    @ConditionalOnProperty(prefix = "eureka.dashboard", name = "enabled", matchIfMissing = true)
    public EurekaController eurekaController() {
        return new EurekaController(this.applicationInfoManager);
    }

    static {
        CodecWrappers.registerWrapper(JACKSON_JSON);
        EurekaJacksonCodec.setInstance(JACKSON_JSON.getCodec());
    }

    @Bean
    public ServerCodecs serverCodecs() {
        return new CloudServerCodecs(this.eurekaServerConfig);
    }

    private static CodecWrapper getFullJson(EurekaServerConfig serverConfig) {
        CodecWrapper codec = CodecWrappers.getCodec(serverConfig.getJsonCodecName());
        return codec == null ? CodecWrappers.getCodec(JACKSON_JSON.codecName()) : codec;
    }

    private static CodecWrapper getFullXml(EurekaServerConfig serverConfig) {
        CodecWrapper codec = CodecWrappers.getCodec(serverConfig.getXmlCodecName());
        return codec == null ? CodecWrappers.getCodec(CodecWrappers.XStreamXml.class)
                : codec;
    }

    class CloudServerCodecs extends DefaultServerCodecs {

        public CloudServerCodecs(EurekaServerConfig serverConfig) {
            super(getFullJson(serverConfig),
                    CodecWrappers.getCodec(CodecWrappers.JacksonJsonMini.class),
                    getFullXml(serverConfig),
                    CodecWrappers.getCodec(CodecWrappers.JacksonXmlMini.class));
        }
    }

    @Bean
    public PeerAwareInstanceRegistry peerAwareInstanceRegistry(ServerCodecs serverCodecs) {
        this.eurekaClient.getApplications(); // force initialization
        return new InstanceRegistry(this.eurekaServerConfig, this.eurekaClientConfig,
                serverCodecs, this.eurekaClient,
                this.instanceRegistryProperties.getExpectedNumberOfRenewsPerMin(),
                this.instanceRegistryProperties.getDefaultOpenForTrafficCount());
    }

    @Bean
    @ConditionalOnMissingBean
    public PeerEurekaNodes peerEurekaNodes(PeerAwareInstanceRegistry registry,
                                           ServerCodecs serverCodecs) {
        return new RefreshablePeerEurekaNodes(registry, this.eurekaServerConfig,
                this.eurekaClientConfig, serverCodecs, this.applicationInfoManager);
    }

    static class RefreshablePeerEurekaNodes extends PeerEurekaNodes implements ApplicationListener<EnvironmentChangeEvent> {

        public RefreshablePeerEurekaNodes(
                final PeerAwareInstanceRegistry registry,
                final EurekaServerConfig serverConfig,
                final EurekaClientConfig clientConfig,
                final ServerCodecs serverCodecs,
                final ApplicationInfoManager applicationInfoManager) {
                super(registry, serverConfig, clientConfig, serverCodecs, applicationInfoManager);
        }

        @Override
        public void onApplicationEvent(final EnvironmentChangeEvent event) {
            if (shouldUpdate(event.getKeys())) {
                updatePeerEurekaNodes(resolvePeerUrls());
            }
        }

        protected boolean shouldUpdate(final Set<String> changedKeys) {
            assert changedKeys != null;

            // if eureka.client.use-dns-for-fetching-service-urls is true, then
            // service-url will not be fetched from environment.
            if (clientConfig.shouldUseDnsForFetchingServiceUrls()) {
                return false;
            }

            if (changedKeys.contains("eureka.client.region")) {
                return true;
            }

            for (final String key : changedKeys) {
                // property keys are not expected to be null.
                if (key.startsWith("eureka.client.service-url.") || key.startsWith("eureka.client.availability-zones.")) {
                    return true;
                }
            }

            return false;
        }
    }

    @Bean
    public EurekaServerContext eurekaServerContext(ServerCodecs serverCodecs,
                                                   PeerAwareInstanceRegistry registry, PeerEurekaNodes peerEurekaNodes) {
        return new DefaultEurekaServerContext(this.eurekaServerConfig, serverCodecs,
                registry, peerEurekaNodes, this.applicationInfoManager);
    }

    @Bean
    public EurekaServerBootstrap eurekaServerBootstrap(PeerAwareInstanceRegistry registry,
                                                       EurekaServerContext serverContext) {
        return new EurekaServerBootstrap(this.applicationInfoManager,
                this.eurekaClientConfig, this.eurekaServerConfig, registry,
                serverContext);
    }

    @Bean
    public FilterRegistrationBean jerseyFilterRegistration(
            javax.ws.rs.core.Application eurekaJerseyApp) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new ServletContainer(eurekaJerseyApp));
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        bean.setUrlPatterns(
                Collections.singletonList(EurekaConstants.DEFAULT_PREFIX + "/*"));

        return bean;
    }

    @Bean
    public javax.ws.rs.core.Application jerseyApplication(Environment environment, ResourceLoader resourceLoader) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false, environment);

        // Filter to include only classes that have a particular annotation.
        //
        provider.addIncludeFilter(new AnnotationTypeFilter(Path.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Provider.class));

        // Find classes in Eureka packages (or subpackages)
        //
        Set<Class<?>> classes = new HashSet<>();
        for (String basePackage : EUREKA_PACKAGES) {
            Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
            for (BeanDefinition bd : beans) {
                Class<?> cls = ClassUtils.resolveClassName(bd.getBeanClassName(),
                        resourceLoader.getClassLoader());
                classes.add(cls);
            }
        }

        // Construct the Jersey ResourceConfig
        //
        Map<String, Object> propsAndFeatures = new HashMap<>();
        propsAndFeatures.put(
                // Skip static content used by the webapp
                ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX,
                EurekaConstants.DEFAULT_PREFIX + "/(fonts|images|css|js)/.*");

        DefaultResourceConfig rc = new DefaultResourceConfig(classes);
        rc.setPropertiesAndFeatures(propsAndFeatures);

        return rc;
    }

    @Bean
    public FilterRegistrationBean traceFilterRegistration(
            @Qualifier("httpTraceFilter") Filter filter) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(filter);
        bean.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return bean;
    }
}

