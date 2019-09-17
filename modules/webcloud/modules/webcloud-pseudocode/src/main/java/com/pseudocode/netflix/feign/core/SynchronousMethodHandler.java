package com.pseudocode.netflix.feign.core;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pseudocode.netflix.feign.core.InvocationHandlerFactory.MethodHandler;
import com.pseudocode.netflix.feign.core.Request.Options;
import com.pseudocode.netflix.feign.core.codec.DecodeException;
import com.pseudocode.netflix.feign.core.codec.Decoder;
import com.pseudocode.netflix.feign.core.codec.ErrorDecoder;

import static com.pseudocode.netflix.feign.core.FeignException.errorExecuting;
import static com.pseudocode.netflix.feign.core.FeignException.errorReading;
import static com.pseudocode.netflix.feign.core.Util.checkNotNull;
import static com.pseudocode.netflix.feign.core.Util.ensureClosed;

//同时操作的MethodHandler
final class SynchronousMethodHandler implements MethodHandler {

    private static final long MAX_RESPONSE_BUFFER_SIZE = 8192L;

    private final MethodMetadata metadata;
    private final Target<?> target;
    private final Client client;
    private final Retryer retryer;
    private final List<RequestInterceptor> requestInterceptors;
    private final Logger logger;
    private final Logger.Level logLevel;
    private final RequestTemplate.Factory buildTemplateFromArgs;
    private final Options options;
    private final Decoder decoder;
    private final ErrorDecoder errorDecoder;
    private final boolean decode404;

    private SynchronousMethodHandler(Target<?> target, Client client, Retryer retryer,
                                     List<RequestInterceptor> requestInterceptors, Logger logger,
                                     Logger.Level logLevel, MethodMetadata metadata,
                                     RequestTemplate.Factory buildTemplateFromArgs, Options options,
                                     Decoder decoder, ErrorDecoder errorDecoder, boolean decode404) {
        this.target = checkNotNull(target, "target");
        this.client = checkNotNull(client, "client for %s", target);
        this.retryer = checkNotNull(retryer, "retryer for %s", target);
        this.requestInterceptors =
                checkNotNull(requestInterceptors, "requestInterceptors for %s", target);
        this.logger = checkNotNull(logger, "logger for %s", target);
        this.logLevel = checkNotNull(logLevel, "logLevel for %s", target);
        this.metadata = checkNotNull(metadata, "metadata for %s", target);
        this.buildTemplateFromArgs = checkNotNull(buildTemplateFromArgs, "metadata for %s", target);
        this.options = checkNotNull(options, "options for %s", target);
        this.errorDecoder = checkNotNull(errorDecoder, "errorDecoder for %s", target);
        this.decoder = checkNotNull(decoder, "decoder for %s", target);
        this.decode404 = decode404;
    }

    //每一个FeignClient请求，进入此invoke方法
    @Override
    public Object invoke(Object[] argv) throws Throwable {
        //请求模板
        RequestTemplate template = buildTemplateFromArgs.create(argv);
        Retryer retryer = this.retryer.clone();
        while (true) {
            try {
                return executeAndDecode(template);
            } catch (RetryableException e) {
                //重试机制继续或传播
                retryer.continueOrPropagate(e);
                if (logLevel != Logger.Level.NONE) {
                    logger.logRetry(metadata.configKey(), logLevel);
                }
                continue;
            }
        }
    }

    Object executeAndDecode(RequestTemplate template) throws Throwable {

        //获取请求对象
        Request request = targetRequest(template);

        if (logLevel != Logger.Level.NONE) {
            logger.logRequest(metadata.configKey(), logLevel, request);
        }

        Response response;
        long start = System.nanoTime();
        try {
            //调用LoadBalancerFeignClient负载均衡器客户端执行请求
            response = client.execute(request, options);
            response.toBuilder().request(request).build();
        } catch (IOException e) {
            if (logLevel != Logger.Level.NONE) {
                logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime(start));
            }
            throw errorExecuting(request, e);
        }
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        boolean shouldClose = true;
        try {
            if (logLevel != Logger.Level.NONE) {
                response = logger.logAndRebufferResponse(metadata.configKey(), logLevel, response, elapsedTime);
                response.toBuilder().request(request).build();
            }
            if (Response.class == metadata.returnType()) {
                if (response.body() == null) {
                    return response;
                }
                if (response.body().length() == null || response.body().length() > MAX_RESPONSE_BUFFER_SIZE) {
                    shouldClose = false;
                    return response;
                }
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                return response.toBuilder().body(bodyData).build();
            }
            if (response.status() >= 200 && response.status() < 300) {
                if (void.class == metadata.returnType()) {
                    return null;
                } else {
                    return decode(response);
                }
            } else if (decode404 && response.status() == 404 && void.class != metadata.returnType()) {
                return decode(response);
            } else {
                throw errorDecoder.decode(metadata.configKey(), response);
            }
        } catch (IOException e) {
            if (logLevel != Logger.Level.NONE) {
                logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime);
            }
            throw errorReading(request, response, e);
        } finally {
            if (shouldClose) {
                ensureClosed(response.body());
            }
        }
    }

    long elapsedTime(long start) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }

    Request targetRequest(RequestTemplate template) {
        for (RequestInterceptor interceptor : requestInterceptors) {
            interceptor.apply(template);
        }
        return target.apply(new RequestTemplate(template));
    }

    Object decode(Response response) throws Throwable {
        try {
            return decoder.decode(response, metadata.returnType());
        } catch (FeignException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new DecodeException(e.getMessage(), e);
        }
    }

    static class Factory {

        private final Client client;
        private final Retryer retryer;
        private final List<RequestInterceptor> requestInterceptors;
        private final Logger logger;
        private final Logger.Level logLevel;
        private final boolean decode404;

        Factory(Client client, Retryer retryer, List<RequestInterceptor> requestInterceptors,
                Logger logger, Logger.Level logLevel, boolean decode404) {
            this.client = checkNotNull(client, "client");
            this.retryer = checkNotNull(retryer, "retryer");
            this.requestInterceptors = checkNotNull(requestInterceptors, "requestInterceptors");
            this.logger = checkNotNull(logger, "logger");
            this.logLevel = checkNotNull(logLevel, "logLevel");
            this.decode404 = decode404;
        }

        public MethodHandler create(Target<?> target, MethodMetadata md,
                                    RequestTemplate.Factory buildTemplateFromArgs,
                                    Options options, Decoder decoder, ErrorDecoder errorDecoder) {
            return new SynchronousMethodHandler(target, client, retryer, requestInterceptors, logger,
                    logLevel, md, buildTemplateFromArgs, options, decoder,
                    errorDecoder, decode404);
        }
    }
}

