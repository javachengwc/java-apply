package com.mina.server;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 带心跳包的mina服务端
 */
public class KeepaliveServer {

    private static final int PORT = 9000;
    /** 200秒后超时 */
    private static final int IDELTIMEOUT = 200;
    /** 10秒发送一次心跳包 */
    private static final int HEARTBEATRATE = 10;
    /** 心跳包内容 */
    private static final String HEARTBEATREQUEST = "keepalive";

    private static Logger logger = LoggerFactory.getLogger(KeepaliveServer.class);

    public static void main(String[] args) throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setReadBufferSize(1024);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,IDELTIMEOUT);
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory()));
        KeepAliveMessageFactory heartBeatFactory = new KeepAliveMessageFactoryImpl();
        KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.READER_IDLE,new KeepAliveRequestTimeoutHandlerImpl());
        //KeepAliveFilter heartBeat = new KeepAliveFilter(heartBeatFactory, IdleStatus.READER_IDLE,new KeepAliveRequestTimeoutHandlerImpl(),HEARTBEATRATE,5);
        //设置是否forward到下一个filter
        ////继续调用IoHandlerAdapter中的 sessionIdle事件
        heartBeat.setForwardEvent(true);
        //设置心跳频率,此处在第一次发出心跳后，后面的心跳请求是在得到响应或则心跳响应超时后再有空闲的HEARTBEATRATE时间后再发起心跳请求
        heartBeat.setRequestInterval(HEARTBEATRATE);
        acceptor.getFilterChain().addLast("heartbeat", heartBeat);
        acceptor.setHandler(new KeepaliveHandler());
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("Server started on port： " + PORT);
    }

    //聋子型心跳机制
    private static class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

        @Override
        public boolean isRequest(IoSession session, Object message) {
            logger.info(Thread.currentThread().getName()+" isRequest: " + message);
            if (message.equals(HEARTBEATREQUEST)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean isResponse(IoSession session, Object message) {
            logger.info(Thread.currentThread().getName()+" isResponse: " + message);
            //判断收到的message是否是响应信息,自定义判断
            //这里的判断会影响响应是否超时的判定
            //这里返回true表示不需要判断具体返回内容，只需要收到有响应，都表示有心跳
            return true;
        }

        @Override
        public Object getRequest(IoSession session) {
            logger.info("准备请求信息: " + HEARTBEATREQUEST);
            //准备心跳请求信息
            return HEARTBEATREQUEST;
        }

        @Override
        public Object getResponse(IoSession session, Object request) {
            logger.info("准备响应信息");
            //如果服务端是接受心跳请求，这里就是对心跳请求的响应，
            //因为此程序中是服务端对客户端发起心跳请求，不需要接受心跳请求，此方法直接返回null即可
            return null;
        }
    }

    /**
     * 对应上面的注释
     * KeepAliveFilter(heartBeatFactory,IdleStatus.BOTH_IDLE,heartBeatHandler)
     * 心跳超时处理
     * KeepAliveFilter在没有收到心跳消息的响应时，会报告给的KeepAliveRequestTimeoutHandler。
     * 默认的处理是 KeepAliveRequestTimeoutHandler.CLOSE（即如果不给handler参数，则会使用默认的从而Close这个Session）
     */
    private static class KeepAliveRequestTimeoutHandlerImpl implements KeepAliveRequestTimeoutHandler {
      @Override
      public void keepAliveRequestTimedOut(KeepAliveFilter filter,IoSession session) throws Exception {
          logger.info("KeepAliveRequestTimeoutHandlerImpl keepAliveRequestTimedOut 心跳超时");
          //session.close(true);
      }
    }
}
