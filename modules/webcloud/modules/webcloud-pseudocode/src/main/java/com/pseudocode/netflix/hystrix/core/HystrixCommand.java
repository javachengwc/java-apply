package com.pseudocode.netflix.hystrix.core;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func0;

//hystrix命令行
public abstract class HystrixCommand<R> extends AbstractCommand<R> implements HystrixExecutable<R>, HystrixInvokableInfo<R>, HystrixObservable<R> {

    protected HystrixCommand(HystrixCommandGroupKey group) {
        super(group, null, null, null, null, null, null, null, null, null, null, null);
    }

    protected HystrixCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
        super(group, null, threadPool, null, null, null, null, null, null, null, null, null);
    }

    protected HystrixCommand(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, null, null, null, null, HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(executionIsolationThreadTimeoutInMilliseconds), null, null, null, null, null, null);
    }

    protected HystrixCommand(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, null, threadPool, null, null, HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(executionIsolationThreadTimeoutInMilliseconds), null, null, null, null, null, null);
    }

    protected HystrixCommand(Setter setter) {
        // use 'null' to specify use the default
        this(setter.groupKey, setter.commandKey, setter.threadPoolKey, null, null, setter.commandPropertiesDefaults, setter.threadPoolPropertiesDefaults, null, null, null, null, null);
    }

    HystrixCommand(HystrixCommandGroupKey group, HystrixCommandKey key, HystrixThreadPoolKey threadPoolKey, HystrixCircuitBreaker circuitBreaker, HystrixThreadPool threadPool,
                                            HystrixCommandProperties.Setter commandPropertiesDefaults, HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults,
                                            HystrixCommandMetrics metrics, TryableSemaphore fallbackSemaphore, TryableSemaphore executionSemaphore,
                                            HystrixPropertiesStrategy propertiesStrategy, HystrixCommandExecutionHook executionHook) {
        super(group, key, threadPoolKey, circuitBreaker, threadPool, commandPropertiesDefaults, threadPoolPropertiesDefaults, metrics, fallbackSemaphore, executionSemaphore, propertiesStrategy, executionHook);
    }

    final public static class Setter {

        protected final HystrixCommandGroupKey groupKey;
        protected HystrixCommandKey commandKey;
        protected HystrixThreadPoolKey threadPoolKey;
        protected HystrixCommandProperties.Setter commandPropertiesDefaults;
        protected HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults;

        protected Setter(HystrixCommandGroupKey groupKey) {
            this.groupKey = groupKey;
        }

        public static Setter withGroupKey(HystrixCommandGroupKey groupKey) {
            return new Setter(groupKey);
        }

        public Setter andCommandKey(HystrixCommandKey commandKey) {
            this.commandKey = commandKey;
            return this;
        }

        public Setter andThreadPoolKey(HystrixThreadPoolKey threadPoolKey) {
            this.threadPoolKey = threadPoolKey;
            return this;
        }

        public Setter andCommandPropertiesDefaults(HystrixCommandProperties.Setter commandPropertiesDefaults) {
            this.commandPropertiesDefaults = commandPropertiesDefaults;
            return this;
        }

        public Setter andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults) {
            this.threadPoolPropertiesDefaults = threadPoolPropertiesDefaults;
            return this;
        }

    }

    private final AtomicReference<Thread> executionThread = new AtomicReference<Thread>();
    private final AtomicBoolean interruptOnFutureCancel = new AtomicBoolean(false);

    protected abstract R run() throws Exception;

    protected R getFallback() {
        throw new UnsupportedOperationException("No fallback available.");
    }

    @Override
    final protected Observable<R> getExecutionObservable() {
        return Observable.defer(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                try {
                    return Observable.just(run());
                } catch (Throwable ex) {
                    return Observable.error(ex);
                }
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                // Save thread on which we get subscribed so that we can interrupt it later if needed
                executionThread.set(Thread.currentThread());
            }
        });
    }

    @Override
    final protected Observable<R> getFallbackObservable() {
        return Observable.defer(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                try {
                    return Observable.just(getFallback());
                } catch (Throwable ex) {
                    return Observable.error(ex);
                }
            }
        });
    }

    //同步调用
    public R execute() {
        try {
            return queue().get();
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(decomposeException(e));
        }
    }

    //异步调用
    public Future<R> queue() {
        final Future<R> delegate = toObservable().toBlocking().toFuture();

        final Future<R> f = new Future<R>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if (delegate.isCancelled()) {
                    return false;
                }

                if (HystrixCommand.this.getProperties().executionIsolationThreadInterruptOnFutureCancel().get()) {
                    interruptOnFutureCancel.compareAndSet(false, mayInterruptIfRunning);
                }

                final boolean res = delegate.cancel(interruptOnFutureCancel.get());

                if (!isExecutionComplete() && interruptOnFutureCancel.get()) {
                    final Thread t = executionThread.get();
                    if (t != null && !t.equals(Thread.currentThread())) {
                        t.interrupt();
                    }
                }

                return res;
            }

            @Override
            public boolean isCancelled() {
                return delegate.isCancelled();
            }

            @Override
            public boolean isDone() {
                return delegate.isDone();
            }

            @Override
            public R get() throws InterruptedException, ExecutionException {
                return delegate.get();
            }

            @Override
            public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return delegate.get(timeout, unit);
            }

        };

        if (f.isDone()) {
            try {
                f.get();
                return f;
            } catch (Exception e) {
                Throwable t = decomposeException(e);
                if (t instanceof HystrixBadRequestException) {
                    return f;
                } else if (t instanceof HystrixRuntimeException) {
                    HystrixRuntimeException hre = (HystrixRuntimeException) t;
                    switch (hre.getFailureType()) {
                        case COMMAND_EXCEPTION:
                        case TIMEOUT:
                            // we don't throw these types from queue() only from queue().get() as they are execution errors
                            return f;
                        default:
                            // these are errors we throw from queue() as they as rejection type errors
                            throw hre;
                    }
                } else {
                    throw Exceptions.sneakyThrow(t);
                }
            }
        }

        return f;
    }

    @Override
    protected String getFallbackMethodName() {
        return "getFallback";
    }

    @Override
    protected boolean isFallbackUserDefined() {
        Boolean containsFromMap = commandContainsFallback.get(commandKey);
        if (containsFromMap != null) {
            return containsFromMap;
        } else {
            Boolean toInsertIntoMap;
            try {
                getClass().getDeclaredMethod("getFallback");
                toInsertIntoMap = true;
            } catch (NoSuchMethodException nsme) {
                toInsertIntoMap = false;
            }
            commandContainsFallback.put(commandKey, toInsertIntoMap);
            return toInsertIntoMap;
        }
    }

    @Override
    protected boolean commandIsScalar() {
        return true;
    }
}
