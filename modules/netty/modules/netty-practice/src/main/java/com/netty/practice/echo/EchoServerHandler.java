package com.netty.practice.echo;

import com.util.base.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

//@Sharable的Handler，表示这个Handler 没有线程安全的问题，可以被多个 Channel 共享，可以创建为单例。
//编写的大部分Handler应该是 @Sharable的，在Handler中不添加状态信息，无状态才能作为单例使用。
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger= LoggerFactory.getLogger(EchoServerHandler.class);

    public static AtomicLong num = new AtomicLong(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf)msg;
        String msgStr =this.transMsg(buf);
        logger.info("EchoServerHandler channelRead start,收到 msg={}",msgStr);
        ThreadUtil.sleep(5*1000L);
        String replyMsg="服务端第"+num.addAndGet(1)+"次回复: hello";
        ByteBuf message=Unpooled.copiedBuffer(replyMsg.getBytes());
        logger.info("EchoServerHandler 回复信息:{}",replyMsg);
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        logger.info("EchoServerHandler channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("EchoServerHandler exceptionCaught error,",cause);
        cause.printStackTrace();
        ctx.close();
    }

    private String transMsg(ByteBuf buf) {
        byte[] bytes  = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String str = new String(bytes, StandardCharsets.UTF_8);
        return str;
    }
}
