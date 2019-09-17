package com.pseudocode.cloud.openfeign.core;

import com.pseudocode.netflix.feign.core.Contract;
import com.pseudocode.netflix.feign.core.Logger;
import com.pseudocode.netflix.feign.core.RequestInterceptor;
import com.pseudocode.netflix.feign.core.Retryer;
import com.pseudocode.netflix.feign.core.codec.Decoder;
import com.pseudocode.netflix.feign.core.codec.Encoder;
import com.pseudocode.netflix.feign.core.codec.ErrorDecoder;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//feign配置参数
@ConfigurationProperties("feign.client")
public class FeignClientProperties {

    private boolean defaultToProperties = true;

    private String defaultConfig = "default";

    private Map<String, FeignClientConfiguration> config = new HashMap<>();

    public boolean isDefaultToProperties() {
        return defaultToProperties;
    }

    public void setDefaultToProperties(boolean defaultToProperties) {
        this.defaultToProperties = defaultToProperties;
    }

    public String getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public Map<String, FeignClientConfiguration> getConfig() {
        return config;
    }

    public void setConfig(Map<String, FeignClientConfiguration> config) {
        this.config = config;
    }

    public static class FeignClientConfiguration {

        //日志级别,配置项为:feign.client.config.default.loggerLevel
        private Logger.Level loggerLevel;

        //2个超时时间如果有配置值，会设置到Request.Options中
        //连接超时
        private Integer connectTimeout;

        //访问超时,配置项为:feign.client.config.default.readTimeout
        private Integer readTimeout;

        //重试器
        private Class<Retryer> retryer;

        private Class<ErrorDecoder> errorDecoder;

        //feign请求拦截器
        private List<Class<RequestInterceptor>> requestInterceptors;

        private Boolean decode404;

        private Class<Decoder> decoder;

        private Class<Encoder> encoder;

        private Class<Contract> contract;

        public Logger.Level getLoggerLevel() {
            return loggerLevel;
        }

        public void setLoggerLevel(Logger.Level loggerLevel) {
            this.loggerLevel = loggerLevel;
        }

        public Integer getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Integer getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Integer readTimeout) {
            this.readTimeout = readTimeout;
        }

        public Class<Retryer> getRetryer() {
            return retryer;
        }

        public void setRetryer(Class<Retryer> retryer) {
            this.retryer = retryer;
        }

        public Class<ErrorDecoder> getErrorDecoder() {
            return errorDecoder;
        }

        public void setErrorDecoder(Class<ErrorDecoder> errorDecoder) {
            this.errorDecoder = errorDecoder;
        }

        public List<Class<RequestInterceptor>> getRequestInterceptors() {
            return requestInterceptors;
        }

        public void setRequestInterceptors(List<Class<RequestInterceptor>> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
        }

        public Boolean getDecode404() {
            return decode404;
        }

        public void setDecode404(Boolean decode404) {
            this.decode404 = decode404;
        }

        public Class<Decoder> getDecoder() {
            return decoder;
        }

        public void setDecoder(Class<Decoder> decoder) {
            this.decoder = decoder;
        }

        public Class<Encoder> getEncoder() {
            return encoder;
        }

        public void setEncoder(Class<Encoder> encoder) {
            this.encoder = encoder;
        }

        public Class<Contract> getContract() {
            return contract;
        }

        public void setContract(Class<Contract> contract) {
            this.contract = contract;
        }

    }

}

