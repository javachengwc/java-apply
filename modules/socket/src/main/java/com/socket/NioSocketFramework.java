package com.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NioSocketFramework extends Thread
{
	private static Logger m_logger = LoggerFactory.getLogger(NioSocketFramework.class);
	
	private Selector m_selector;
	
	private boolean m_needRegister = false;
	
	// 因为selector会被锁住, 所以新注册的需要先放在队列里
	private Map<ServerSocketChannel, INioSocketCB> m_waitServer = new ConcurrentHashMap<ServerSocketChannel, INioSocketCB>();
	
	private Map<SocketChannel, INioSocketCB> m_waitClient = new ConcurrentHashMap<SocketChannel, INioSocketCB>();
	
	public NioSocketFramework() throws Exception
	{
		this.setName(this.getClass().getSimpleName());
		m_selector = Selector.open();
	}
	
	public void registerServer(ServerSocketChannel channel, INioSocketCB cb)
		throws Exception
	{
		m_waitServer.put(channel, cb);
		m_needRegister = true;
		m_selector.wakeup();
	}
	
	public void registerClient(SocketChannel channel, INioSocketCB cb)
		throws Exception
	{
		m_waitClient.put(channel, cb);
		m_needRegister = true;
		m_selector.wakeup();
	}
	
	private void selectAdd(SelectionKey key, int addSet)
	{
		int oldOps = key.interestOps();
		oldOps |= addSet;
		key.interestOps(oldOps);
	}
	
	private void selectRemove(SelectionKey key, int removeSet)
	{
		int oldOps = key.interestOps();
		oldOps &= ~removeSet;
		key.interestOps(oldOps);
	}
	
	public void write(SocketChannel sc, ByteBuffer buf)
		throws Exception
	{
		buf.flip();

		int n = sc.write(buf);
		// m_logger.debug("socket write count: " + n);
		
		SelectionKey key = sc.keyFor(m_selector);
		if(key == null)
		{
			m_logger.error("the SelectionKey is null");
			buf.compact();
			return;
		}
		
		if(buf.hasRemaining())
		{
			selectAdd(key, SelectionKey.OP_WRITE);
			// m_logger.debug("select add write, remaining:" + buf.remaining());
		}
		else
		{
			selectRemove(key, SelectionKey.OP_WRITE);
		}
		
		buf.compact();
	}
	
	public void onWriteAble(SocketChannel sc, ByteBuffer buf)
		throws Exception
	{
		buf.flip();
		
		@SuppressWarnings("unused")
		int n = sc.write(buf);
		// m_logger.debug("socket continue write count: " + n);
		
		if( !buf.hasRemaining())
		{
			SelectionKey key = sc.keyFor(m_selector);
			selectRemove(key, SelectionKey.OP_WRITE);
		}
		
		buf.compact();
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			
			try
			{
				int n = m_selector.select(500);
				if(m_needRegister)
				{
					_doRegistChannel();
					m_needRegister = false;
				}
				
				if(n == 0)
					continue;
				
				Iterator<SelectionKey> it = m_selector.selectedKeys()
					.iterator();
				while(it.hasNext())
				{
					SelectionKey key = it.next();
					try
					{
						if(key.isAcceptable())
						{
							// m_logger.debug("_onAccept");
							_onAccept(key);
						}
						else if(key.isReadable())
						{
							_onRead(key);
						}
						else if(key.isWritable())
						{
							// m_logger.debug("_onWrite");
							_onWrite(key);
						}
						else if(key.isConnectable())
						{
							// m_logger.debug("_onConnect");
							_onConnect(key);
						}
					}
					catch(Exception ex)
					{
						m_logger.error("process SelectionKey error.", ex);
						_onClose(key);
					}
					finally
					{
						it.remove();
					}
				}
			}
			catch(Exception ex)
			{
				m_logger.error("NioServerSocket exit.", ex);
			}
		}
	}
	
	private void _doRegistChannel()
		throws Exception
	{
		for(ServerSocketChannel ssc : m_waitServer.keySet())
		{
			SelectionKey key = ssc.register(m_selector, SelectionKey.OP_ACCEPT);
			INioSocketCB cb = m_waitServer.get(ssc);
			key.attach(cb);
		}
		m_waitServer.clear();
		
		for(SocketChannel sc : m_waitClient.keySet())
		{
			SelectionKey key = sc.register(m_selector, SelectionKey.OP_READ);
			INioSocketCB cb = m_waitClient.get(sc);
			key.attach(cb);
		}
		m_waitClient.clear();
	}
	
	private void _onAccept(SelectionKey key)
		throws Exception
	{
		ServerSocketChannel server = (ServerSocketChannel)key.channel();
		SocketChannel sc = server.accept();
		sc.configureBlocking(false);
		sc.register(m_selector, SelectionKey.OP_READ);
		
		SelectionKey acceptKey = sc.keyFor(m_selector);
		
		INioSocketCB cb = (INioSocketCB)key.attachment();
		acceptKey.attach(cb);
		cb.onAccept(acceptKey);
	}
	
	private void _onRead(SelectionKey key)
		throws Exception
	{
		
		INioSocketCB cb = (INioSocketCB)key.attachment();
		cb.onReadable(key);
	}
	
	private void _onWrite(SelectionKey key)
		throws Exception
	{
		INioSocketCB cb = (INioSocketCB)key.attachment();
		cb.onWriteable(key);
	}
	
	private void _onConnect(SelectionKey key)
		throws Exception
	{
		INioSocketCB cb = (INioSocketCB)key.attachment();
		cb.onConnect(key);
	}
	
	private void _onClose(SelectionKey key)
		throws Exception
	{
		key.cancel();
		key.channel().close();
		
		INioSocketCB cb = (INioSocketCB)key.attachment();
		cb.onDisConnect(key);
	}
}
