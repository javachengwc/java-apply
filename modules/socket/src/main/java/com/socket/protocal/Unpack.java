package com.socket.protocal;

import com.socket.marshal.Marshallable;
import com.socket.marshal.uint;

import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Unpack
{
	protected ByteBuffer buffer;
	
	public int getSize()
	{
		return buffer.limit() - buffer.position();
	}
	
	/* wrap */
	public Unpack(byte[] bytes, int offset, int length)
	{
		buffer = ByteBuffer.wrap(bytes, offset, length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public Unpack(byte[] bytes)
	{
		this(bytes, 0, bytes.length);
	}
	
	/**
	 * buf [ position : limit] -> Unpack
	 */
	public Unpack(ByteBuffer buf)
	{
		this(buf.array(), buf.position(), buf.limit() - buf.position());
	}
	
	/**
	 * 该函数没有buffer，只是便于Class.newInstance进行统一初始化, 只有popObject能继续使用之
	 */
	public Unpack()
	{
		buffer = null;
	}
	
	public ByteBuffer getBuffer()
	{
		return buffer.duplicate();
	}
	
	public byte[] popFetch(int sz)
	{
		try
		{
			byte[] fetch = new byte[ sz ];
			buffer.get(fetch);
			return fetch;
		}
		catch(BufferUnderflowException bEx)
		{
			throw new UnpackException();
		}
	}
	
	public Object popObject(Object obj)
		throws Exception
	{
		if(obj instanceof Marshallable)
		{
			return popMarshallable((Marshallable)obj);
		}
		else
			if(obj instanceof String)
			{
				return popVarstr();
			}
			else
				if(obj instanceof Unpack)
				{
					((Unpack)obj).buffer = ByteBuffer.allocate(buffer
						.remaining());
					((Unpack)obj).buffer.order(ByteOrder.LITTLE_ENDIAN);
					((Unpack)obj).buffer.put(buffer);
					((Unpack)obj).buffer.rewind();
					return obj;
				}
				else
				{
					throw new UnpackException("unknow object type");
				}
	}
	
	public byte popByte()
	{
		try
		{
			return buffer.get();
		}
		catch(BufferUnderflowException bEx)
		{
			throw new UnpackException();
		}
	}
	
	// 16位的大小
	public byte[] popVarbin()
	{
		return popFetch(buffer.getShort());
	}
	
	// 32位的大小
	public byte[] popVarbin32()
	{
		return popFetch(buffer.getInt());
	}
	
	public String popVarbin(String encode)
	{
		try
		{
			byte[] bytes = popVarbin();
			return new String(bytes, encode);
		}
		catch(UnsupportedEncodingException codeEx)
		{
			throw new UnpackException();
		}
	}
	
	public String popVarstr()
	{
		return popVarbin("utf-8");
	}
	
	public String popVarstr(String encode)
	{
		return popVarbin(encode);
	}
	
	public int popInt()
	{
		try
		{
			return buffer.getInt();
		}
		catch(BufferUnderflowException bEx)
		{
			throw new UnpackException();
		}
	}
	
	public uint popUInt()
	{
		return new uint(popInt());
	}
	
	public long popLong()
	{
		try
		{
			return buffer.getLong();
		}
		catch(BufferUnderflowException bEx)
		{
			throw new UnpackException();
		}
	}
	
	public short popShort()
	{
		try
		{
			return buffer.getShort();
		}
		catch(BufferUnderflowException bEx)
		{
			throw new UnpackException();
		}
	}
	
	public Marshallable popMarshallable(Marshallable mar)
		throws Exception
	{
		mar.unmarshal(this);
		return mar;
	}
	
	public boolean popBoolean()
	{
		if(popByte() > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public String toString()
	{
		return buffer.toString();
	}
	
}
