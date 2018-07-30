package com.spring.pseudocode.web.web.context.request.async;


import com.spring.pseudocode.beans.factory.BeanFactory;
import com.spring.pseudocode.beans.factory.BeanFactoryAware;
import com.spring.pseudocode.core.core.task.AsyncTaskExecutor;
import com.spring.pseudocode.web.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter;

import java.util.concurrent.Callable;

public class WebAsyncTask<V> implements BeanFactoryAware
{
    private final Callable<V> callable;

    private Long timeout;

    private AsyncTaskExecutor executor;

    private String executorName;

    private BeanFactory beanFactory;

    private Callable<V> timeoutCallback;

    private Runnable completionCallback;

    public WebAsyncTask(Callable<V> callable)
    {
        this.callable = callable;
    }

    public WebAsyncTask(long timeout, Callable<V> callable)
    {
        this(callable);
        this.timeout = timeout;
    }

    public WebAsyncTask(Long timeout, String executorName, Callable<V> callable)
    {
        this(callable);
        this.executorName = executorName;
        this.timeout = timeout;
    }

    public WebAsyncTask(Long timeout, AsyncTaskExecutor executor, Callable<V> callable)
    {
        this(callable);
        this.executor = executor;
        this.timeout = timeout;
    }

    public Callable<?> getCallable()
    {
        return this.callable;
    }

    public Long getTimeout()
    {
        return this.timeout;
    }

    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }

    public AsyncTaskExecutor getExecutor()
    {
        if (this.executor != null) {
            return this.executor;
        }
        if (this.executorName != null) {
            return (AsyncTaskExecutor)this.beanFactory.getBean(this.executorName, AsyncTaskExecutor.class);
        }
        return null;
    }

    public void onTimeout(Callable<V> callback)
    {
        this.timeoutCallback = callback;
    }

    public void onCompletion(Runnable callback)
    {
        this.completionCallback = callback;
    }

    CallableProcessingInterceptor getInterceptor() {
        return new CallableProcessingInterceptorAdapter()
        {
            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
                return WebAsyncTask.this.timeoutCallback != null ? WebAsyncTask.this.timeoutCallback.call() : CallableProcessingInterceptor.RESULT_NONE;
            }

            public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {
                if (WebAsyncTask.this.completionCallback != null)
                    WebAsyncTask.this.completionCallback.run();
            }
        };
    }
}
