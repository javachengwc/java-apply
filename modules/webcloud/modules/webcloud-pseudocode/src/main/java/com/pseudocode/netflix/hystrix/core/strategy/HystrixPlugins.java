package com.pseudocode.netflix.hystrix.core.strategy;


import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

import com.pseudocode.netflix.hystrix.core.strategy.concurrency.HystrixConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//hystrix默认提供了HystrixPlugins类，可以让用户自定义线程池
public class HystrixPlugins {

    private static class LazyHolder { private static final HystrixPlugins INSTANCE = HystrixPlugins.create(); }
    private final ClassLoader classLoader;
    final AtomicReference<HystrixEventNotifier> notifier = new AtomicReference<HystrixEventNotifier>();
    final AtomicReference<HystrixConcurrencyStrategy> concurrencyStrategy = new AtomicReference<HystrixConcurrencyStrategy>();
    final AtomicReference<HystrixMetricsPublisher> metricsPublisher = new AtomicReference<HystrixMetricsPublisher>();
    final AtomicReference<HystrixPropertiesStrategy> propertiesFactory = new AtomicReference<HystrixPropertiesStrategy>();
    final AtomicReference<HystrixCommandExecutionHook> commandExecutionHook = new AtomicReference<HystrixCommandExecutionHook>();
    private final HystrixDynamicProperties dynamicProperties;


    private HystrixPlugins(ClassLoader classLoader, LoggerSupplier logSupplier) {
        //This will load Archaius if its in the classpath.
        this.classLoader = classLoader;
        //N.B. Do not use a logger before this is loaded as it will most likely load the configuration system.
        //The configuration system may need to do something prior to loading logging. @agentgt
        dynamicProperties = resolveDynamicProperties(classLoader, logSupplier);
    }

    static HystrixPlugins create(ClassLoader classLoader, LoggerSupplier logSupplier) {
        return new HystrixPlugins(classLoader, logSupplier);
    }

    static HystrixPlugins create(ClassLoader classLoader) {
        return new HystrixPlugins(classLoader, new LoggerSupplier() {
            @Override
            public Logger getLogger() {
                return LoggerFactory.getLogger(HystrixPlugins.class);
            }
        });
    }

    static HystrixPlugins create() {
        return create(HystrixPlugins.class.getClassLoader());
    }

    public static HystrixPlugins getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static void reset() {
        getInstance().notifier.set(null);
        getInstance().concurrencyStrategy.set(null);
        getInstance().metricsPublisher.set(null);
        getInstance().propertiesFactory.set(null);
        getInstance().commandExecutionHook.set(null);
        HystrixMetricsPublisherFactory.reset();
    }

    public HystrixEventNotifier getEventNotifier() {
        if (notifier.get() == null) {
            // check for an implementation from Archaius first
            Object impl = getPluginImplementation(HystrixEventNotifier.class);
            if (impl == null) {
                // nothing set via Archaius so initialize with default
                notifier.compareAndSet(null, HystrixEventNotifierDefault.getInstance());
                // we don't return from here but call get() again in case of thread-race so the winner will always get returned
            } else {
                // we received an implementation from Archaius so use it
                notifier.compareAndSet(null, (HystrixEventNotifier) impl);
            }
        }
        return notifier.get();
    }

    public void registerEventNotifier(HystrixEventNotifier impl) {
        if (!notifier.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered.");
        }
    }

    public HystrixConcurrencyStrategy getConcurrencyStrategy() {
        if (concurrencyStrategy.get() == null) {
            // check for an implementation from Archaius first
            Object impl = getPluginImplementation(HystrixConcurrencyStrategy.class);
            if (impl == null) {
                // nothing set via Archaius so initialize with default
                concurrencyStrategy.compareAndSet(null, HystrixConcurrencyStrategyDefault.getInstance());
                // we don't return from here but call get() again in case of thread-race so the winner will always get returned
            } else {
                // we received an implementation from Archaius so use it
                concurrencyStrategy.compareAndSet(null, (HystrixConcurrencyStrategy) impl);
            }
        }
        return concurrencyStrategy.get();
    }

    public void registerConcurrencyStrategy(HystrixConcurrencyStrategy impl) {
        if (!concurrencyStrategy.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered.");
        }
    }

    public HystrixMetricsPublisher getMetricsPublisher() {
        if (metricsPublisher.get() == null) {
            // check for an implementation from Archaius first
            Object impl = getPluginImplementation(HystrixMetricsPublisher.class);
            if (impl == null) {
                // nothing set via Archaius so initialize with default
                metricsPublisher.compareAndSet(null, HystrixMetricsPublisherDefault.getInstance());
                // we don't return from here but call get() again in case of thread-race so the winner will always get returned
            } else {
                // we received an implementation from Archaius so use it
                metricsPublisher.compareAndSet(null, (HystrixMetricsPublisher) impl);
            }
        }
        return metricsPublisher.get();
    }

