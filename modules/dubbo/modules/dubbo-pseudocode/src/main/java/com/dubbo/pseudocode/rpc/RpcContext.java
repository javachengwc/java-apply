package com.dubbo.pseudocode.rpc;


import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.threadlocal.InternalThreadLocal;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.*;

import static org.apache.dubbo.common.constants.CommonConstants.*;
import static org.apache.dubbo.rpc.Constants.ASYNC_KEY;
import static org.apache.dubbo.rpc.Constants.RETURN_KEY;

public class RpcContext {

    private static final InternalThreadLocal<RpcContext> LOCAL = new InternalThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    private static final InternalThreadLocal<RpcContext> SERVER_LOCAL = new InternalThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    private final Map<String, String> attachments = new HashMap<String, String>();
    private final Map<String, Object> values = new HashMap<String, Object>();

    private List<URL> urls;

    private URL url;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;

    private String remoteApplicationName;

    @Deprecated
    private List<Invoker<?>> invokers;
    @Deprecated
    private Invoker<?> invoker;
    @Deprecated
    private Invocation invocation;

    private Object request;
    private Object response;
    private AsyncContext asyncContext;


    protected RpcContext() {
    }

    public static RpcContext getServerContext() {
        return SERVER_LOCAL.get();
    }

    public static void restoreServerContext(RpcContext oldServerContext) {
        SERVER_LOCAL.set(oldServerContext);
    }

    public static void removeServerContext() {
        SERVER_LOCAL.remove();
    }

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    public static void restoreContext(RpcContext oldContext) {
        LOCAL.set(oldContext);
    }

