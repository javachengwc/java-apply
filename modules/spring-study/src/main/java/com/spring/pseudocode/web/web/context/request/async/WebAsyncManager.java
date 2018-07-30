package com.spring.pseudocode.web.web.context.request.async;

import com.spring.pseudocode.core.core.task.AsyncTaskExecutor;
import com.spring.pseudocode.core.core.task.SimpleAsyncTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

public final class WebAsyncManager
{
    private static final Object RESULT_NONE = new Object();

    private static final Logger logger = LoggerFactory.getLogger(WebAsyncManager.class);

    private AsyncWebRequest asyncWebRequest;

    private AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(getClass().getSimpleName());

    private Object concurrentResult = RESULT_NONE;

    private Object[] concurrentResultContext;

    public void setAsyncWebRequest(AsyncWebRequest asyncWebRequest)
    {
        this.asyncWebRequest = asyncWebRequest;
        //.................
    }

    public void setTaskExecutor(AsyncTaskExecutor taskExecutor)
    {
        this.taskExecutor = taskExecutor;
    }

    public boolean isConcurrentHandlingStarted()
    {
        return (this.asyncWebRequest != null) && (this.asyncWebRequest.isAsyncStarted());
    }

    public boolean hasConcurrentResult()
    {
        return this.concurrentResult != RESULT_NONE;
    }

    public Object getConcurrentResult()
    {
        return this.concurrentResult;
    }

    public Object[] getConcurrentResultContext()
    {
        return this.concurrentResultContext;
    }

    public void startCallableProcessing(Callable<?> callable, Object[] processingContext) throws Exception
    {
        startCallableProcessing(new WebAsyncTask(callable), processingContext);
    }

    public void startCallableProcessing(WebAsyncTask<?> webAsyncTask, Object[] processingContext) throws Exception
    {
        Long timeout = webAsyncTask.getTimeout();
        if (timeout != null) {
            this.asyncWebRequest.setTimeout(timeout);
        }

        AsyncTaskExecutor executor = webAsyncTask.getExecutor();
        if (executor != null) {
            this.taskExecutor = executor;
        }
        //.........

        Callable callable = webAsyncTask.getCallable();
        //this.asyncWebRequest.addTimeoutHandler(new Object(interceptorChain, callable)
        //this.asyncWebRequest.addCompletionHandler(new Runnable(interceptorChain, callable)

        startAsyncProcessing(processingContext);
        try {

            //提交执行任务
//            this.taskExecutor.submit(new Object(interceptorChain, callable)
//            {
//                public void run() {
//                    Object result = null;
//                    try {
//                        this.val$interceptorChain.applyPreProcess(WebAsyncManager.this.asyncWebRequest, this.val$callable);
//                        result = this.val$callable.call();
//                    }catch (Throwable ex) {
//                        result = ex;
//                    }finally {
//                        result = this.val$interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, this.val$callable, result);
//                    }
//
//                }
//            } );
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public void startDeferredResultProcessing(DeferredResult<?> deferredResult, Object[] processingContext) throws Exception
    {
//        Long timeout = deferredResult.getTimeoutValue();
//        if (timeout != null) {
//            this.asyncWebRequest.setTimeout(timeout);
//        }

        //DeferredResultInterceptorChain interceptorChain = new DeferredResultInterceptorChain(interceptors);
        //this.asyncWebRequest.addTimeoutHandler(new Runnable(interceptorChain, deferredResult)
        //this.asyncWebRequest.addCompletionHandler(new Runnable(interceptorChain, deferredResult)

        startAsyncProcessing(processingContext);
        try
        {
//            interceptorChain.applyPreProcess(this.asyncWebRequest, deferredResult);
//            deferredResult.setResultHandler(new DeferredResult.DeferredResultHandler(interceptorChain, deferredResult) {
//                public void handleResult(Object result) {
//                    result = this.val$interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, this.val$deferredResult, result);
//                    WebAsyncManager.this.setConcurrentResultAndDispatch(result);
//                }
//            } );
        }
        catch (Throwable ex) {
            //setConcurrentResultAndDispatch(ex);
        }
    }

    private void startAsyncProcessing(Object[] processingContext) {
        this.concurrentResultContext = processingContext;
        this.asyncWebRequest.startAsync();
    }
}
