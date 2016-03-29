package com.socket.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * TCP服务器端
 */
public class NioTcpServer {

	// 超时时间，单位毫秒
	private static final int TimeOut = 3000;

	private Selector selector;
	private ServerSocketChannel listenerChannel;

	private NioTcpServer(){
		
	}
	private static NioTcpServer inst = new NioTcpServer();
	
	public static NioTcpServer getInstance(){
		return inst;
	}
	public void init() throws IOException{
		// 创建选择器
		selector = Selector.open();

		// 打开监听信道
		listenerChannel = ServerSocketChannel.open();

		// 与本地端口绑定
		listenerChannel.socket().bind(new InetSocketAddress( 10002));

		// 设置为非阻塞模式
		listenerChannel.configureBlocking(false);

		// 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
		listenerChannel.register(selector, SelectionKey.OP_ACCEPT);		
	}
	public void startListen() throws IOException{
		// 反复循环,等待IO
		while (true) {
			// 等待某信道就绪(或超时)
			if (selector.select(TimeOut) == 0) {
				continue;
			}

			// 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();

			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();

				try {
					if (key.isAcceptable()) {
						// 有客户端连接请求时
						new ClientSocket().handleAccept(key);
					}

					if (key.isReadable()) {
						// 从客户端读取数据
						new ClientSocket().handleRead(key);
					}

				} catch (Exception ex) {
					System.err.println(ex.getMessage());				
					// 出现IO异常（如客户端断开连接）时移除处理过的键
					keyIter.remove();
					key.channel().close();
					//UsersReffer.getInstace().clearUser(key); 移除服务端保留的此在线用户
					continue;
				}

				// 移除处理过的键
				keyIter.remove();
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

}