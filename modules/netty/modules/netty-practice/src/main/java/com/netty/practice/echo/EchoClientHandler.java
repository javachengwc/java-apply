package com.netty.practice.echo;

import com.util.base.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger= LoggerFactory.getLogger(EchoClientHandler.class);

    private static AtomicLong  num = new AtomicLong(0);

    private ByteBuf message =Unpooled.buffer(EchoClient.SIZE);

    public EchoClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        message.writeBytes("hello".getBytes());
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf)msg;
        String msgStr =this.transMsg(buf);
        logger.info("EchoClientHandler channelRead start， 收到 msg={}",msgStr);
        ThreadUtil.sleep(5*1000L);
        String sendMsg="客户端第"+num.addAndGet(1)+"次发送: hello";
        ByteBuf info=Unpooled.copiedBuffer(sendMsg.getBytes());
        logger.info("EchoClientHandler 发送信息:{}",sendMsg);
        ctx.writeAndFlush(info);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        logger.info("EchoClientHandler channelReadComplete ");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("EchoClientHandler exceptionCaught error,",cause);
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

