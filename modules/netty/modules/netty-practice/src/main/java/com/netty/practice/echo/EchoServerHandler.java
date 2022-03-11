package com.netty.practice.echo;

import com.util.base.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

//@Sharable的Handler，表示一个可以被分享的handler，可以分享给多个客户端使用
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
        byte[] barray = new byte[buf.readableBytes()];
        buf.getBytes(0, barray);
        String str = new String(barray);
        return str;
    }
}
