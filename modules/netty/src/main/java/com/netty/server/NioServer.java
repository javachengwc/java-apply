package com.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class NioServer {
	
	private ServerBootstrap bootstrap;
	
	private MyChannelHandler channelHandler = new MyChannelHandler();

	public NioServer()
	{
		
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
				
		ChannelPipeline pineline = bootstrap.getPipeline();
        
		pineline.addLast("encode", new StringEncoder());
	    // 接受信息的时候会被处理
		pineline.addLast("decode", new StringDecoder());
		// 自定义处理类，我们的业务一般从这个类开始
		pineline.addLast("servercnfactory", channelHandler);
		
		bootstrap.bind(new InetSocketAddress(8080));
		
	}
	
	// 处理channel中的数据,SimpleChannelHandler帮我们实现好了很多有用户的方法这里就只重写了几个方法
	private class MyChannelHandler extends SimpleChannelHandler{

		// netty默认信息接受入口
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			long cur=System.currentTimeMillis();
			System.out.println(cur+"接受到的信息" + e.getMessage());
			System.out.println("当前线程 :"+Thread.currentThread().getName());
			new Thread("working"+cur)
			{
				public void run()
				{
					System.out.println(Thread.currentThread().getName()+" start runing");
					try{
					    Thread.currentThread().sleep(10*1000);
					}catch(Exception e)
					{}
					System.out.println(Thread.currentThread().getName()+" end");
				}
			}.start();
			
			System.out.println(cur+"处理结束");
			// 返回信息可以在dos对话框中看到自己输的内容
			e.getChannel().write(e.getMessage());
			super.messageReceived(ctx, e);
		}

		@Override
		public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			System.out.println("channel Connected......");
			super.channelConnected(ctx, e);
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			System.out.println("channelClosed");
			super.channelClosed(ctx, e);
		}
		
	}

}
