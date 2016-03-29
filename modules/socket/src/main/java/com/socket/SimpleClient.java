package com.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.socket.workshop.TaskItem;
import com.socket.workshop.WorkShop;
import org.apache.log4j.Logger;


public class SimpleClient implements INioSocketCB, ISocketBufferCB
{
	private static Logger m_logger = Logger.getLogger(SimpleClient.class);
	
	protected WorkShop m_workshop;
	
	private SocketBuffer m_sockBuffer;
	
	private boolean m_block = false;
	
	// private NioClientSocket m_clientSocket;
	private SocketChannel m_clientChannel;
	
	private NioSocketFramework m_socketFrame;
	
	private String m_host;
	
	private int m_port;
	
	public SimpleClient(NioSocketFramework socketFrame)
	{
		m_socketFrame = socketFrame;
		m_sockBuffer = new SocketBuffer(this);
	}
	
	public void setBlock(boolean block)
	{
		m_block = block;
	}
	
	public void connect(String host, int port)
		throws Exception
	{
		connect(host, port, "", 0);
	}
	
	public void connect(String host, int port, String workerClassName,
		int workerCount)
		throws Exception
	{
		// 是否需要创建处理收到回应的工作线程
		if(workerCount > 0)
		{
			m_workshop = new WorkShop();
			m_workshop.create(workerCount, workerClassName);
		}
		
		m_host = host;
		m_port = port;
				
		m_clientChannel = SocketChannel.open();
		
		reconn();
	}
	
	private void reconn()
		throws Exception
	{
		if(!m_clientChannel.isOpen())
			m_clientChannel = SocketChannel.open();
		
		InetSocketAddress addr = new InetSocketAddress(m_host, m_port);
		m_clientChannel.connect(addr);
		m_clientChannel.configureBlocking(m_block);
		m_socketFrame.registerClient(m_clientChannel, this);
	}
	
	public synchronized void sendToServer(byte[] bytes)
		throws Exception
	{
		if(!isConnected())
		{
			reconn();
		}
		
		m_sockBuffer.appendToWriteBuffer(bytes);
		ByteBuffer wbuf = m_sockBuffer.getWriteBuffer();
		m_socketFrame.write(m_clientChannel, wbuf);
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
		// client不处理
	}
	
	public void onConnect(SelectionKey key)
		throws Exception
	{
		// 使用同步的connect,不处理
	}
	
	public void onReadable(SelectionKey key)
		throws Exception
	{
		int ret = m_sockBuffer.onRead(key);
		if(ret == -1)
		{
			m_logger.info("socket read -1, will close key" + key);
			m_clientChannel.close();
		}
	}
	
	public void onWriteable(SelectionKey key)
		throws Exception
	{
		ByteBuffer wbuf = m_sockBuffer.getWriteBuffer();
		m_socketFrame.write(m_clientChannel, wbuf);
	}
	
	public void onDisConnect(SelectionKey key)
		throws Exception
	{
		
	}
	
	public boolean isConnected()
	{
		if(m_clientChannel == null)
			return false;
		
		return m_clientChannel.isConnected();
	}
}
