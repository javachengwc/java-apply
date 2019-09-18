package com.pseudocode.netflix.hystrix.core.strategy.concurrency;


import java.io.Closeable;

public class HystrixRequestContext implements Closeable {

    private static ThreadLocal<HystrixRequestContext> requestVariables = new ThreadLocal<HystrixRequestContext>();

    public static boolean isCurrentThreadInitialized() {
        HystrixRequestContext context = requestVariables.get();
        return context != null && context.state != null;
    }

    public static HystrixRequestContext getContextForCurrentThread() {
        HystrixRequestContext context = requestVariables.get();
        if (context != null && context.state != null) {
            // context.state can be null when context is not null
            // if a thread is being re-used and held a context previously, the context was shut down
            // but the thread was not cleared
            return context;
        } else {
            return null;
        }
    }

    public static void setContextOnCurrentThread(HystrixRequestContext state) {
        requestVariables.set(state);
    }

    public static HystrixRequestContext initializeContext() {
        HystrixRequestContext state = new HystrixRequestContext();
        requestVariables.set(state);
        return state;
    }

    private HystrixRequestContext() {

    }

    public void shutdown() {
        if (state != null) {
            for (HystrixRequestVariableDefault<?> v : state.keySet()) {
                // for each RequestVariable we call 'remove' which performs the shutdown logic
                try {
                    HystrixRequestVariableDefault.remove(this, v);
                } catch (Throwable t) {
                    HystrixRequestVariableDefault.logger.error("Error in shutdown, will continue with shutdown of other variables", t);
                }
            }
            // null out so it can be garbage collected even if the containing object is still
            // being held in ThreadLocals on threads that weren't cleaned up
            state = null;
        }
    }

    public void close() {
        shutdown();
    }

}

