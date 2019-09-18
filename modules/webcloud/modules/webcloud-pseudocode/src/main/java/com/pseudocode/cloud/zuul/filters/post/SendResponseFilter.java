package com.pseudocode.cloud.zuul.filters.post;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletResponse;

import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.constants.ZuulHeaders;
import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.util.HTTPRequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;

import com.netflix.util.Pair;

//SendResponseFilter：是post阶段最后执行的过滤器。
//该过滤器会检查请求上下文中是否包含请求响应相关的头信息、响应数据流或是响应体，只有在包含它们其中一个的时候就会执行处理逻辑。
//而该过滤器的处理逻辑就是利用请求上下文的响应信息来组织需要发送回客户端的响应内容
public class SendResponseFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(SendResponseFilter.class);

    private boolean useServlet31 = true;
    private ZuulProperties zuulProperties;

    private ThreadLocal<byte[]> buffers;

    @Deprecated
    public SendResponseFilter() {
        this(new ZuulProperties());
    }

    public SendResponseFilter(ZuulProperties zuulProperties) {
        this.zuulProperties = zuulProperties;
        try {
            HttpServletResponse.class.getMethod("setContentLengthLong", long.class);
        } catch(NoSuchMethodException e) {
            useServlet31 = false;
        }
        buffers = ThreadLocal.withInitial(() -> new byte[zuulProperties.getInitialStreamBufferSize()]);
    }

    boolean isUseServlet31() {
        return useServlet31;
    }

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        return context.getThrowable() == null
                && (!context.getZuulResponseHeaders().isEmpty()
                || context.getResponseDataStream() != null
                || context.getResponseBody() != null);
    }

    @Override
    public Object run() {
        try {
            addResponseHeaders();
            writeResponse();
        }
        catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    private void writeResponse() throws Exception {
        RequestContext context = RequestContext.getCurrentContext();
        // there is no body to send
        if (context.getResponseBody() == null
                && context.getResponseDataStream() == null) {
            return;
        }
        HttpServletResponse servletResponse = context.getResponse();
        if (servletResponse.getCharacterEncoding() == null) { // only set if not set
            servletResponse.setCharacterEncoding("UTF-8");
        }

        OutputStream outStream = servletResponse.getOutputStream();
        InputStream is = null;
        try {
            if (context.getResponseBody() != null) {
                String body = context.getResponseBody();
                is = new ByteArrayInputStream(
                        body.getBytes(servletResponse.getCharacterEncoding()));
            }
            else {
                is = context.getResponseDataStream();
                if (is!=null && context.getResponseGZipped()) {
                    // if origin response is gzipped, and client has not requested gzip,
                    // decompress stream before sending to client
                    // else, stream gzip directly to client
                    if (isGzipRequested(context)) {
                        servletResponse.setHeader(ZuulHeaders.CONTENT_ENCODING, "gzip");
                    }
                    else {
                        is = handleGzipStream(is);
                    }
                }
            }

            if (is!=null) {
                writeResponse(is, outStream);
            }
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (Exception ex) {
                    log.warn("Error while closing upstream input stream", ex);
                }
            }

            try {
                Object zuulResponse = context.get("zuulResponse");
                if (zuulResponse instanceof Closeable) {
                    ((Closeable) zuulResponse).close();
                }
                outStream.flush();
                // The container will close the stream for us
            }
            catch (IOException ex) {
                log.warn("Error while sending response to client: " + ex.getMessage());
            }
        }
    }


    protected InputStream handleGzipStream(InputStream in) throws Exception {
        // Record bytes read during GZip initialization to allow to rewind the stream if needed
        //
        RecordingInputStream stream = new RecordingInputStream(in);
        try {
            return new GZIPInputStream(stream);
        }
        catch (java.util.zip.ZipException | java.io.EOFException ex) {

            if (stream.getBytesRead()==0) {
                // stream was empty, return the original "empty" stream
                return in;
            }
            else {
                // reset the stream and assume an unencoded response
                log.warn(
                        "gzip response expected but failed to read gzip headers, assuming unencoded response for request "
                                + RequestContext.getCurrentContext()
                                .getRequest().getRequestURL()
                                .toString());

                stream.reset();
                return stream;
            }
        }
        finally {
            stream.stopRecording();
        }
    }


    protected boolean isGzipRequested(RequestContext context) {
        final String requestEncoding = context.getRequest()
                .getHeader(ZuulHeaders.ACCEPT_ENCODING);

        return requestEncoding != null
                && HTTPRequestUtils.getInstance().isGzipped(requestEncoding);
    }


    private void writeResponse(InputStream zin, OutputStream out) throws Exception {
        byte[] bytes = buffers.get();
        int bytesRead = -1;
        while ((bytesRead = zin.read(bytes)) != -1) {
            out.write(bytes, 0, bytesRead);
        }
    }

    private void addResponseHeaders() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = context.getResponse();
        if (this.zuulProperties.isIncludeDebugHeader()) {
            @SuppressWarnings("unchecked")
            List<String> rd = (List<String>) context.get(ROUTING_DEBUG_KEY);
            if (rd != null) {
                StringBuilder debugHeader = new StringBuilder();
                for (String it : rd) {
                    debugHeader.append("[[[" + it + "]]]");
                }
                servletResponse.addHeader(X_ZUUL_DEBUG_HEADER, debugHeader.toString());
            }
        }
        List<Pair<String, String>> zuulResponseHeaders = context.getZuulResponseHeaders();
        if (zuulResponseHeaders != null) {
            for (Pair<String, String> it : zuulResponseHeaders) {
                servletResponse.addHeader(it.first(), it.second());
            }
        }
        if (includeContentLengthHeader(context)) {
            Long contentLength = context.getOriginContentLength();
            if(useServlet31) {
                servletResponse.setContentLengthLong(contentLength);
            } else {
                //Try and set some kind of content length if we can safely convert the Long to an int
                if (isLongSafe(contentLength)) {
                    servletResponse.setContentLength(contentLength.intValue());
                }
            }
        }
    }

    private boolean isLongSafe(long value) {
        return value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE;
    }

    protected boolean includeContentLengthHeader(RequestContext context) {
        // Not configured to forward the header
        if (!this.zuulProperties.isSetContentLength()) {
            return false;
        }

        // Only if Content-Length is provided
        if (context.getOriginContentLength() == null) {
            return false;
        }

        // If response is compressed, include header only if we are not about to decompress it
        if (context.getResponseGZipped()) {
            return context.isGzipRequested();
        }

        // Forward it in all other cases
        return true;
    }

    private static class RecordingInputStream extends InputStream {

        private InputStream delegate;
        private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        public RecordingInputStream(InputStream delegate) {
            super();
            this.delegate = Objects.requireNonNull(delegate);
        }

        @Override
        public int read() throws IOException {
            int read = delegate.read();

            if (buffer!=null && read!=-1) {
                buffer.write(read);
            }

            return read;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = delegate.read(b, off, len);

            if (buffer!=null && read!=-1) {
                buffer.write(b, off, read);
            }

            return read;
        }

        public void reset() {
            if (buffer==null) {
                throw new IllegalStateException("Stream is not recording");
            }

            this.delegate = new SequenceInputStream(new ByteArrayInputStream(buffer.toByteArray()), delegate);
            this.buffer = new ByteArrayOutputStream();
        }

        public int getBytesRead() {
            return (buffer==null)?-1:buffer.size();
        }

        public void stopRecording() {
            this.buffer = null;
        }

        @Override
        public void close() throws IOException {
            this.delegate.close();
        }
    }
}
