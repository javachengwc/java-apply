package com.socket;

import java.nio.channels.SelectionKey;

public interface INioSocketCB
{
	public abstract void onAccept(SelectionKey key)
		throws Exception;
	
	public abstract void onConnect(SelectionKey key)
		throws Exception;
	
	public abstract void onReadable(SelectionKey key)
		throws Exception;
	
	public abstract void onWriteable(SelectionKey key)
		throws Exception;
	
	public abstract void onDisConnect(SelectionKey key)
		throws Exception;
}
