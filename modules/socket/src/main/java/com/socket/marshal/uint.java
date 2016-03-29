package com.socket.marshal;

public class uint
{
	private long v;
	
	public uint(int i)
	{
		if(i < 0)
		{
			String s = Integer.toBinaryString(i);
			v = Long.valueOf(s, 2);
		}
		else
		{
			v = i;
		}
	}
	
	public uint(long l)
	{
		v = l;
	}
	
	public uint(String l)
	{
		v = Long.valueOf(l);
	}
	
	public static uint toUInt(int i)
	{
		return new uint(i);
	}
	
	public int toInt()
	{
		return (int)v;
	}
	
	public long toLong()
	{
		return v;
	}
	
	@Override
	public String toString()
	{
		return Long.toString(v);
	}
	
	public static void main(String[] args)
		throws Exception
	{
		// int i = -1;
		// String s = Integer .toBinaryString(i);
		// long l = Long.valueOf(s, 2);
		// System.out.print(l);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)(v ^ (v >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		uint other = (uint)obj;
		if(v != other.v)
			return false;
		return true;
	}
}
