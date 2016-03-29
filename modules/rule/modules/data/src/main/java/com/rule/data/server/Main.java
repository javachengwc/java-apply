package com.rule.data.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rule.data.handler.Gearman;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.LogMonitor;
import com.rule.data.service.core.Services;
import com.rule.data.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 程序入口
 */
public class Main {

    static {

        JSON.DEFAULT_GENERATE_FEATURE = JSON.DEFAULT_GENERATE_FEATURE | SerializerFeature.BrowserCompatible.getMask();
    }

    private static final ExecutorService executor4Boss = Executors.newCachedThreadPool();
    private static final ExecutorService executor4Worker = Executors.newCachedThreadPool();
    public static final Date LAUCH_TIME = new Date();

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
        Services.getService("");
        LogMonitor.start();

        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(executor4Boss, executor4Worker));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpChunkAggregator(6400*1024)); // 6400KB
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("handler", new Gearman());
                return pipeline;
            }
        });

        startMonitor(args);
        try {
            bootstrap.bind(new InetSocketAddress(Integer.valueOf(args[0])));
            System.out.println("succeed bind, listen on " + args[0]);
        } catch (Throwable e) {
            LogUtil.info("", e);
            System.out.println("bind error, will exit. " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * 启动状态监测
     */
    private static void startMonitor(String[] args) {
        try {
            int port = Integer.parseInt(args[1]);
            ServerBootstrap bootstrap = new ServerBootstrap(  new NioServerSocketChannelFactory(executor4Boss, executor4Worker));

            bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                @Override
                public ChannelPipeline getPipeline() throws Exception {
                    ChannelPipeline pipeline = Channels.pipeline();

                    pipeline.addLast("handler", new SimpleChannelUpstreamHandler() {
                        long complete = -1;

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
                            e.getChannel().close();
                        }

                        @Override
                        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

                            String msg =((ChannelBuffer) e.getMessage()).toString(Charset.forName("utf8"));

                            if (!StringUtils.isBlank(msg) && msg.startsWith("quit"))
                            {
                                e.getChannel().write(ChannelBuffers.wrappedBuffer("will exit ...\r\n".getBytes())).addListener(ChannelFutureListener.CLOSE);
                                return;
                            }

                            final String lineS = "\r\n";
                            StringBuilder sb = new StringBuilder(lineS);
                            final ThreadPoolExecutor e4G = Gearman.executor4G;
                            final int activeCount = e4G.getActiveCount();
                            final int waiting = e4G.getQueue().size();
                            final long completed = e4G.getCompletedTaskCount();

                            sb.append(DataUtil.date2String(new Date())).append(lineS);
                            sb.append("task        running: ").append(activeCount).append(lineS);
                            sb.append("task        waiting: ").append(waiting).append(lineS);
                            if (complete != -1) {
                                sb.append("task completed just: ").append(completed - complete).append(lineS);
                            } else {
                                sb.append("task completed just: ").append(0).append(lineS);
                            }

                            sb.append("task completed  all: ").append(completed).append(lineS);

                            sb.append("thread        count: ").append(e4G.getPoolSize()).append(lineS);
                            e.getChannel().write(ChannelBuffers.wrappedBuffer(sb.toString().getBytes()));

                            complete = completed;
                        }

                        @Override
                        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
                            long dif = (System.currentTimeMillis() - LAUCH_TIME.getTime()) / 1000;
                            e.getChannel().write(ChannelBuffers.wrappedBuffer((
                                              " ------------------------------\r\n"
                                            + " |        Server Stat         |\r\n"
                                            + " ------------------------------\r\n"
                                            + "lauch time: "
                                            + DataUtil.date2String(LAUCH_TIME)
                                            + ", elapsed: "
                                            + (dif / (3600 * 24)) + "d "
                                            + (dif % (3600 * 24) / 3600) + "h "
                                            + (dif % (3600 * 24) % 3600 / 60) + "m"
                                            + ", http-req: " + Gearman.httpTimes.get() + "\r\n"
                                            + "memory free: " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M/"
                                            + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M\r\n").getBytes()));
                        }
                    });
                    return pipeline;
                }
            });

            bootstrap.bind(new InetSocketAddress(port));
            System.out.println("succeed monitor, listen on " + port);
        } catch (Exception e) {

            LogUtil.info("", e);
            System.out.println("no monitor");
        }
    }
}
