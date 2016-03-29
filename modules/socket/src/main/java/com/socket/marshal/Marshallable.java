package com.socket.marshal;

import com.socket.protocal.Pack;
import com.socket.protocal.Unpack;

public interface Marshallable
{
	public abstract void marshal(Pack pack)
		throws Exception;
	
	public abstract void unmarshal(Unpack unpack)
		throws Exception;
}
