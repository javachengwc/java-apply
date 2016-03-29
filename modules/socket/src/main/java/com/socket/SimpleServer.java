package com.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.socket.workshop.TaskItem;
import com.socket.workshop.WorkShop;
import org.apache.log4j.Logger;

public class SimpleServer implements INioSocketCB, ISocketBufferCB
{
	private static Logger m_logger = Logger.getLogger(SimpleServer.class);
	
	protected WorkShop m_workshop;
	
	private ServerSocketChannel m_serverChannel;
	
	private NioSocketFramework m_socketFrame;
	
	private Map<SelectionKey, SocketBuffer> m_socketbuffers = new HashMap<SelectionKey, SocketBuffer>();
	
	public SimpleServer(NioSocketFramework socketFrame)
	{
		m_socketFrame = socketFrame;
	}
	
	public void start(int port, int threadcount, String workerClassName)
		throws Exception
	{
		// 监听端口
		bindSocket(port);
		
		// 创建工作线程
		m_workshop = new WorkShop();
		m_workshop.create(threadcount, workerClassName);
		
		// 注册到nio框架中
		m_socketFrame.registerServer(m_serverChannel, this);
		
		m_logger.info("server started, port :"
			+ m_serverChannel.socket().getLocalPort());
	}
	
	private void bindSocket(int port)
		throws Exception
	{
		m_serverChannel = ServerSocketChannel.open();
		InetSocketAddress localAddr = new InetSocketAddress(port);
		m_serverChannel.socket().bind(localAddr);
		m_serverChannel.configureBlocking(false);
		
		m_logger.debug("server socket created, port :" + port);
	}
	
	public void sendToClient(SelectionKey key, byte[] bytes)
		throws Exception
	{
		sendToClient(key, bytes, 0, bytes.length);
	}
	
	public void sendToClient(SelectionKey key, ByteBuffer buf)
		throws Exception
	{
		sendToClient(key, buf.array(), buf.position(), buf.limit()-buf.position());
	}
	
	public synchronized void sendToClient(SelectionKey key, byte[] bytes,
		int offset, int length)
		throws Exception
	{
		SocketBuffer sockbuf = m_socketbuffers.get(key);
		if(sockbuf != null)
		{
			sockbuf.appendToWriteBuffer(bytes, offset, length);
			ByteBuffer wbuf = sockbuf.getWriteBuffer();
			SocketChannel channel = (SocketChannel)key.channel();
			m_socketFrame.write(channel, wbuf);
		}
	}
	
	public void onPacket(SelectionKey key, byte[] bytes)
		throws Exception
	{
		TaskItem taskitem = new TaskItem(key, bytes);
		m_workshop.pushTask(taskitem);
	}
	
	public void onAccept(SelectionKey key)
		throws Exception
	{
		SocketBuffer sockbuf = new SocketBuffer(this);
		m_socketbuffers.put(key, sockbuf);
	}
	
	public void onConnect(SelectionKey key)
		throws Exception
	{
		// server不处理
	}
	
	public void onReadable(SelectionKey key)
		throws Exception
	{
		SocketBuffer sockbuf = m_socketbuffers.get(key);
		if(sockbuf != null)
		{
			int ret = sockbuf.onRead(key);
			if(ret == -1)
			{
				try
				{
					String local = ((SocketChannel)key.channel()).socket().getLocalSocketAddress().toString();
					String remote = ((SocketChannel)key.channel()).socket().getRemoteSocketAddress().toString();
					m_logger.info("local:"+local + " remote:" + remote);
				}
				catch(Exception ex)
				{
					m_logger.error("", ex);
				}
				
				m_logger.info("socket read -1, will close key" + key);
				closeKey(key);
			}
		}
	}
	
	public void onWriteable(SelectionKey key)
		throws Exception
	{
		SocketBuffer sockbuf = m_socketbuffers.get(key);
		if(sockbuf != null)
		{
			ByteBuffer wbuf = sockbuf.getWriteBuffer();
			m_socketFrame.write((SocketChannel)key.channel(), wbuf);
		}
	}
	
	public void onDisConnect(SelectionKey key)
		throws Exception
	{
		m_socketbuffers.remove(key);
		m_logger.info("socket selectionKey closed, key:" + key);
	}
	
	public void closeKey(SelectionKey key)
		throws Exception
	{
		key.cancel();
		key.channel().close();
		
		onDisConnect(key);
	}
}
