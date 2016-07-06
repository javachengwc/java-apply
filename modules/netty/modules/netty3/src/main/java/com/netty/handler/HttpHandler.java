package com.netty.handler;

import com.alibaba.fastjson.JSON;
import com.netty.vo.BaseResp;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpHandler extends SimpleChannelUpstreamHandler {

    private static Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    public static final AtomicLong httpTimes = new AtomicLong(0);

    public HttpHandler()
    {
        logger.info("-----------------------HttpHandler create------------------");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        httpTimes.incrementAndGet();
        long begin =System.currentTimeMillis();
        final HttpRequest request = (HttpRequest) e.getMessage();
        final String uri = request.getUri();
        final HttpMethod method = request.getMethod();
        HttpResponse response = null;
        Throwable ex = null;

        long time = System.currentTimeMillis();
        if (!method.equals(HttpMethod.POST)) {
            ex = new Exception(method + " not supported");
            response = getResp(new BaseResp(-1, ex.getMessage()));
        } else if ("/deal".equals(uri)) {
            String content = request.getContent().toString(CharsetUtil.UTF_8);
            try {
                long now =System.currentTimeMillis();
                //具体的处理服务
                BaseResp respInfo = DealHandler.handle(content);
                response = getResp(respInfo);
            } catch (Throwable t) {
                ex = t;
                response = getResp(new BaseResp(-1, "系统错误: " + t.getMessage()));
            }
        } else {
            ex = new Exception(uri + " not found");
            response = getResp(new BaseResp(-1, ex.getMessage()));
        }
        long now =System.currentTimeMillis();

        response.setProtocolVersion(request.getProtocolVersion());
        final int bytes = response.getContent().readableBytes();

        String accessInfo = "done " + httpRequest2String(request, ctx.getChannel())
                + ' ' + "sent:" + bytes
                + ' ' + simpleString4E(ex)
                + " latency:" + (System.currentTimeMillis() - time);

        if (HttpHeaders.isKeepAlive(request)) {
            response.setHeader(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            e.getChannel().write(response);
        } else {
            response.setHeader(CONNECTION, HttpHeaders.Values.CLOSE);
            e.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private String simpleString4E(Throwable se) {
        if (se == null) {
            return "NO_EXCEP";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(se.getClass().getName()).append(": ").append(se.getMessage()).append(", ");

        if (se.getStackTrace().length > 0) {
            final StackTraceElement ele = se.getStackTrace()[0];
            sb.append(ele.getMethodName()).append("(");
            sb.append(ele.getFileName()).append(":").append(ele.getLineNumber()).append(")");
        }

        return sb.toString();
    }

    private String httpRequest2String(HttpRequest request, Channel channel) {
        StringBuilder buf = new StringBuilder(128);
        final String forwardIP = request.getHeader("X-Forwarded-For");
        if (forwardIP != null) {
            buf.append("native-ip:").append(forwardIP).append(' ');
        }
        buf.append("last-ip:").append(channel.getRemoteAddress()).append(' ');
        buf.append(request.getMethod().toString());
        buf.append(' ');
        buf.append(request.getUri());
        buf.append(' ');
        buf.append(request.getProtocolVersion().getText());

        return buf.toString();
    }

    private HttpResponse getResp(BaseResp resp) {

        String respJson = JSON.toJSONString(resp);
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        response.setHeader(CONTENT_TYPE, "application/json");
        response.setContent(ChannelBuffers.wrappedBuffer(respJson.getBytes()));
        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        logger.error("exeception caught: " + simpleString4E(e.getCause()));
        e.getChannel().close();
    }
}