    public static void removeContext() {
        LOCAL.remove();
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    @SuppressWarnings("unchecked")
    public <T> T getRequest(Class<T> clazz) {
        return (request != null && clazz.isAssignableFrom(request.getClass())) ? (T) request : null;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @SuppressWarnings("unchecked")
    public <T> T getResponse(Class<T> clazz) {
        return (response != null && clazz.isAssignableFrom(response.getClass())) ? (T) response : null;
    }

    public boolean isProviderSide() {
        return !isConsumerSide();
    }

    public boolean isConsumerSide() {
        return getUrl().getParameter(SIDE_KEY, PROVIDER_SIDE).equals(CONSUMER_SIDE);
    }

    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> getCompletableFuture() {
        return FutureContext.getContext().getCompletableFuture();
    }

    @SuppressWarnings("unchecked")
    public <T> Future<T> getFuture() {
        return FutureContext.getContext().getCompletableFuture();
    }

    public void setFuture(CompletableFuture<?> future) {
        FutureContext.getContext().setFuture(future);
    }

    public List<URL> getUrls() {
        return urls == null && url != null ? (List<URL>) Arrays.asList(url) : urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public RpcContext setLocalAddress(String host, int port) {
        if (port < 0) {
            port = 0;
        }
        this.localAddress = InetSocketAddress.createUnresolved(host, port);
        return this;
    }

    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    public RpcContext setLocalAddress(InetSocketAddress address) {
        this.localAddress = address;
        return this;
    }

    public String getLocalAddressString() {
        return getLocalHost() + ":" + getLocalPort();
    }

    public String getLocalHostName() {
        String host = localAddress == null ? null : localAddress.getHostName();
        if (StringUtils.isEmpty(host)) {
            return getLocalHost();
        }
        return host;
    }

    public RpcContext setRemoteAddress(String host, int port) {
        if (port < 0) {
            port = 0;
        }
        this.remoteAddress = InetSocketAddress.createUnresolved(host, port);
        return this;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public RpcContext setRemoteAddress(InetSocketAddress address) {
        this.remoteAddress = address;
        return this;
    }

    public String getRemoteApplicationName() {
        return remoteApplicationName;
    }

    public RpcContext setRemoteApplicationName(String remoteApplicationName) {
        this.remoteApplicationName = remoteApplicationName;
        return this;
    }

    public String getRemoteAddressString() {
        return getRemoteHost() + ":" + getRemotePort();
    }

    public String getRemoteHostName() {
        return remoteAddress == null ? null : remoteAddress.getHostName();
    }

    public String getLocalHost() {
        String host = localAddress == null ? null :
                localAddress.getAddress() == null ? localAddress.getHostName()
                        : NetUtils.filterLocalHost(localAddress.getAddress().getHostAddress());
        if (host == null || host.length() == 0) {
            return NetUtils.getLocalHost();
        }
        return host;
    }

    public int getLocalPort() {
        return localAddress == null ? 0 : localAddress.getPort();
    }

    public String getRemoteHost() {
        return remoteAddress == null ? null :
                remoteAddress.getAddress() == null ? remoteAddress.getHostName()
                        : NetUtils.filterLocalHost(remoteAddress.getAddress().getHostAddress());
    }

    public int getRemotePort() {
        return remoteAddress == null ? 0 : remoteAddress.getPort();
    }

    public String getAttachment(String key) {
        return attachments.get(key);
    }

    public RpcContext setAttachment(String key, String value) {
        if (value == null) {
            attachments.remove(key);
        } else {
            attachments.put(key, value);
        }
        return this;
    }

    public RpcContext removeAttachment(String key) {
        attachments.remove(key);
        return this;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public RpcContext setAttachments(Map<String, String> attachment) {
        this.attachments.clear();
        if (attachment != null && attachment.size() > 0) {
            this.attachments.putAll(attachment);
        }
        return this;
    }

    public void clearAttachments() {
        this.attachments.clear();
    }

    public Map<String, Object> get() {
        return values;
    }

    public RpcContext set(String key, Object value) {
        if (value == null) {
            values.remove(key);
        } else {
            values.put(key, value);
        }
        return this;
    }

    public RpcContext remove(String key) {
        values.remove(key);
        return this;
    }

    public Object get(String key) {
        return values.get(key);
    }

    @Deprecated
    public boolean isServerSide() {
        return isProviderSide();
    }

    @Deprecated
    public boolean isClientSide() {
        return isConsumerSide();
    }

    @Deprecated
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Invoker<?>> getInvokers() {
        return invokers == null && invoker != null ? (List) Arrays.asList(invoker) : invokers;
    }

    public RpcContext setInvokers(List<Invoker<?>> invokers) {
        this.invokers = invokers;
        if (CollectionUtils.isNotEmpty(invokers)) {
            List<URL> urls = new ArrayList<URL>(invokers.size());
            for (Invoker<?> invoker : invokers) {
                urls.add(invoker.getUrl());
            }
            setUrls(urls);
        }
        return this;
    }

    @Deprecated
    public Invoker<?> getInvoker() {
        return invoker;
    }

    public RpcContext setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
        if (invoker != null) {
            setUrl(invoker.getUrl());
        }
        return this;
    }

    @Deprecated
    public Invocation getInvocation() {
        return invocation;
    }

    public RpcContext setInvocation(Invocation invocation) {
        this.invocation = invocation;
        if (invocation != null) {
            setMethodName(invocation.getMethodName());
            setParameterTypes(invocation.getParameterTypes());
            setArguments(invocation.getArguments());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> asyncCall(Callable<T> callable) {
        try {
            try {
                setAttachment(ASYNC_KEY, Boolean.TRUE.toString());
                final T o = callable.call();
                //local invoke will return directly
                if (o != null) {
                    if (o instanceof CompletableFuture) {
                        return (CompletableFuture<T>) o;
                    }
                    return CompletableFuture.completedFuture(o);
                } else {
                    // The service has a normal sync method signature, should get future from RpcContext.
                }
            } catch (Exception e) {
                throw new RpcException(e);
            } finally {
                removeAttachment(ASYNC_KEY);
            }
        } catch (final RpcException e) {
            return new CompletableFuture<T>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public T get() throws InterruptedException, ExecutionException {
                    throw new ExecutionException(e.getCause());
                }

                @Override
                public T get(long timeout, TimeUnit unit)
                        throws InterruptedException, ExecutionException,
                        TimeoutException {
                    return get();
                }
            };
        }
        return ((CompletableFuture<T>) getContext().getFuture());
    }

    public void asyncCall(Runnable runnable) {
        try {
            setAttachment(RETURN_KEY, Boolean.FALSE.toString());
            runnable.run();
        } catch (Throwable e) {
            // FIXME should put exception in future?
            throw new RpcException("oneway call error ." + e.getMessage(), e);
        } finally {
            removeAttachment(RETURN_KEY);
        }
    }

    @SuppressWarnings("unchecked")
    public static AsyncContext startAsync() throws IllegalStateException {
        RpcContext currentContext = getContext();
        if (currentContext.asyncContext == null) {
            currentContext.asyncContext = new AsyncContextImpl();
        }
        currentContext.asyncContext.start();
        return currentContext.asyncContext;
    }

    protected void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public boolean isAsyncStarted() {
        if (this.asyncContext == null) {
            return false;
        }
        return asyncContext.isAsyncStarted();
    }

    public boolean stopAsync() {
        return asyncContext.stop();
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

}

