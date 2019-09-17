package com.pseudocode.cloud.openfeign.core;

import com.pseudocode.cloud.openfeign.core.support.SpringMvcContract;
import com.pseudocode.netflix.feign.core.Contract;
import com.pseudocode.netflix.feign.core.Feign;
import com.pseudocode.netflix.feign.core.Retryer;
import com.pseudocode.netflix.feign.core.codec.Decoder;
import com.pseudocode.netflix.feign.core.codec.Encoder;
import com.pseudocode.netflix.feign.hystrix.HystrixFeign;
import com.pseudocode.netflix.hystrix.core.HystrixCommand;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FeignClientsConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired(required = false)
    private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<AnnotatedParameterProcessor>();

    @Autowired(required = false)
    private List<FeignFormatterRegistrar> feignFormatterRegistrars = new ArrayList<FeignFormatterRegistrar>();

    @Autowired(required = false)
    private Logger logger;

    //解码器,默认使用HttpMessageConverters实现
    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder() {
        return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
    }

    //编码器,默认使用HttpMessageConverters实现
    @Bean
    @ConditionalOnMissingBean
    public Encoder feignEncoder() {
        return new SpringEncoder(this.messageConverters);
    }

    //feign接口方法元数据解析器,,提供springmvc的注解解析，支持@RequestMapping，@RequestBody，@RequestParam，@PathVariable
    @Bean
    @ConditionalOnMissingBean
    public Contract feignContract(ConversionService feignConversionService) {
        return new SpringMvcContract(this.parameterProcessors, feignConversionService);
    }

    @Bean
    public FormattingConversionService feignConversionService() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        for (FeignFormatterRegistrar feignFormatterRegistrar : feignFormatterRegistrars) {
            feignFormatterRegistrar.registerFormatters(conversionService);
        }
        return conversionService;
    }

    //整合hystrix的feign.builder
    //当引入了Hytrix并开启参数feign.hystrix.enabled=true后，
    //会加载feign.hystrix.HystrixFeign.Builder，此时feign具备降级熔断的功能
    @Configuration
    @ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
    protected static class HystrixFeignConfiguration {
        @Bean
        @Scope("prototype")
        @ConditionalOnMissingBean
        @ConditionalOnProperty(name = "feign.hystrix.enabled")
        public Feign.Builder feignHystrixBuilder() {
            return HystrixFeign.builder();
        }
    }

    //重试器,默认Retryer.NEVER_RETRY，不进行重试，可自己实现Retryer接口实现自己的重试策略，
    //但是feign在集成了ribbon的情况下，最好保持默认不进行重试，因为ribbon也会有重试策略，如果feign也开启重试，容易产生混乱
    @Bean
    @ConditionalOnMissingBean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public Feign.Builder feignBuilder(Retryer retryer) {
        return Feign.builder().retryer(retryer);
    }

//    @Bean
//    @ConditionalOnMissingBean(FeignLoggerFactory.class)
//    public FeignLoggerFactory feignLoggerFactory() {
//        return new DefaultFeignLoggerFactory(logger);
//    }

}
