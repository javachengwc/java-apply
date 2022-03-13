package com.netty.practice.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServer {

    public static Logger logger= LoggerFactory.getLogger(EchoServer.class);

    public static boolean SSL = System.getProperty("ssl") != null;

    public static int PORT = Integer.parseInt(System.getProperty("port", "8001"));

    public static void main(String[] args) throws Exception {
        logger.info("EchoServer start................");
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        //bossGroup，用于处理 Accept 事件, 一般声明为一个线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        //workerGroup，用于处理消息的读写事件
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        EchoServerHandler serverHandler = new EchoServerHandler();
        try {
            //服务端引导类 ServerBootstrap,集成所有配置，引导程序加载
            ServerBootstrap b = new ServerBootstrap();
            //reactor变异主从模式--一个主 Reactor 和多个子 Reactor,业务线程池和子 Reactor 池合并为一,也是netty的默认reactor模式
            b.group(bossGroup, workerGroup)
                    //设置netty以nio IO模型运行
                    //如果程序运行在linux系统上，可以使用EpollServerSocketChannel，它采用 epoll 模型，比 select 模型更高效
                    .channel(NioServerSocketChannel.class)
                    //设置SO_BACKLOG 参数，表示最大等待连接数
                    .option(ChannelOption.SO_BACKLOG, 100)
                    //ServerSocketChannel对应的 Handler,只能设置一个，它会在 SocketChannel 建立起来之前执行
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            // 可以添加多个子Handler
                            // 添加长度+内容解码器
                            //p.addLast(new LengthFieldBasedFrameDecoder(1024,1024,1024));
                            //添加业务处理handler
                            p.addLast(serverHandler);
                            //如果是使用addLast(group,handler),表示多线程处理业务
                            //p.addLast(EventExecutorGroup group,serverHandler);
                        }
                    });

            //绑定端口，并启动服务端程序，sync () 会阻塞直到启动完成才执行后面的代码
            ChannelFuture f = b.bind(PORT).sync();
            logger.info("EchoServer start end................");
            //等待服务端监听端口关闭，sync () 会阻塞主线程
            f.channel().closeFuture().sync();
        } finally {
            //shutdownGracefully()方法优雅地关闭线程池，优雅停机
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
