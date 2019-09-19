package com.pseudocode.netflix.feign.hystrix;

import com.pseudocode.netflix.feign.core.InvocationHandlerFactory.MethodHandler;
import com.pseudocode.netflix.feign.core.Target;
import com.pseudocode.netflix.hystrix.core.HystrixCommand;
import com.pseudocode.netflix.hystrix.core.HystrixCommand.Setter;
import rx.Observable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//feign也整合了Hystrix,feign在方法调用的时候会经过统一方法拦截器FeignInvocationHandler的处理，
//而HystrixFeign则是使用了HystrixInvocationHandler代替，在方法调用的时候进行Hystrix的封装
//Hystrix有超时时间，feign本身也有超时时间，正常来说Hystrix的超时间要大于feign的超时时间，
//如果是小于的话，Hytrix已经超时了，feign再等待就已经没有意义了
//feign超时会触发重试操作，此时要是Hytrix发生超时异常返回了，但这并不会切断feign的继续操作。
//假设Hytrix的超时时间为1s，feign设置的超时时间为2s，而真正业务操作需要耗时3s，
//这时Hytrix超时异常返回，而后feign也会发生超时异常，但是feign会根据超时策略继续进行重试操作，并不会因为Hytrix的中断而中断。
//所以Hytrix的超时时间一般要大于feign的总超时时间，如这个例子中要设置2**5(默认重试次数4 + 1)=10s，
//公式就是Hytrix的超时间=feign的超时时间*(feign的重试次数 + 1)
final class HystrixInvocationHandler implements InvocationHandler {

    private final Target<?> target;
    private final Map<Method, MethodHandler> dispatch;
    private final FallbackFactory<?> fallbackFactory; // Nullable
    private final Map<Method, Method> fallbackMethodMap;
    private final Map<Method, Setter> setterMethodMap;

    HystrixInvocationHandler(Target<?> target, Map<Method, MethodHandler> dispatch,
                             SetterFactory setterFactory, FallbackFactory<?> fallbackFactory) {
        this.target = checkNotNull(target, "target");
        this.dispatch = checkNotNull(dispatch, "dispatch");
        this.fallbackFactory = fallbackFactory;
        this.fallbackMethodMap = toFallbackMethod(dispatch);
        this.setterMethodMap = toSetters(setterFactory, target, dispatch.keySet());
    }

    static Map<Method, Method> toFallbackMethod(Map<Method, MethodHandler> dispatch) {
        Map<Method, Method> result = new LinkedHashMap<Method, Method>();
        for (Method method : dispatch.keySet()) {
            method.setAccessible(true);
            result.put(method, method);
        }
        return result;
    }

    static Map<Method, Setter> toSetters(SetterFactory setterFactory, Target<?> target,
                                         Set<Method> methods) {
        Map<Method, Setter> result = new LinkedHashMap<Method, Setter>();
        for (Method method : methods) {
            method.setAccessible(true);
            result.put(method, setterFactory.create(target, method));
        }
        return result;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        // early exit if the invoked method is from java.lang.Object
        // code is the same as ReflectiveFeign.FeignInvocationHandler
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler = args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }

        //命令行模式
        HystrixCommand<Object> hystrixCommand = new HystrixCommand<Object>(setterMethodMap.get(method)) {
            @Override
            protected Object run() throws Exception {
                try {
                    //正常逻辑的执行
                    return HystrixInvocationHandler.this.dispatch.get(method).invoke(args);
                } catch (Exception e) {
                    throw e;
                } catch (Throwable t) {
                    throw (Error) t;
                }
            }

            @Override
            protected Object getFallback() {
                if (fallbackFactory == null) {
                    return super.getFallback();
                }
                try {
                    Object fallback = fallbackFactory.create(getExecutionException());
                    Object result = fallbackMethodMap.get(method).invoke(fallback, args);
                    if (isReturnsHystrixCommand(method)) {
                        return ((HystrixCommand) result).execute();
                    } else if (isReturnsObservable(method)) {
                        // Create a cold Observable
                        return ((Observable) result).toBlocking().first();
                    } else if (isReturnsSingle(method)) {
                        // Create a cold Observable as a Single
                        return ((Single) result).toObservable().toBlocking().first();
                    } else if (isReturnsCompletable(method)) {
                        ((Completable) result).await();
                        return null;
                    } else {
                        return result;
                    }
                } catch (IllegalAccessException e) {
                    // shouldn't happen as method is public due to being an interface
                    throw new AssertionError(e);
                } catch (InvocationTargetException e) {
                    // Exceptions on fallback are tossed by Hystrix
                    throw new AssertionError(e.getCause());
                }
            }
        };

        if (isReturnsHystrixCommand(method)) {
            return hystrixCommand;
        } else if (isReturnsObservable(method)) {
            // Create a cold Observable
            return hystrixCommand.toObservable();
        } else if (isReturnsSingle(method)) {
            // Create a cold Observable as a Single
            return hystrixCommand.toObservable().toSingle();
        } else if (isReturnsCompletable(method)) {
            return hystrixCommand.toObservable().toCompletable();
        }
        //调用hystrixCommand.execute()，hystrix如果是多线程隔离，会在隔离线程池中取线程运行run()，
        //而调用程序要在execute()调用处一直堵塞着，直到run()运行完成返回结果
        return hystrixCommand.execute();
    }

    private boolean isReturnsCompletable(Method method) {
        return Completable.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsHystrixCommand(Method method) {
        return HystrixCommand.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsObservable(Method method) {
        return Observable.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsSingle(Method method) {
        return Single.class.isAssignableFrom(method.getReturnType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HystrixInvocationHandler) {
            HystrixInvocationHandler other = (HystrixInvocationHandler) obj;
            return target.equals(other.target);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return target.toString();
    }
}

