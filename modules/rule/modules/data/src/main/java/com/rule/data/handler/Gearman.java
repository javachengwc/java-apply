package com.rule.data.handler;

import com.alibaba.fastjson.JSON;
import com.rule.data.exception.RengineException;
import com.rule.data.service.core.LogMonitor;
import com.rule.data.service.core.ProcessInfos;
import com.rule.data.util.ConfigUtil;
import com.rule.data.util.LogUtil;
import com.rule.data.util.RequestIpUtil;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 服务调度
 */
public class Gearman extends SimpleChannelUpstreamHandler {

    private static final int THREAD_COUNT = ConfigUtil.getThreadCount();

    private static final boolean call_log = ConfigUtil.getCallLog();

    public static final ThreadPoolExecutor executor4G = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());

    public static final AtomicLong httpTimes = new AtomicLong(0);

    private static final int fileThreshold = ConfigUtil.getFileThreshold();

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        executor4G.submit(new RengineRun(ctx, e));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

        LogUtil.error("exception caught: " + simpleString4E(e.getCause()));
        e.getChannel().close();
    }

    public static class RengineRun implements Runnable {

        private final ChannelHandlerContext ctx;

        private final MessageEvent e;

        RengineRun(ChannelHandlerContext ctx, MessageEvent e) {

            this.ctx = ctx;
            this.e = e;
        }

        /**
         * 判断HTTP请求是否是POST, 且对应uri是否有handler可以处理
         * 如果有异常, 如果发现是RengineException, 将异常入库
         * 文件大小过大, 也会告警入库
         */
        @Override
        public void run() {

            httpTimes.incrementAndGet();

            final HttpRequest request = (HttpRequest) e.getMessage();

            final String uri = request.getUri();

            final HttpMethod method = request.getMethod();

            String ip = httpRequest2String(request, ctx.getChannel());

            String ruleReferer = request.getHeader("ruleReferer"); //自定义header

            String content = null;

            HttpResponse response = null;

            Throwable ex = null;

            //不支持的请求方式
            if (!method.equals(HttpMethod.POST) && !method.equals(HttpMethod.GET)) {

                ex = new Exception(method + " not supported");
                response = getError(ex.getMessage());

            } else {


                Handler handler = Handlers.getHandler(uri);

                //找不到请求处理器
                if (handler == null) {

                    ex = new Exception(uri + " not found");
                    response = getError(ex.getMessage());

                } else {

                    content = request.getContent().toString(CharsetUtil.UTF_8);
                    if (call_log && "/service".equals(uri))
                    {
                         //记录调用日志
                         LogMonitor.callLog(ip, content);
                    }

                    try {

                        //请求处理
                        response = handler.handleReq(content);
                    } catch (Throwable t) {
                        ex = t;
                        response = getError(t.getMessage());
                    } finally {
                        ProcessInfos.clear();
                    }
                }
            }

            response.setProtocolVersion(request.getProtocolVersion());

            final int bytes = response.getContent().readableBytes();

            final String accessInfo = httpRequest2String(request, ctx.getChannel())  + ' ' + "sent:" + bytes + ' ' + simpleString4E(ex);

            if (ex != null) {
                //记录异常
                LogUtil.error(accessInfo);

                if (ex instanceof RengineException) {

                    RengineException re = (RengineException) ex;
                    LogMonitor.error(re.getServiceName(), ip + ";" + re.getMessage()+";ruleReferer:"+ruleReferer);
                }
            } else {
                LogUtil.info(accessInfo);
            }

            if (bytes > fileThreshold) {
                LogMonitor.error("告警: 数据过大", "返回数据大小超过阀值(" + fileThreshold + "), " + bytes + ", 请求内容:" + content);
            }

            response.setHeader(HttpHeaders.Names.SERVER, "MWS/1.0");

            response.setHeader(CONTENT_LENGTH, bytes);

            String contentType = response.getHeader(HttpHeaders.Names.CONTENT_TYPE);
            if (contentType == null) {
                contentType = "text/html";
            }

            response.setHeader(HttpHeaders.Names.CONTENT_TYPE, contentType + "; charset=utf8");

            if (HttpHeaders.isKeepAlive(request)) {
                response.setHeader(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                e.getChannel().write(response);
            } else {
                response.setHeader(CONNECTION, HttpHeaders.Values.CLOSE);
                e.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
            }

        }

    }

    public static String simpleString4E(Throwable se) {
        if (se == null) {
            return "NO_EXCEP";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(se.getMessage()).append("]");

        return sb.toString();
    }

    static String httpRequest2String(HttpRequest request, Channel channel) {

        StringBuilder buf = new StringBuilder(128);

        String forwardIP = RequestIpUtil.getNativeIp(request);
        if (forwardIP != null) {
            buf.append("native-ip:").append(forwardIP).append(' ');
        }

        String forwardIP4Http = RequestIpUtil.getNativeIpByHttp(request);
        if(forwardIP4Http!=null){
            buf.append("native-ip2:").append(forwardIP4Http).append(' ');
        }

        buf.append("last-ip:").append(channel.getRemoteAddress()).append(' ');
        buf.append(request.getMethod().toString());
        buf.append(' ');
        buf.append(request.getUri());
        buf.append(' ');
        buf.append(request.getProtocolVersion().getText());

        return buf.toString();
    }


    static HttpResponse getError(String description) {

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);

        response.setHeader(CONTENT_TYPE, "application/json");

        response.setContent(ChannelBuffers.wrappedBuffer(JSON.toJSONString(new BaseRespInfo("-1", description)).getBytes()));

        return response;
    }

}
