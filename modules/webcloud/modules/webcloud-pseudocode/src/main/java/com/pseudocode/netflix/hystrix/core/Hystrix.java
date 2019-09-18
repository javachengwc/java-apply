package com.pseudocode.netflix.hystrix.core;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.functions.Action0;

public class Hystrix {

    private static final Logger logger = LoggerFactory.getLogger(Hystrix.class);

    public static void reset() {
        // shutdown thread-pools
        HystrixThreadPool.Factory.shutdown();
        _reset();
    }

    public static void reset(long time, TimeUnit unit) {
        // shutdown thread-pools
        HystrixThreadPool.Factory.shutdown(time, unit);
        _reset();
    }

    private static void _reset() {
        // clear metrics
        HystrixCommandMetrics.reset();
        HystrixThreadPoolMetrics.reset();
        HystrixCollapserMetrics.reset();
        // clear collapsers
        HystrixCollapser.reset();
        // clear circuit breakers
        HystrixCircuitBreaker.Factory.reset();
        HystrixPlugins.reset();
        HystrixPropertiesFactory.reset();
        currentCommand.set(new ConcurrentStack<HystrixCommandKey>());
    }

    private static ThreadLocal<ConcurrentStack<HystrixCommandKey>> currentCommand = new ThreadLocal<ConcurrentStack<HystrixCommandKey>>() {
        @Override
        protected ConcurrentStack<HystrixCommandKey> initialValue() {
            return new ConcurrentStack<HystrixCommandKey>();
        }
    };

    public static HystrixCommandKey getCurrentThreadExecutingCommand() {
        if (currentCommand == null) {
            // statics do "interesting" things across classloaders apparently so this can somehow be null ...
            return null;
        }
        return currentCommand.get().peek();
    }

    static Action0 startCurrentThreadExecutingCommand(HystrixCommandKey key) {
        final ConcurrentStack<HystrixCommandKey> list = currentCommand.get();
        try {
            list.push(key);
        } catch (Exception e) {
            logger.warn("Unable to record command starting", e);
        }
        return new Action0() {

            @Override
            public void call() {
                endCurrentThreadExecutingCommand(list);
            }

        };
    }

    static void endCurrentThreadExecutingCommand() {
        endCurrentThreadExecutingCommand(currentCommand.get());
    }

    private static void endCurrentThreadExecutingCommand(ConcurrentStack<HystrixCommandKey> list) {
        try {
            if (!list.isEmpty()) {
                list.pop();
            }
        } catch (NoSuchElementException e) {
            // this shouldn't be possible since we check for empty above and this is thread-isolated
            logger.debug("No command found to end.", e);
        } catch (Exception e) {
            logger.warn("Unable to end command.", e);
        }
    }

    private static class ConcurrentStack<E> {
        AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

        public void push(E item) {
            Node<E> newHead = new Node<E>(item);
            Node<E> oldHead;
            do {
                oldHead = top.get();
                newHead.next = oldHead;
            } while (!top.compareAndSet(oldHead, newHead));
        }

        public E pop() {
            Node<E> oldHead;
            Node<E> newHead;
            do {
                oldHead = top.get();
                if (oldHead == null) {
                    return null;
                }
                newHead = oldHead.next;
            } while (!top.compareAndSet(oldHead, newHead));
            return oldHead.item;
        }

        public boolean isEmpty() {
            return top.get() == null;
        }

        public int size() {
            int currentSize = 0;
            Node<E> current = top.get();
            while (current != null) {
                currentSize++;
                current = current.next;
            }
            return currentSize;
        }

        public E peek() {
            Node<E> eNode = top.get();
            if (eNode == null) {
                return null;
            } else {
                return eNode.item;
            }
        }

        private class Node<E> {
            public final E item;
            public Node<E> next;

            public Node(E item) {
                this.item = item;
            }
        }
    }
}
