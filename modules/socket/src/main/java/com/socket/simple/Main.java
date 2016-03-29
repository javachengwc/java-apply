package com.socket.simple;

import com.util.date.SysDateTime;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		System.out.println("Server Start At:" + SysDateTime.getNow());

		//启动NIO socket
		NioTcpServer server = NioTcpServer.getInstance();
		server.init();
		server.startListen();
	}
}
