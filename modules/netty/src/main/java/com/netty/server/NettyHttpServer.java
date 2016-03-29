package com.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.netty.handler.HttpHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;

public class NettyHttpServer {

    private static Logger logger = LoggerFactory.getLogger(NettyHttpServer.class);

    public static int port=5000;

    public static void main(String[] args) {

        final ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory( Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("aggregator", new HttpChunkAggregator(655360)); // 640KB
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("ws-header", new SimpleChannelDownstreamHandler() {
                    @Override
                    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
                        if (e instanceof MessageEvent) {
                            final HttpResponse response = (HttpResponse) ((MessageEvent) e).getMessage();
                            response.setHeader(HttpHeaders.Names.SERVER, "MWS/1.0");
                            response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());

                            String contentType = response.getHeader(HttpHeaders.Names.CONTENT_TYPE);
                            if (contentType == null) {
                                contentType = "text/html";
                            }

                            response.setHeader(HttpHeaders.Names.CONTENT_TYPE,contentType + "; charset=utf8");
                            logger.info("---------pipeline ws-header exe handleDownstream---------");
                        }
                        ctx.sendDownstream(e);
                    }
                });
                pipeline.addLast("handler", new HttpHandler());
                return pipeline;
            }
        });

        // Bind and start to accept incoming connections.
        try {
            port = Integer.valueOf(args[0]);
            bootstrap.bind(new InetSocketAddress(port));
        } catch (Throwable e) {
            System.out.println("bind error " + e.getMessage());
            System.exit(-1);
        }
        //增加关闭的钩子
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run()
            {
                bootstrap.releaseExternalResources();
                System.out.println("TCP服务已关闭");
            }
        });
        System.out.println("succeed, listen on " + args[0]);
    }

}
