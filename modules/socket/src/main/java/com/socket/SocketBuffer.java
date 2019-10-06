package com.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

//buffer最重要的状态变量有三个，以及重要方法
//    position---缓冲区的位置，是下一个要读取或写入的元素的索引。缓冲区的位置不能为负，并且不能大于其限制
//    limit---缓冲区的限制。在写模式下表示最多能写入多少数据，此时和Capacity相同，在读模式下表示最多能读多少数据，此时和缓存中的实际数据大小相同。
//    capacity---指定了可以存储在缓冲区中的最大数据容量
//0 <= position <= limit <= capacity
//clear() 清除方法，转换成写模式.使缓冲区为一系列新的通道写入操作做好准备：它将限制设置为容量大小，将位置设置为 0。
//flip()  反转方法，转换成读模式，使缓冲区为一系列新的通道读取做好准备：它将限制设置为当前位置，然后将位置设置为 0。
//rewind() 重绕方法，在读写模式下都可用，它单纯的将当前位置置0，同时取消mark标记，限制保持不变。
//remaining() 返回当前位置与限制之间的元素数，一般在读模式下调用。
//compact() 压缩缓冲区方法，将缓冲区的当前位置和界限之间的字（如果有复制到缓冲区的开始处。调用后变成写模式,
// 即将索引 p = position() 处的字节复制到索引 0 处，将索引 p + 1 处的字节复制到索引 1 处，
// 依此类推，直到将索引 limit() - 1 处的字节复制到索引 n = limit() - 1 - p 处。
// 然后将缓冲区的位置设置为 n+1，并将其界限设置为其容量。
public class SocketBuffer
{
	private static Logger m_logger = LoggerFactory.getLogger(SocketBuffer.class);
	
	/**
	 * 最大buffer 大小
	 */
	private int m_maxCapacity = 1024 * 512;
	
	/**
	 * 默认buffer 大小 640
	 */
	private int m_capacity = 1024*64;
	
	private ByteBuffer m_readbuf;
	
	private ByteBuffer m_writebuf;
	
	private int m_curPacketSize = -1;
	
	private ISocketBufferCB m_cb;
	
