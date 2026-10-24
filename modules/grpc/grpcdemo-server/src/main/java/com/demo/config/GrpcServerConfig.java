package com.demo.config;

import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class GrpcServerConfig {

    private static final Logger log = LoggerFactory.getLogger(GrpcServerConfig.class);

    /** Container CPU limit from k8s deployment (2000m = 2 cores) */
    private static final int CONTAINER_CPU_LIMIT = 2;

    private static final int CORE_POOL_SIZE = CONTAINER_CPU_LIMIT * 2;
    private static final int MAX_POOL_SIZE = CONTAINER_CPU_LIMIT * 4;
    private static final int QUEUE_CAPACITY = 200;
    private static final long KEEP_ALIVE_SECONDS = 60L;

    /** Maximum inbound message size: 64 KB  */
    private static final int MAX_INBOUND_MESSAGE_SIZE = 64 * 1024;

    /** Maximum concurrent calls per client connection */
    private static final int MAX_CONCURRENT_CALLS_PER_CONNECTION = 100;

    /**
     * Bounded thread pool for gRPC business processing.
     *
     * <p>Registered as a Spring bean with {@code destroyMethod = "shutdown"} so
     * that the executor is properly terminated when the Spring context closes,
     * preventing JVM hang on shutdown. Threads are set as daemon to avoid
     * blocking JVM exit if graceful shutdown does not complete in time.</p>
     *
     * @return the bounded thread pool executor
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolExecutor grpcBizExecutor() {
        ThreadFactory bizThreadFactory = new ThreadFactory() {
            private final AtomicInteger threadSeq = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "grpc-biz-pool-" + threadSeq.getAndIncrement());
                thread.setDaemon(true);
                thread.setUncaughtExceptionHandler((t, e) ->
                        log.error("gRPC business thread uncaught exception, thread: {}", t.getName(), e));
                return thread;
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                bizThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );

        log.info("gRPC business thread pool initialized: core={}, max={}, queue={}",
                CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_CAPACITY);

        return executor;
    }

    /**
     * Configure the gRPC server to use a bounded business thread pool,
     * message size limits, and concurrency caps.
     *
     * @param grpcBizExecutor the Spring-managed thread pool
     * @return the gRPC server configurer
     */
    @Bean
    public GrpcServerConfigurer grpcServerConfigurer(ThreadPoolExecutor grpcBizExecutor) {
        return serverBuilder -> {
            serverBuilder.executor(grpcBizExecutor);
            serverBuilder.maxInboundMessageSize(MAX_INBOUND_MESSAGE_SIZE);

            // maxConcurrentCallsPerConnection is only available on NettyServerBuilder.
            // The shaded jar (grpc-netty-shaded) relocates the class to a different
            // package, so use reflection to avoid a compile-time dependency on the
            // shaded package path.
            try {
                serverBuilder.getClass()
                        .getMethod("maxConcurrentCallsPerConnection", int.class)
                        .invoke(serverBuilder, MAX_CONCURRENT_CALLS_PER_CONNECTION);
                log.info("gRPC server configured: maxInboundMessageSize={}KB, maxConcurrentCallsPerConnection={}",
                        MAX_INBOUND_MESSAGE_SIZE / 1024, MAX_CONCURRENT_CALLS_PER_CONNECTION);
            } catch (NoSuchMethodException e) {
                log.warn("gRPC: maxConcurrentCallsPerConnection not applied (serverBuilder type: {})",
                        serverBuilder.getClass().getName());
            } catch (Exception e) {
                log.warn("gRPC: maxConcurrentCallsPerConnection invocation failed", e);
            }
        };
    }
}

