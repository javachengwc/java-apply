package com.demo.config;


import com.demo.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "grpc.client")
@Data
public class GrpcClientConfig {

    private static final Logger log = LoggerFactory.getLogger(GrpcClientConfig.class);

    private String target = "localhost:9090";
    private long keepAliveTime = 30;
    private long keepAliveTimeout = 10;
    private long idleTimeout = 300;
    private long connectTimeout = 5;
    private long callTimeout = 30;
    private int maxInboundMessageSize = 65536;

    private PoolConfig pool = new PoolConfig();

    @Data
    public static class PoolConfig {
        private int minIdle = 2;
        private int maxActive = 10;
        private long maxWaitTime = 3000;
    }

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel managedChannel() {
        log.info("Creating gRPC ManagedChannel for target: {}", target);

        NettyChannelBuilder builder = NettyChannelBuilder.forTarget(target)
                .negotiationType(NegotiationType.PLAINTEXT)
                .keepAliveTime(keepAliveTime, TimeUnit.SECONDS)
                .keepAliveTimeout(keepAliveTimeout, TimeUnit.SECONDS)
                .idleTimeout(idleTimeout, TimeUnit.SECONDS)
                .maxInboundMessageSize(maxInboundMessageSize)
                .userAgent("grpc-client/1.0");

        try {
            ManagedChannel channel = builder.build();
            log.info("gRPC ManagedChannel created successfully with target: {}, keepAliveTime: {}s, maxInboundMessageSize: {}",
                    target, keepAliveTime, maxInboundMessageSize);
            return channel;
        } catch (Exception e) {
            log.error("Failed to create gRPC ManagedChannel", e);
            throw new RuntimeException("Failed to create gRPC ManagedChannel", e);
        }
    }

    @Bean
    public HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub(ManagedChannel managedChannel) {
        return HelloServiceGrpc.newBlockingStub(managedChannel)
                .withDeadlineAfter(callTimeout, TimeUnit.SECONDS);
    }
}
