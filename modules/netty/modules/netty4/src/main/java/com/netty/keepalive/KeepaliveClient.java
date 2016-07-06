package com.netty.keepalive;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 心跳客户端
 * 1.客户端网络空闲10秒没有进行写操作时，进行发送一次ping心跳给服务端,失败心跳计数器加1;
 * 3.每当客户端收到服务端的pong心跳应答后，失败心跳计数器清零;
 * 4.如果连续超过3次没有收到服务端的心跳回复，则断开当前连接，在5秒后进行重连操作，直到重连成功，否则每隔5秒又会进行重连;
 * 5.服务端网络空闲状态到达30秒没有进行读操作时，服务端心跳失败计数器加1;
 * 6.只要收到客户端的ping消息，服务端心跳失败计数器清零;
 * 7.服务端连续3次没有收到客户端的ping消息后，将关闭链路，释放资源，等待客户端重连;
 */
public class KeepaliveClient {

    private static Logger logger = LoggerFactory.getLogger(KeepaliveClient.class);

    //定义客户端没有收到服务端的pong消息的最大次数
    private static final int MAX_UN_REC_PONG_TIMES = 3;

    //多长时间未请求后，发送心跳
    private static final int WRITE_WAIT_SECONDS = 10;

    //隔N秒后重连
    private static final int RE_CONN_WAIT_SECONDS = 5;

    private String host ;
    private int port ;
    private EventLoopGroup group ;
    private Bootstrap b ;
    private Channel ch ;

    //客户端连续N次没有收到服务端的pong消息计数器
    private int unRecPongTimes = 0 ;

    private ScheduledExecutorService executorService ;

    //是否停止
    private boolean isStop = false ;

    public KeepaliveClient(String host, int port) {
        this.host = host ;
        this.port = port ;
        group = new NioEventLoopGroup();
        b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new HeartbeatInitializer());
    }

    public void start() {
        connServer();
        logger.info("KeepaliveClient start");
    }

    private void connServer(){
        isStop = false;
        if(executorService!=null){
            executorService.shutdown();
        }
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(new Runnable() {
            boolean isConnSucc = true;
            public void run() {
                logger.info("KeepaliveClient connect server executorService run");
                try {
                    // 重置计数器
                    unRecPongTimes = 0;
                    // 连接服务端
                    if(ch!=null&&ch.isOpen()){
                        ch.close();
                    }
                    ch = b.connect(host, port).sync().channel();
                    // 此方法会阻塞
					//ch.closeFuture().sync();
                    logger.info("KeepaliveClient connect server finish");
                } catch (Exception e) {
                    e.printStackTrace();
                    isConnSucc = false ;
                } finally{
                    if(isConnSucc){
                        if(executorService!=null){
                            executorService.shutdown();
                        }
                    }
                }
            }
        }, RE_CONN_WAIT_SECONDS, RE_CONN_WAIT_SECONDS, TimeUnit.SECONDS);
    }

    public class HeartbeatInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
            pipeline.addLast("encoder", new ObjectEncoder());
            pipeline.addLast("ping", new IdleStateHandler(0, WRITE_WAIT_SECONDS, 0,TimeUnit.SECONDS));
            // 客户端的逻辑
            pipeline.addLast("handler", new ClientHandler());
        }
    }

    public class ClientHandler extends SimpleChannelInboundHandler<KeepaliveMessage> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, KeepaliveMessage msg) throws Exception {
            logger.info("KeepaliveClient  channelRead0 msg: sn=" + msg.getSn() + ",reqcode=" + msg.getReqCode());
            if (KeepaliveMessage.RSP_CODE== msg.getReqCode()) {
                // 计数器清零
                unRecPongTimes = 0;
            }
            logger.info("KeepaliveClient  channelRead0 unRecPongTimes="+unRecPongTimes);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("KeepaliveClient channelActive alient active ");
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            logger.info("KeepaliveClient channelInactive client close ");
            super.channelInactive(ctx);
	        //重连
            if(!isStop){
                connServer();
            }
        }

        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            logger.info("KeepaliveClient userEventTriggered start");
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE) {
	                //读超时
                    logger.info("KeepaliveClient userEventTriggered READER_IDLE读超时)");
                } else if (event.state() == IdleState.WRITER_IDLE) {
	                //写超时
                    logger.info("KeepaliveClient userEventTriggered WRITER_IDLE写超时,unRecPongTimes="+unRecPongTimes);
                    if(unRecPongTimes < MAX_UN_REC_PONG_TIMES){
                        logger.info("KeepaliveClient userEventTriggered  send ping msg");
                        ctx.channel().writeAndFlush(getSrcMsg()) ;
                        unRecPongTimes++;
                    }else{
                        ctx.channel().close();
                    }
                } else if (event.state() == IdleState.ALL_IDLE) {
	                //总超时
                    logger.info("KeepaliveClient userEventTriggered ALL_IDLE 总超时");
                }
            }
        }
    }

    private KeepaliveMessage getSrcMsg(){
        KeepaliveMessage keepAliveMessage = new KeepaliveMessage();
        // 设备号
        keepAliveMessage.setSn("sn_111");
        keepAliveMessage.setReqCode(KeepaliveMessage.REQ_CODE);
        return keepAliveMessage ;
    }

    public void stop(){
        isStop = true;
        if(ch!=null&&ch.isOpen()){
            ch.close();
        }
        if(executorService!=null){
            executorService.shutdown();
        }
    }

    public static void main(String[] args) {
        KeepaliveClient keepaliveClient = new KeepaliveClient("127.0.0.1",9000);
        keepaliveClient.start();
    }
}
