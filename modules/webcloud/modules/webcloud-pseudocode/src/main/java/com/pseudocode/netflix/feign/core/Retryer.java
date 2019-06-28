package com.pseudocode.netflix.feign.core;

import static java.util.concurrent.TimeUnit.SECONDS;

public interface Retryer extends Cloneable {

    void continueOrPropagate(RetryableException e);

    Retryer clone();

    public static class Default implements Retryer {

        private final int maxAttempts;
        private final long period;
        private final long maxPeriod;
        int attempt;
        long sleptForMillis;

        public Default() {
            this(100, SECONDS.toMillis(1), 5);
        }

        public Default(long period, long maxPeriod, int maxAttempts) {
            this.period = period;
            this.maxPeriod = maxPeriod;
            this.maxAttempts = maxAttempts;
            this.attempt = 1;
        }

        // visible for testing;
        protected long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        public void continueOrPropagate(RetryableException e) {
            if (attempt++ >= maxAttempts) {
                throw e;
            }

            long interval;
            if (e.retryAfter() != null) {
                interval = e.retryAfter().getTime() - currentTimeMillis();
                if (interval > maxPeriod) {
                    interval = maxPeriod;
                }
                if (interval < 0) {
                    return;
                }
            } else {
                interval = nextMaxInterval();
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            sleptForMillis += interval;
        }

        long nextMaxInterval() {
            long interval = (long) (period * Math.pow(1.5, attempt - 1));
            return interval > maxPeriod ? maxPeriod : interval;
        }

        @Override
        public Retryer clone() {
            return new Default(period, maxPeriod, maxAttempts);
        }
    }

    Retryer NEVER_RETRY = new Retryer() {

        @Override
        public void continueOrPropagate(RetryableException e) {
            throw e;
        }

        @Override
        public Retryer clone() {
            return this;
        }
    };
}

