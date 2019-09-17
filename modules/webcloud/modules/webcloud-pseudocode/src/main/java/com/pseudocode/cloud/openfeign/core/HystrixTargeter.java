package com.pseudocode.cloud.openfeign.core;


import com.pseudocode.netflix.feign.core.Feign;
import com.pseudocode.netflix.feign.core.Target;
import com.pseudocode.netflix.feign.hystrix.HystrixFeign;
import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
class HystrixTargeter implements Targeter {

    @Override
    public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
                        Target.HardCodedTarget<T> target) {
        if (!(feign instanceof HystrixFeign.Builder)) {
            return feign.target(target);
        }
        HystrixFeign.Builder builder = (HystrixFeign.Builder) feign;
        SetterFactory setterFactory = getOptional(factory.getName(), context, SetterFactory.class);
        if (setterFactory != null) {
            builder.setterFactory(setterFactory);
        }
        Class<?> fallback = factory.getFallback();
        if (fallback != void.class) {
            return targetWithFallback(factory.getName(), context, target, builder, fallback);
        }
        Class<?> fallbackFactory = factory.getFallbackFactory();
        if (fallbackFactory != void.class) {
            return targetWithFallbackFactory(factory.getName(), context, target, builder, fallbackFactory);
        }

        return feign.target(target);
    }

    private <T> T targetWithFallbackFactory(String feignClientName, FeignContext context,
                                            Target.HardCodedTarget<T> target,
                                            HystrixFeign.Builder builder,
                                            Class<?> fallbackFactoryClass) {
        FallbackFactory<? extends T> fallbackFactory = (FallbackFactory<? extends T>)
                getFromContext("fallbackFactory", feignClientName, context, fallbackFactoryClass, FallbackFactory.class);
		/* We take a sample fallback from the fallback factory to check if it returns a fallback
		that is compatible with the annotated feign interface. */
        Object exampleFallback = fallbackFactory.create(new RuntimeException());
        Assert.notNull(exampleFallback,
                String.format("Incompatible fallbackFactory instance for feign client %s. Factory may not produce null!", feignClientName));
        if (!target.type().isAssignableFrom(exampleFallback.getClass())) {
            throw new IllegalStateException(
                    String.format("Incompatible fallbackFactory instance for feign client %s. " +
                                    "Factory produces instances of '%s', but should produce instances of '%s'",
                            feignClientName, exampleFallback.getClass(), target.type()));
        }
        return builder.target(target, fallbackFactory);
    }


    private <T> T targetWithFallback(String feignClientName, FeignContext context,
                                     Target.HardCodedTarget<T> target,
                                     HystrixFeign.Builder builder, Class<?> fallback) {
        T fallbackInstance = getFromContext("fallback", feignClientName, context, fallback, target.type());
        return builder.target(target, fallbackInstance);
    }

    private <T> T getFromContext(String fallbackMechanism, String feignClientName, FeignContext context,
                                 Class<?> beanType, Class<T> targetType) {
        Object fallbackInstance = context.getInstance(feignClientName, beanType);
        if (fallbackInstance == null) {
            throw new IllegalStateException(String.format("No " + fallbackMechanism + " instance of type %s found for feign client %s",
                    beanType, feignClientName));
        }

        if (!targetType.isAssignableFrom(beanType)) {
            throw new IllegalStateException(
                    String.format("Incompatible " + fallbackMechanism +
                                    " instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
                            beanType, targetType, feignClientName));
        }
        return (T) fallbackInstance;
    }

    private <T> T getOptional(String feignClientName, FeignContext context, Class<T> beanType) {
        return context.getInstance(feignClientName, beanType);
    }
}
