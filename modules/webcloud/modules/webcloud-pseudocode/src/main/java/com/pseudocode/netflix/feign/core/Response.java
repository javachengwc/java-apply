package com.pseudocode.netflix.feign.core;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public final class Response implements Closeable {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private final int status;
    private final String reason;
    private final Map<String, Collection<String>> headers;
    private final Body body;
    private final Request request;

    private Response(Builder builder) {
        //checkState(builder.status >= 200, "Invalid status code: %s", builder.status);
        this.status = builder.status;
        this.reason = builder.reason; //nullable
        this.headers = Collections.unmodifiableMap(caseInsensitiveCopyOf(builder.headers));
        this.body = builder.body; //nullable
        this.request = builder.request; //nullable
    }

    public Builder toBuilder(){
        return new Builder(this);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        int status;
        String reason;
        Map<String, Collection<String>> headers;
        Body body;
        Request request;

        Builder() {
        }

        Builder(Response source) {
            this.status = source.status;
            this.reason = source.reason;
            this.headers = source.headers;
            this.body = source.body;
            this.request = source.request;
        }

        /** @see Response#status*/
        public Builder status(int status) {
            this.status = status;
            return this;
        }

        /** @see Response#reason */
        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        /** @see Response#headers */
        public Builder headers(Map<String, Collection<String>> headers) {
            this.headers = headers;
            return this;
        }

        /** @see Response#body */
        public Builder body(Body body) {
            this.body = body;
            return this;
        }

        /** @see Response#body */
        public Builder body(InputStream inputStream, Integer length) {
            this.body = InputStreamBody.orNull(inputStream, length);
            return this;
        }

        /** @see Response#body */
        public Builder body(byte[] data) {
            this.body = ByteArrayBody.orNull(data);
            return this;
        }

        /** @see Response#body */
        public Builder body(String text, Charset charset) {
            this.body = ByteArrayBody.orNull(text, charset);
            return this;
        }

        /** @see Response#request */
        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    public int status() {
        return status;
    }

    public String reason() {
        return reason;
    }

    public Map<String, Collection<String>> headers() {
        return headers;
    }

    public Body body() {
        return body;
    }

    public Request request() {
        return request;
    }

    @Override
    public void close() {
        //Util.ensureClosed(body);
    }

    public interface Body extends Closeable {

        Integer length();

        boolean isRepeatable();

        InputStream asInputStream() throws IOException;

        Reader asReader() throws IOException;
    }

    private static final class InputStreamBody implements Response.Body {

        private final InputStream inputStream;
        private final Integer length;
        private InputStreamBody(InputStream inputStream, Integer length) {
            this.inputStream = inputStream;
            this.length = length;
        }

        private static Body orNull(InputStream inputStream, Integer length) {
            if (inputStream == null) {
                return null;
            }
            return new InputStreamBody(inputStream, length);
        }

        @Override
        public Integer length() {
            return length;
        }

        @Override
        public boolean isRepeatable() {
            return false;
        }

        @Override
        public InputStream asInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public Reader asReader() throws IOException {
            return new InputStreamReader(inputStream, UTF_8);
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }
    }

    private static final class ByteArrayBody implements Response.Body {

        private final byte[] data;

        public ByteArrayBody(byte[] data) {
            this.data = data;
        }

        private static Body orNull(byte[] data) {
            if (data == null) {
                return null;
            }
            return new ByteArrayBody(data);
        }

        private static Body orNull(String text, Charset charset) {
            if (text == null) {
                return null;
            }
           // checkNotNull(charset, "charset");
            return new ByteArrayBody(text.getBytes(charset));
        }

        @Override
        public Integer length() {
            return data.length;
        }

        @Override
        public boolean isRepeatable() {
            return true;
        }

        @Override
        public InputStream asInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        @Override
        public Reader asReader() throws IOException {
            return new InputStreamReader(asInputStream(), UTF_8);
        }

        @Override
        public void close() throws IOException {
        }

    }

    private static Map<String, Collection<String>> caseInsensitiveCopyOf(Map<String, Collection<String>> headers) {
        Map<String, Collection<String>> result = new TreeMap<String, Collection<String>>(String.CASE_INSENSITIVE_ORDER);

        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            if (!result.containsKey(headerName)) {
                result.put(headerName.toLowerCase(Locale.ROOT), new LinkedList<String>());
            }
            result.get(headerName).addAll(entry.getValue());
        }
        return result;
    }
}

