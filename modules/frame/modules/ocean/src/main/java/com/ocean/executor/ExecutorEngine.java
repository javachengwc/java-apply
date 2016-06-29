package com.ocean.executor;

import com.google.common.util.concurrent.*;
import com.ocean.exception.ShardException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * 执行引擎
 */
public class ExecutorEngine {

    private static Logger logger = LoggerFactory.getLogger(ExecutorEngine.class);

    /**
     * 多线程执行任务
     */
    public static <I, O> List<O> execute(Collection<I> inputs, ExecuteUnit<I, O> executeUnit) {

        logger.info("ExecutorEngine execute...");
        ListenableFuture<List<O>> futures = submitFutures(inputs, executeUnit);
        addCallback(futures);
        return getFutureResults(futures);
    }

    /**
     * 多线程执行任务并归并结果
     * @param inputs 执行入参
     * @param executeUnit 执行单元
     * @param mergeUnit 合并结果单元
     * @param <I> 入参类型
     * @param <M> 中间结果类型
     * @param <O> 最终结果类型
     * @return 执行结果
     */
    public static <I, M, O> O execute(Collection<I> inputs, ExecuteUnit<I, M> executeUnit, MergeUnit<M, O> mergeUnit) {
        logger.info("ExecutorEngine execute..include merge.");
        return mergeUnit.merge(execute(inputs, executeUnit));
    }

    private static <I, O> ListenableFuture<List<O>> submitFutures(final Collection<I> inputs, final ExecuteUnit<I, O> executeUnit) {

        logger.info("ExecutorEngine submitFutures...");
        Set<ListenableFuture<O>> result = new HashSet<ListenableFuture<O>>(inputs.size());
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(inputs.size()));
        for (final I each : inputs) {
            result.add(service.submit(new Callable<O>() {
                public O call() throws Exception {
                    logger.info("ExecutorEngine 多线程异步执行sql task");
                    return executeUnit.execute(each);
                }
            }));
        }
        service.shutdown();
        return Futures.allAsList(result);
    }

    private static <T> void addCallback(ListenableFuture<T> allFutures) {
        Futures.addCallback(allFutures, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                logger.info("ExecutorEngine execute result success,", result);
            }
            @Override
            public void onFailure(Throwable thrown) {
                logger.error("ExecutorEngine execute result error,", thrown);
            }
        });
    }

    private static <O> O getFutureResults(ListenableFuture<O> futures) {
        try {
            return futures.get();
        } catch (Exception ex ) {
            logger.error("ExecutorEngine getFutureResults error,",ex);
            throw new ShardException(ex);
        }
    }
}

