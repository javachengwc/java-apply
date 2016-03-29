package com.socket;

import com.socket.protocal.Pack;
import com.socket.protocal.Unpack;
import com.socket.protocal.UnpackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BlockingClient
{
	private Socket m_socket = null;
	
	public String m_host;
	
	public int m_port;
	
	protected OutputStream m_outputStream = null;
	
	protected InputStream m_inputStream = null;
	
	protected ByteBuffer m_inputBuffer = null;
	
	private static Logger m_logger = LoggerFactory.getLogger(BlockingClient.class);
	
	private int m_defSoTimeout = 10 * 1000; // 默认读取超时为10s
	
	public BlockingClient(String host, int port) throws UnknownHostException,
		IOException
	{
		m_host = host;
		m_port = port;
	}
	
	public void reconnect()
		throws Exception
	{
		if(m_socket != null)
		{
			try
			{
				close();
			}
			catch(IOException e)
			{
				m_logger.error("Close socket error,", e);
			}
		}
		m_logger.debug("reconnect " + m_host+":"+m_port);
		m_socket = new Socket(m_host, m_port);
		m_outputStream = m_socket.getOutputStream();
		m_inputStream = m_socket.getInputStream();
		
		m_socket.setSoTimeout(m_defSoTimeout);
	}
	
	public void setTimeout(int millisecond)
		throws SocketException
	{
		m_defSoTimeout = millisecond;
		if(m_socket != null)
			m_socket.setSoTimeout(m_defSoTimeout);
	}
		
	public boolean isConnected()
	{
		if(m_socket == null)
			
			return false;
		
		return m_socket.isConnected();
	}
	
	/**
	 * 发送字节数据
	 * @param bytes 字节数据
	 * @param isLogger 如果是需要服务器的返回数据进行处理则为true,反之为false
	 * @throws java.io.IOException
	 */
	public void sendBytes(byte[] bytes,boolean isLogger)
		throws IOException
	{
		ByteBuffer out = ByteBuffer.allocate(4);
		out.order(ByteOrder.LITTLE_ENDIAN);
		//长度包含包长度int 4个字节
		out.putInt(bytes.length+4);
		
		synchronized(this)
		{
			clearInputBuffer(isLogger);
			m_outputStream.write(out.array());
			m_outputStream.write(bytes);
		}
	}
	
	public void sendPacket(Pack pack)
		throws IOException
	{
		synchronized(this)
		{
			clearInputBuffer(true);
			
			ByteBuffer data = pack.getBuffer();
			
			ByteBuffer out = ByteBuffer.allocate(data.limit()-data.position()+4);
			out.order(ByteOrder.LITTLE_ENDIAN);
			//长度包含包长度int 4个字节
			out.putInt(data.limit()-data.position()+4);
			out.put(data);
			out.flip();
			//m_outputStream.write(out.array());
			m_outputStream.write(out.array(), out.position(), out.limit());
		}
	}
	
	public byte[] receiveBytes()
		throws IOException
	{
		synchronized(this)
		{
			// 获取包长度字段
			byte[] lengthBytes = new byte[ 4 ];
			int ret = m_inputStream.read(lengthBytes);
			
			if(ret == -1)
			{
				throw new IOException("Connection closed by peer.");
			}
			
			Unpack lengthUnpack = new Unpack(lengthBytes);
			int packetSize = lengthUnpack.popInt();
			
			// 非法长度，抛出异常
			int maxlen = 1024 * 1024 * 64; // 64m
			if(packetSize < 0 || packetSize > maxlen)
				throw new UnpackException("Invalid packet, size = "
					+ packetSize);
			
			// len包含前4个字节
			ByteBuffer packetBuffer = ByteBuffer.allocate(packetSize-4);
			packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
			
			byte[] packetBytes = packetBuffer.array();
			int size = 0;
			while(size < packetSize-4)
			{
				size += m_inputStream
					.read(packetBytes, size, packetSize-4-size);
			}
			
			return packetBytes;
		}
	}
	
	public Unpack receivePacket()
		throws IOException
	{
		synchronized(this)
		{
			// 获取包长度字段
			byte[] lengthBytes = new byte[ 4 ];
			int ret = m_inputStream.read(lengthBytes);
			
			if(ret == -1)
			{
				throw new IOException("Connection closed by peer.");
			}
			
			Unpack lengthUnpack = new Unpack(lengthBytes);
			int packetSize = lengthUnpack.popInt();
			
			// 非法长度，抛出异常
			int maxlen = 1024 * 1024 * 64; // 64m
			if(packetSize < 0 || packetSize > maxlen)
				throw new UnpackException("Invalid packet,size too big or is size=0, size = "
					+ packetSize);
			
			// read the remaining data
//			ByteBuffer packetBuffer = ByteBuffer.allocate(packetSize);
//			packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
//			packetBuffer.putInt(packetSize);
			
			byte[] packetBytes = new byte[packetSize-4];
			int size = 0;
			while(size < packetSize - 4)
			{
				size += m_inputStream.read(packetBytes, size, packetSize
					- 4 - size);
			}
			
			Unpack packetUnpack = new Unpack(packetBytes);
			
//			String ss = "";
//			for(int ii = 0; ii < packetBytes.length; ii++)
//			{
//				ss += Integer.toHexString(packetBytes[ii]) + " ";
//			}
//			m_logger.debug("thread:"+Thread.currentThread().getId()+"socket:"+this.m_socket+">>>>:" + ss);
//			m_logger.debug("---ava:" + m_inputStream.available());
			return packetUnpack;
		}
	}
	
	private void clearInputBuffer(boolean isLogger) throws IOException
	{
		int available = m_inputStream.available();
		if(available > 0)
		{
			long skip = m_inputStream.skip(available);
			if(isLogger){
				m_logger.debug("连接中有上次未读完的数据！socket:"+this.m_socket + ",字节数:"+available +",已跳过："+skip, 
					new Exception());
			}
		}
	}
	
	public void close()
		throws IOException
	{
		if(m_socket != null)
		{
			m_socket.shutdownOutput();
			m_socket.close();
		}
	}
	
	@Override
	public String toString(){
		return "BlockingClient:"+this.m_host+":"+this.m_port;
	}
	
}
