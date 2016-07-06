package com.netty.keepalive;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 心跳服务端
 */
public class KeepaliveServer {

    private static Logger logger = LoggerFactory.getLogger(KeepaliveServer.class);

    //多长时间未收到请求后,服务端失败计数器加1
    private static final int READ_WAIT_SECONDS = 30;

    //服务端没收到客户端的ping消息的最大次数
    private static final int MAX_UN_REC_PING_TIMES = 3;

    //在线的map
    private ConcurrentHashMap<Channel,KeepaliveMessage> onlineMap = new ConcurrentHashMap<Channel, KeepaliveMessage>();
    //在线的失败计数器-->未收到client端发送的ping请求
    private ConcurrentHashMap<Channel,Integer> onlinePingMap = new ConcurrentHashMap<Channel, Integer>();

    private int port ;

    private ChannelFuture f ;

    private ServerBootstrap b ;

    public KeepaliveServer(int port) {
        this.port = port;
    }

    public void startServer() {

        logger.info("KeepaliveServer begin");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new KeepaliveServerInitializer());
            // 服务器绑定端口监听
            f = b.bind(port).sync();
            //打印在线信息
            new Timer().scheduleAtFixedRate( new TimerTask() {
                public void run() {
                    showOnlineInfo();
                }
            },10000,10000);

            //监听服务器关闭监听，此方法会阻塞
            f.channel().closeFuture().sync();
            logger.info("KeepaliveServer start ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 消息处理器
     */
    private class KeepaliveServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {

            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
            pipeline.addLast("encoder", new ObjectEncoder());
			//这里只监听读操作
            pipeline.addLast("pong", new IdleStateHandler(READ_WAIT_SECONDS, 0, 0, TimeUnit.SECONDS));
            pipeline.addLast("handler", new Heartbeat());
        }
    }

    private class Heartbeat extends SimpleChannelInboundHandler<KeepaliveMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, KeepaliveMessage msg) throws Exception {
            logger.info(Thread.currentThread().getName()+" "+ctx.channel().remoteAddress() + " msg: sn=" + msg.getSn() + ",reqcode=" + msg.getReqCode());
            // 收到ping消息后，回复
            if(!StringUtils.isBlank(msg.getSn())&& msg.getReqCode()==KeepaliveMessage.REQ_CODE){

                Channel channel =ctx.channel();
                //在线信息
                onlineMap.putIfAbsent(channel,msg);
                //在线的超时记数器清0
                onlinePingMap.put(channel,0);

                msg.setReqCode(KeepaliveMessage.RSP_CODE);
                ctx.channel().writeAndFlush(msg);

            }else{
                ctx.channel().close();
            }
        }

        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE) {
	                //读超时
                    Channel channel =ctx.channel();
                    Integer unRecPingTimes=onlinePingMap.get(channel);
                    unRecPingTimes= (unRecPingTimes==null)?0:unRecPingTimes;
                    KeepaliveMessage msg = onlineMap.get(channel);

                    logger.info(Thread.currentThread().getName()+" KeepaliveServer userEventTriggered 服务端(READER_IDLE读超时),unRecPingTimes="+unRecPingTimes);
                    // 失败计数器次数大于等于3次的时候，关闭链接，等待client重连
                    if(unRecPingTimes >= MAX_UN_REC_PING_TIMES){
                        logger.info("KeepaliveServer userEventTriggered 服务端(READER_IDLE读超时,关闭chanel)");
                        //从集合中移除
                        onlineMap.remove(channel);
                        onlinePingMap.remove(channel);
                        //连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连
                        ctx.channel().close();
                    }else{
                        // 失败计数器加1
                        unRecPingTimes++;
                        onlinePingMap.put(channel, unRecPingTimes);
                    }
                    logger.info("KeepaliveServer userEventTriggered unRecPingTimes="+unRecPingTimes);
                } else if (event.state() == IdleState.WRITER_IDLE) {
	                //写超时
                    logger.info(Thread.currentThread().getName()+" KeepaliveServer userEventTriggered 服务端(WRITER_IDLE 写超时)");
                } else if (event.state() == IdleState.ALL_IDLE) {
	                //总超时
                    logger.info(Thread.currentThread().getName()+" KeepaliveServer userEventTriggered 服务端(ALL_IDLE 总超时)");
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //在客户端链接突然断开的时候触发此方法
            logger.info(Thread.currentThread().getName()+" KeepaliveServer exceptionCaught 错误原因："+cause.getMessage());
            Channel channel =ctx.channel();
            //从集合中移除
            onlineMap.remove(channel);
            onlinePingMap.remove(channel);
            channel.close();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("Heartbeat channelActive client active ");
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            logger.info("Heartbeat channelInactive 客户端失效");
            // 关闭，等待重连
            Channel channel =ctx.channel();
            onlineMap.remove(channel);
            onlinePingMap.remove(channel);
            ctx.close();
        }
    }

    public void showOnlineInfo()
    {
        int onlineCount=onlineMap.size();
        String info="";
        if (onlineCount>0)
        {
            StringBuffer buf = new StringBuffer();
            int t=0;
            for(KeepaliveMessage msg:onlineMap.values())
            {
                if(t>0 && t%5==0)
                {

                    buf.append("\r\n");
                }
                buf.append(msg.getSn()).append(" ");
                t++;
            }
            info= buf.toString();
        }
        logger.info("KeepaliveServer onlineInfo: count="+onlineCount+"\r\n"+"info="+info);
    }

    public void stopServer(){
        if(f!=null){
            f.channel().close();
        }
    }

    public static void main(String[] args) {
        KeepaliveServer keepaliveServer = new KeepaliveServer(9000);
        keepaliveServer.startServer();
    }
}
