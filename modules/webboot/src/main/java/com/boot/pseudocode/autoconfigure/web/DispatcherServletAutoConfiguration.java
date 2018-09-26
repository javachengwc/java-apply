package com.boot.pseudocode.autoconfigure.web;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.util.Arrays;
import java.util.List;

@AutoConfigureOrder(Integer.MIN_VALUE)
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({DispatcherServlet.class})
@AutoConfigureAfter({EmbeddedServletContainerAutoConfiguration.class})
public class DispatcherServletAutoConfiguration
{
    public static final String DEFAULT_DISPATCHER_SERVLET_BEAN_NAME = "dispatcherServlet";
    public static final String DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME = "dispatcherServletRegistration";

    @Order(Integer.MAX_VALUE)
    private static class DispatcherServletRegistrationCondition extends SpringBootCondition
    {
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
        {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            ConditionOutcome outcome = checkDefaultDispatcherName(beanFactory);
            if (!outcome.isMatch()) {
                return outcome;
            }
            return checkServletRegistration(beanFactory);
        }

        private ConditionOutcome checkDefaultDispatcherName(ConfigurableListableBeanFactory beanFactory)
        {
            List servlets = Arrays.asList(beanFactory.getBeanNamesForType(DispatcherServlet.class, false, false));

            boolean containsDispatcherBean = beanFactory.containsBean("dispatcherServlet");

            if ((containsDispatcherBean) && (!servlets.contains("dispatcherServlet")))
            {
                return ConditionOutcome.noMatch(startMessage().found("non dispatcher servlet")
                        .items(new Object[] { "dispatcherServlet" }));
            }

            return ConditionOutcome.match();
        }

        private ConditionOutcome checkServletRegistration(ConfigurableListableBeanFactory beanFactory)
        {
            ConditionMessage.Builder message = startMessage();
            List registrations = Arrays.asList(beanFactory.getBeanNamesForType(ServletRegistrationBean.class, false, false));

            boolean containsDispatcherRegistrationBean = beanFactory.containsBean("dispatcherServletRegistration");

            if (registrations.isEmpty()) {
                if (containsDispatcherRegistrationBean) {
                    return ConditionOutcome.noMatch(message.found("non servlet registration bean")
                            .items(new Object[] { "dispatcherServletRegistration" }));
                }
                return ConditionOutcome.match(message.didNotFind("servlet registration bean").atAll());
            }

            if (registrations.contains("dispatcherServletRegistration"))
            {
                return ConditionOutcome.noMatch(message.found("servlet registration bean")
                        .items(new Object[] { "dispatcherServletRegistration" }));
            }

            if (containsDispatcherRegistrationBean) {
                return ConditionOutcome.noMatch(message.found("non servlet registration bean")
                        .items(new Object[] { "dispatcherServletRegistration" }));
            }

            return ConditionOutcome.match(message.found("servlet registration beans")
                    .items(ConditionMessage.Style.QUOTE, registrations)
                    .append("and none is named dispatcherServletRegistration"));
        }

        private ConditionMessage.Builder startMessage()
        {
            return ConditionMessage.forCondition("DispatcherServlet Registration", new Object[0]);
        }
    }

    @Order(Integer.MAX_VALUE)
    private static class DefaultDispatcherServletCondition extends SpringBootCondition
    {
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
        {
            ConditionMessage.Builder message = ConditionMessage.forCondition("Default DispatcherServlet", new Object[0]);

            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            List dispatchServletBeans = Arrays.asList(beanFactory.getBeanNamesForType(DispatcherServlet.class, false, false));

            if (dispatchServletBeans.contains("dispatcherServlet")) {
                return ConditionOutcome.noMatch(message.found("dispatcher servlet bean")
                        .items(new Object[] { "dispatcherServlet" }));
            }

            if (beanFactory.containsBean("dispatcherServlet")) {
                return ConditionOutcome.noMatch(message.found("non dispatcher servlet bean")
                        .items(new Object[] { "dispatcherServlet" }));
            }

            if (dispatchServletBeans.isEmpty()) {
                return ConditionOutcome.match(message.didNotFind("dispatcher servlet beans").atAll());
            }
            return ConditionOutcome.match(message
                    .found("dispatcher servlet bean", "dispatcher servlet beans")
                    .items(ConditionMessage.Style.QUOTE, dispatchServletBeans)
                    .append("and none is named dispatcherServlet"));
        }
    }

    @Configuration
    @Conditional({DispatcherServletAutoConfiguration.DispatcherServletRegistrationCondition.class})
    @ConditionalOnClass({ServletRegistration.class})
    @EnableConfigurationProperties({WebMvcProperties.class})
    @Import({DispatcherServletAutoConfiguration.DispatcherServletConfiguration.class})
    protected static class DispatcherServletRegistrationConfiguration
    {
        private final ServerProperties serverProperties;
        private final WebMvcProperties webMvcProperties;
        private final MultipartConfigElement multipartConfig;

        public DispatcherServletRegistrationConfiguration(ServerProperties serverProperties, WebMvcProperties webMvcProperties,
                                                          ObjectProvider<MultipartConfigElement> multipartConfigProvider)
        {
            this.serverProperties = serverProperties;
            this.webMvcProperties = webMvcProperties;
            this.multipartConfig = ((MultipartConfigElement)multipartConfigProvider.getIfAvailable());
        }

        @Bean(name={"dispatcherServletRegistration"})
        @ConditionalOnBean(value={DispatcherServlet.class}, name={"dispatcherServlet"})
        public ServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet) {
            ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet,
                    new String[] { this.serverProperties.getServletMapping() });
            registration.setName("dispatcherServlet");
            registration.setLoadOnStartup(this.webMvcProperties.getServlet().getLoadOnStartup());
            if (this.multipartConfig != null) {
                registration.setMultipartConfig(this.multipartConfig);
            }
            return registration;
        }
    }

    @Configuration
    @Conditional({DispatcherServletAutoConfiguration.DefaultDispatcherServletCondition.class})
    @ConditionalOnClass({ServletRegistration.class})
    @EnableConfigurationProperties({WebMvcProperties.class})
    protected static class DispatcherServletConfiguration
    {
        private final WebMvcProperties webMvcProperties;

        public DispatcherServletConfiguration(WebMvcProperties webMvcProperties)
        {
            this.webMvcProperties = webMvcProperties;
        }

        @Bean(name={"dispatcherServlet"})
        public DispatcherServlet dispatcherServlet() {
            DispatcherServlet dispatcherServlet = new DispatcherServlet();
            dispatcherServlet.setDispatchOptionsRequest(this.webMvcProperties.isDispatchOptionsRequest());
            dispatcherServlet.setDispatchTraceRequest(this.webMvcProperties.isDispatchTraceRequest());
            dispatcherServlet.setThrowExceptionIfNoHandlerFound(this.webMvcProperties.isThrowExceptionIfNoHandlerFound());
            return dispatcherServlet;
        }

        @Bean
        @ConditionalOnBean({MultipartResolver.class})
        @ConditionalOnMissingBean(name={"multipartResolver"})
        public MultipartResolver multipartResolver(MultipartResolver resolver) { return resolver;
        }
    }
}