	public SocketBuffer(ISocketBufferCB cb)
	{
		m_cb = cb;
		m_readbuf = ByteBuffer.allocate(m_capacity);
		m_writebuf = ByteBuffer.allocate(m_capacity);
		m_readbuf.order(ByteOrder.LITTLE_ENDIAN);
		m_writebuf.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public int onRead(SelectionKey key)
		throws Exception
	{
		SocketChannel channel = (SocketChannel)key.channel();
		m_logger.info(""+channel.isConnected());
		while(true)
		{
			int count = channel.read(m_readbuf);
			
			if(count == -1)
				return -1;
			
			if(count == 0)
			{
				if(m_readbuf.position() == m_readbuf.capacity())
				{
					m_logger
						.warn("the m_readbuf m_readbuf.position()==m_readbuf.capacity()="
							+ m_readbuf.position());
					this.incCapacity(128);
					
				}
				else
				{
					break;
				}
				
			}
			
			while(true)
			{
				if(processReadBuf(key))
				{
					break;
				}
			}
			
		}
		decCapacity();
		return 1;
	}
	
	private boolean processReadBuf(SelectionKey key) throws Exception
	{
		//变成读模式
		m_readbuf.flip();
		// 第一次读取，须读取包的大小
		if(m_curPacketSize == -1)
		{
			//已有数据不到4字节
			if(m_readbuf.remaining() < 4)
			{
				//为写作准备
				m_readbuf.compact();
				m_logger.debug("length not enough, need 4");
				return true;
			}
			// 读取包大小
			m_curPacketSize = m_readbuf.getInt();
			m_logger.debug("packet size=" + m_curPacketSize);
			incCapacity(m_curPacketSize - 4);
		}

		//没有个完整包
		if(m_readbuf.remaining() < m_curPacketSize - 4)
		{
			m_logger.debug("packet size not enough, need " + m_curPacketSize
				+ " now:" + m_readbuf.remaining());
			m_readbuf.compact();
			return true;
		}
		
		// m_logger.debug("m_curPacketSize: " + m_curPacketSize);
		byte[] bytes = new byte[ m_curPacketSize - 4 ];
		//读取一个完整包数据
		m_readbuf.get(bytes, 0, m_curPacketSize - 4);
		//为写作准备
		m_readbuf.compact();
		
		m_curPacketSize = -1;
		
		m_cb.onPacket(key, bytes);
		if(m_readbuf.position() != 0)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 减少当前buffer 到默认值容量
	 */
	private void decCapacity()
	{
		if(m_readbuf.capacity() == m_capacity)
			return;
		int size = m_readbuf.position();
		if(size < m_capacity)
		{
			ByteBuffer newBuffer = ByteBuffer.allocate(m_capacity);
			newBuffer.order(m_readbuf.order());
			m_readbuf.flip();
			newBuffer.put(m_readbuf);
			m_readbuf = newBuffer;
			m_logger.info("decCapacity ByteBuffer,the old buffer size=" + size);
		}
	}
	
	/**
	 * 增加buffer容量
	 * 
	 * @throws Exception
	 */
	private void incCapacity(int increament)
		throws BufferOverflowException
	{
		int rem = m_readbuf.capacity() - m_readbuf.position();
		if(rem >= increament)
		{
			return;
		}
		if((increament + 4) > m_maxCapacity)
		{
			m_logger
				.info("DynamicBuffer maximun size reached " + m_maxCapacity);
			throw new BufferOverflowException();
		}
		int newCapacity = Math.min(m_readbuf.capacity() * 2, m_maxCapacity);
		
		if(newCapacity == m_maxCapacity)
		{
			m_logger.warn("use the maxCapacity ByteBuffer,the m_maxCapacity="
				+ m_maxCapacity);
		}
		ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
		newBuffer.order(m_readbuf.order());
		newBuffer.putInt(increament + 4);
		newBuffer.put(m_readbuf);
		m_readbuf = newBuffer;
		newBuffer.flip();
		newBuffer.getInt();
		m_logger.info("incCapacity ByteBuffer,the newCapacity=" + newCapacity
			+ ",packet size=" + (increament + 4));
	}
	
	public void appendToWriteBuffer(byte[] bytes)
		throws Exception
	{
		ensureWriteBufferCapacity(bytes.length + 4);
		m_writebuf.putInt(bytes.length);
		m_writebuf.put(bytes);
	}
	
	public void appendToWriteBuffer(byte[] bytes, int offset, int length)
		throws Exception
	{
		// 包的大小为包头+包体+长度（int 4个字节）
		int size = length + 4;
		ensureWriteBufferCapacity(size);
		m_writebuf.putInt(size);
		m_writebuf.put(bytes, offset, length);
	}
	
	// TODO 暂时有问题
	private void ensureWriteBufferCapacity(int increament)
	{
		int rem = m_writebuf.capacity() - m_writebuf.position();
		if(rem >= increament)
		{
			decWriteBuffer(increament);
			return;
		}
		incWriteBuffer(increament);
	}
	
	private void incWriteBuffer(int increament)
	{
		
		if((increament) > m_maxCapacity)
		{
			m_logger.info("WriteBuffer maximun size reached " + m_maxCapacity);
			throw new BufferOverflowException();
		}
		int requiredCapacity = m_writebuf.capacity() + increament
			- m_writebuf.remaining();
		int tmpCap = Math.max(m_writebuf.capacity() * 2, requiredCapacity);
		int newCapacity = Math.min(tmpCap, m_maxCapacity);
		
		if(newCapacity == m_maxCapacity)
		{
			m_logger.warn("use the maxCapacity WriteBuffer,the m_maxCapacity="
				+ m_maxCapacity);
		}
		ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
		newBuffer.order(m_writebuf.order());
		m_writebuf.flip();
		newBuffer.put(m_writebuf);
		m_writebuf = newBuffer;
		
		m_logger.info("incCapacity WriteBuffer,the newCapacity=" + newCapacity
			+ ",packet size=" + (increament + 4));
	}
	
	private void decWriteBuffer(int increament)
	{
		if(m_writebuf.capacity() == m_capacity)
			return;
		int size = m_writebuf.position() + increament;
		if(size < m_capacity)
		{
			ByteBuffer newBuffer = ByteBuffer.allocate(m_capacity);
			newBuffer.order(m_writebuf.order());
			m_writebuf.flip();
			newBuffer.put(m_writebuf);
			m_writebuf = newBuffer;
			m_logger
				.info("decCapacity WriteBuffer,the old buffer size=" + size);
		}
	}
	
	public ByteBuffer getWriteBuffer()
	{
		return m_writebuf;
	}
}
