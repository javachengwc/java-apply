package com.pseudocode.netflix.feign.core;

import java.io.IOException;
import static com.pseudocode.netflix.feign.core.Util.valuesOrEmpty;

public abstract class Logger {

    protected static String methodTag(String configKey) {
        return new StringBuilder().append('[').append(configKey.substring(0, configKey.indexOf('('))).append("] ").toString();
    }

    protected abstract void log(String configKey, String format, Object... args);

    protected void logRequest(String configKey, Level logLevel, Request request) {
        log(configKey, "---> %s %s HTTP/1.1", request.method(), request.url());
        if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

            for (String field : request.headers().keySet()) {
                for (String value : valuesOrEmpty(request.headers(), field)) {
                    log(configKey, "%s: %s", field, value);
                }
            }

            int bodyLength = 0;
            if (request.body() != null) {
                bodyLength = request.body().length;
                if (logLevel.ordinal() >= Level.FULL.ordinal()) {
                    String
                            bodyText =
                            request.charset() != null ? new String(request.body(), request.charset()) : null;
                    log(configKey, ""); // CRLF
                    log(configKey, "%s", bodyText != null ? bodyText : "Binary data");
                }
            }
            log(configKey, "---> END HTTP (%s-byte body)", bodyLength);
        }
    }

    protected void logRetry(String configKey, Level logLevel) {
        log(configKey, "---> RETRYING");
    }

    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        return response;
    }

    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        log(configKey, "<--- ERROR %s: %s (%sms)", ioe.getClass().getSimpleName(), ioe.getMessage(), elapsedTime);
        return ioe;
    }

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        FULL
    }

    public static class ErrorLogger extends Logger {
        @Override
        protected void log(String configKey, String format, Object... args) {
            System.err.printf(methodTag(configKey) + format + "%n", args);
        }
    }

    public static class JavaLogger extends Logger {

        protected void logRequest(String configKey, Level logLevel, Request request) {
        }

        protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
            return response;
        }
        protected void log(String configKey, String format, Object... args) {
        }

        public JavaLogger appendToFile(String logfile) {
            return this;
        }
    }

    public static class NoOpLogger extends Logger {

        protected void logRequest(String configKey, Level logLevel, Request request) {
        }

        protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
            return response;
        }

        protected void log(String configKey, String format, Object... args) {
        }
    }
}

