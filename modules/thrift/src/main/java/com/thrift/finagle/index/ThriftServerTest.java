package com.thrift.finagle.index;

import java.net.InetSocketAddress;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

public class ThriftServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndexNewsOperatorServices.Processor processor = new IndexNewsOperatorServices.Processor(new IndexNewsOperatorServicesImpl());
		try{
			TServerTransport serverTransport = new TServerSocket( new InetSocketAddress("0.0.0.0",9813));
			Args trArgs=new Args(serverTransport);
			trArgs.processor(processor);
			//使用二进制来编码应用层的数据
			trArgs.protocolFactory(new TBinaryProtocol.Factory(true, true));
			//使用普通的socket来传输数据
			trArgs.transportFactory(new TTransportFactory());
			TServer server = new TThreadPoolServer(trArgs);
			System.out.println("server begin ......................");
			server.serve();
			System.out.println("---------------------------------------");
			server.stop();
		}catch(Exception e){
			throw new RuntimeException("index thrift server start failed!!"+"/n"+e.getMessage());
		}

	}

}
