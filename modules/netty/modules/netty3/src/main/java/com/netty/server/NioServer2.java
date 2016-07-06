package com.netty.server;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class NioServer2 {

	public NioServer2() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),// boss线程池
						Executors.newCachedThreadPool()// worker线程池
				));
		ExecutionHandler executionHandler =new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(16, 1024*1024, 1024*1024));
		bootstrap.setPipelineFactory(new BussnessPipelineFactory(executionHandler));

		bootstrap.setOption("child.tcpNoDelay", true);
		//bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(8081));//
	}

	private class BussnessPipelineFactory implements ChannelPipelineFactory {

		private final ExecutionHandler executionHandler;

		public BussnessPipelineFactory(final ExecutionHandler executionHandler) {

			this.executionHandler = executionHandler;

		}

		public ChannelPipeline getPipeline() {

			return Channels.pipeline(

			new StringDecoder(),
	
			new StringEncoder(),

			executionHandler, // 多个pipeline之间必须共享同一个ExecutionHandler

			new BussnessHandler());// 业务逻辑handler，IO密集

		}
       
	}
	
	private class BussnessHandler extends SimpleChannelHandler{

		// netty默认信息接受入口
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			long cur=System.currentTimeMillis();
//			HttpMessage m =(HttpMessage)e.getMessage();
//			List<Map.Entry<String, String>> list = m.getHeaders();
//			for(Map.Entry<String, String> s:list)
//			{
//				System.out.println("hearder----"+s.getKey()+":"+s.getValue());
//			}
			System.out.println(cur+"接受到的信息" + e.getMessage());
			System.out.println("当前线程 :"+Thread.currentThread().getName());
			//Thread.sleep(10*1000);
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
