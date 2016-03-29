package com.socket;

import java.nio.channels.SelectionKey;

public interface ISocketBufferCB
{
	public void onPacket(SelectionKey key, byte[] bytes)
		throws Exception;
}
