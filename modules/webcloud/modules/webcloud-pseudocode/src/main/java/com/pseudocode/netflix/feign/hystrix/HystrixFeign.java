package com.pseudocode.netflix.feign.hystrix;

import com.pseudocode.netflix.feign.core.*;
import com.pseudocode.netflix.feign.core.codec.Decoder;
import com.pseudocode.netflix.feign.core.codec.Encoder;
import com.pseudocode.netflix.feign.core.codec.ErrorDecoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public final class HystrixFeign {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Feign.Builder {

        private Contract contract = new Contract.Default();
        private SetterFactory setterFactory = new SetterFactory.Default();

        public Builder setterFactory(SetterFactory setterFactory) {
            this.setterFactory = setterFactory;
            return this;
        }

        public <T> T target(Target<T> target, T fallback) {
            return build(fallback != null ? new FallbackFactory.Default<T>(fallback) : null)
                    .newInstance(target);
        }

        public <T> T target(Target<T> target, FallbackFactory<? extends T> fallbackFactory) {
            return build(fallbackFactory).newInstance(target);
        }

        public <T> T target(Class<T> apiType, String url, T fallback) {
            return target(new Target.HardCodedTarget<T>(apiType, url), fallback);
        }

        public <T> T target(Class<T> apiType, String url, FallbackFactory<? extends T> fallbackFactory) {
            return target(new Target.HardCodedTarget<T>(apiType, url), fallbackFactory);
        }

        @Override
        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        @Override
        public Feign build() {
            return build(null);
        }

        Feign build(final FallbackFactory<?> nullableFallbackFactory) {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                @Override public InvocationHandler create(Target target,
                                                          Map<Method, MethodHandler> dispatch) {
                    return new HystrixInvocationHandler(target, dispatch, setterFactory, nullableFallbackFactory);
                }
            });
            super.contract(new HystrixDelegatingContract(contract));
            return super.build();
        }

        // Covariant overrides to support chaining to new fallback method.
        @Override
        public Builder logLevel(Logger.Level logLevel) {
            return (Builder) super.logLevel(logLevel);
        }

        @Override
        public Builder client(Client client) {
            return (Builder) super.client(client);
        }

        @Override
        public Builder retryer(Retryer retryer) {
            return (Builder) super.retryer(retryer);
        }

        @Override
        public Builder logger(Logger logger) {
            return (Builder) super.logger(logger);
        }

        @Override
        public Builder encoder(Encoder encoder) {
            return (Builder) super.encoder(encoder);
        }

        @Override
        public Builder decoder(Decoder decoder) {
            return (Builder) super.decoder(decoder);
        }

        @Override
        public Builder mapAndDecode(ResponseMapper mapper, Decoder decoder) {
            return (Builder) super.mapAndDecode(mapper, decoder);
        }

        @Override
        public Builder decode404() {
            return (Builder) super.decode404();
        }

        @Override
        public Builder errorDecoder(ErrorDecoder errorDecoder) {
            return (Builder) super.errorDecoder(errorDecoder);
        }

        @Override
        public Builder options(Request.Options options) {
            return (Builder) super.options(options);
        }

        @Override
        public Builder requestInterceptor(RequestInterceptor requestInterceptor) {
            return (Builder) super.requestInterceptor(requestInterceptor);
        }

        @Override
        public Builder requestInterceptors(Iterable<RequestInterceptor> requestInterceptors) {
            return (Builder) super.requestInterceptors(requestInterceptors);
        }
    }
}

