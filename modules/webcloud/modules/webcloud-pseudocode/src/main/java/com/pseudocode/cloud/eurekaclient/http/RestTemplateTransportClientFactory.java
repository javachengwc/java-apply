package com.pseudocode.cloud.eurekaclient.http;


import java.net.URI;
import java.net.URISyntaxException;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.discovery.shared.resolver.EurekaEndpoint;
import com.pseudocode.netflix.eureka.client.discovery.shared.transport.EurekaHttpClient;
import com.pseudocode.netflix.eureka.client.discovery.shared.transport.TransportClientFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class RestTemplateTransportClientFactory implements TransportClientFactory {

    @Override
    public EurekaHttpClient newClient(EurekaEndpoint serviceUrl) {
        return new RestTemplateEurekaHttpClient(restTemplate(serviceUrl.getServiceUrl()), serviceUrl.getServiceUrl());
    }

    private RestTemplate restTemplate(String serviceUrl) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            URI serviceURI = new URI(serviceUrl);
            if (serviceURI.getUserInfo() != null) {
                String[] credentials = serviceURI.getUserInfo().split(":");
                if (credentials.length == 2) {
                    restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(
                            credentials[0], credentials[1]));
                }
            }
        }
        catch (URISyntaxException ignore) {

        }

        restTemplate.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());

        return restTemplate;
    }

    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE));

        SimpleModule jsonModule = new SimpleModule();
        jsonModule.setSerializerModifier(createJsonSerializerModifier());//keyFormatter, compact));
        converter.getObjectMapper().registerModule(jsonModule);

        converter.getObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        converter.getObjectMapper().configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
//        converter.getObjectMapper().addMixIn(Applications.class, ApplicationsJsonMixIn.class);
//        converter.getObjectMapper().addMixIn(InstanceInfo.class, InstanceInfoJsonMixIn.class);

        return converter;
    }

    public static BeanSerializerModifier createJsonSerializerModifier() {
        return new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                if (beanDesc.getBeanClass().isAssignableFrom(InstanceInfo.class)) {
                    //return new InstanceInfoJsonBeanSerializer((BeanSerializerBase) serializer, false);
                    return null;
                }
                return serializer;
            }
        };
    }

    @Override
    public void shutdown() {
    }

}