    public void registerMetricsPublisher(HystrixMetricsPublisher impl) {
        if (!metricsPublisher.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered.");
        }
    }

    public HystrixPropertiesStrategy getPropertiesStrategy() {
        if (propertiesFactory.get() == null) {
            // check for an implementation from Archaius first
            Object impl = getPluginImplementation(HystrixPropertiesStrategy.class);
            if (impl == null) {
                // nothing set via Archaius so initialize with default
                propertiesFactory.compareAndSet(null, HystrixPropertiesStrategyDefault.getInstance());
                // we don't return from here but call get() again in case of thread-race so the winner will always get returned
            } else {
                // we received an implementation from Archaius so use it
                propertiesFactory.compareAndSet(null, (HystrixPropertiesStrategy) impl);
            }
        }
        return propertiesFactory.get();
    }

    public HystrixDynamicProperties getDynamicProperties() {
        return dynamicProperties;
    }

    public void registerPropertiesStrategy(HystrixPropertiesStrategy impl) {
        if (!propertiesFactory.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered.");
        }
    }

    public HystrixCommandExecutionHook getCommandExecutionHook() {
        if (commandExecutionHook.get() == null) {
            // check for an implementation from Archaius first
            Object impl = getPluginImplementation(HystrixCommandExecutionHook.class);
            if (impl == null) {
                // nothing set via Archaius so initialize with default
                commandExecutionHook.compareAndSet(null, HystrixCommandExecutionHookDefault.getInstance());
                // we don't return from here but call get() again in case of thread-race so the winner will always get returned
            } else {
                // we received an implementation from Archaius so use it
                commandExecutionHook.compareAndSet(null, (HystrixCommandExecutionHook) impl);
            }
        }
        return commandExecutionHook.get();
    }

    public void registerCommandExecutionHook(HystrixCommandExecutionHook impl) {
        if (!commandExecutionHook.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered.");
        }
    }


    private <T> T getPluginImplementation(Class<T> pluginClass) {
        T p = getPluginImplementationViaProperties(pluginClass, dynamicProperties);
        if (p != null) return p;
        return findService(pluginClass, classLoader);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPluginImplementationViaProperties(Class<T> pluginClass, HystrixDynamicProperties dynamicProperties) {
        String classSimpleName = pluginClass.getSimpleName();
        // Check Archaius for plugin class.
        String propertyName = "hystrix.plugin." + classSimpleName + ".implementation";
        String implementingClass = dynamicProperties.getString(propertyName, null).get();
        if (implementingClass != null) {
            try {
                Class<?> cls = Class.forName(implementingClass);
                // narrow the scope (cast) to the type we're expecting
                cls = cls.asSubclass(pluginClass);
                return (T) cls.newInstance();
            } catch (ClassCastException e) {
                throw new RuntimeException(classSimpleName + " implementation is not an instance of " + classSimpleName + ": " + implementingClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(classSimpleName + " implementation class not found: " + implementingClass, e);
            } catch (InstantiationException e) {
                throw new RuntimeException(classSimpleName + " implementation not able to be instantiated: " + implementingClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(classSimpleName + " implementation not able to be accessed: " + implementingClass, e);
            }
        } else {
            return null;
        }
    }

    private static HystrixDynamicProperties resolveDynamicProperties(ClassLoader classLoader, LoggerSupplier logSupplier) {
        HystrixDynamicProperties hp = getPluginImplementationViaProperties(HystrixDynamicProperties.class,
                HystrixDynamicPropertiesSystemProperties.getInstance());
        if (hp != null) {
            logSupplier.getLogger().debug(
                    "Created HystrixDynamicProperties instance from System property named "
                            + "\"hystrix.plugin.HystrixDynamicProperties.implementation\". Using class: {}",
                    hp.getClass().getCanonicalName());
            return hp;
        }
        hp = findService(HystrixDynamicProperties.class, classLoader);
        if (hp != null) {
            logSupplier.getLogger()
                    .debug("Created HystrixDynamicProperties instance by loading from ServiceLoader. Using class: {}",
                            hp.getClass().getCanonicalName());
            return hp;
        }
        hp = HystrixArchaiusHelper.createArchaiusDynamicProperties();
        if (hp != null) {
            logSupplier.getLogger().debug("Created HystrixDynamicProperties. Using class : {}",
                    hp.getClass().getCanonicalName());
            return hp;
        }
        hp = HystrixDynamicPropertiesSystemProperties.getInstance();
        logSupplier.getLogger().info("Using System Properties for HystrixDynamicProperties! Using class: {}",
                hp.getClass().getCanonicalName());
        return hp;
    }

    private static <T> T findService(
            Class<T> spi,
            ClassLoader classLoader) throws ServiceConfigurationError {

        ServiceLoader<T> sl = ServiceLoader.load(spi,
                classLoader);
        for (T s : sl) {
            if (s != null)
                return s;
        }
        return null;
    }

    interface LoggerSupplier {
        Logger getLogger();
    }


}
