package com.pseudocode.netflix.feign.core.codec;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.pseudocode.netflix.feign.core.FeignException;
import com.pseudocode.netflix.feign.core.Response;
import com.pseudocode.netflix.feign.core.RetryableException;

import static com.pseudocode.netflix.feign.core.FeignException.errorStatus;
import static com.pseudocode.netflix.feign.core.Util.RETRY_AFTER;
import static com.pseudocode.netflix.feign.core.Util.checkNotNull;
import static java.util.Locale.US;
import static java.util.concurrent.TimeUnit.SECONDS;

public interface ErrorDecoder {

    public Exception decode(String methodKey, Response response);

    public static class Default implements ErrorDecoder {

        private final RetryAfterDecoder retryAfterDecoder = new RetryAfterDecoder();

        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = errorStatus(methodKey, response);
            Date retryAfter = retryAfterDecoder.apply(firstOrNull(response.headers(), RETRY_AFTER));
            if (retryAfter != null) {
                return new RetryableException(exception.getMessage(), exception, retryAfter);
            }
            return exception;
        }

        private <T> T firstOrNull(Map<String, Collection<T>> map, String key) {
            if (map.containsKey(key) && !map.get(key).isEmpty()) {
                return map.get(key).iterator().next();
            }
            return null;
        }
    }

    static class RetryAfterDecoder {

        static final DateFormat RFC822_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", US);
        private final DateFormat rfc822Format;

        RetryAfterDecoder() {
            this(RFC822_FORMAT);
        }

        RetryAfterDecoder(DateFormat rfc822Format) {
            this.rfc822Format = checkNotNull(rfc822Format, "rfc822Format");
        }

        protected long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        public Date apply(String retryAfter) {
            if (retryAfter == null) {
                return null;
            }
            if (retryAfter.matches("^[0-9]+$")) {
                long deltaMillis = SECONDS.toMillis(Long.parseLong(retryAfter));
                return new Date(currentTimeMillis() + deltaMillis);
            }
            synchronized (rfc822Format) {
                try {
                    return rfc822Format.parse(retryAfter);
                } catch (ParseException ignored) {
                    return null;
                }
            }
        }
    }
}

