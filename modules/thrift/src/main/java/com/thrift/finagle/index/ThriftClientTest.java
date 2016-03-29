package com.thrift.finagle.index;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClientTest {

	/**
	 * @param args
	 * @throws org.apache.thrift.TException
	 */
	public static void main(String[] args) throws TException {
		// TODO Auto-generated method stub
		TTransport transport = new TSocket("127.0.0.1", 9813);
		long start=System.currentTimeMillis();
//		TTransport transport = new TSocket("218.11.178.110",9090);
        TProtocol protocol = new TBinaryProtocol(transport);
        IndexNewsOperatorServices.Client client=new IndexNewsOperatorServices.Client(protocol);
        transport.open();
        client.deleteArtificiallyNews(123456);
        NewsModel newsModel=new NewsModel();
        newsModel.setId(789456);
        newsModel.setTitle("this from java client");
        newsModel.setContent("　世界杯比赛前，由于塞尔维亚和黑山突然宣布分裂，国际足联开会决定剔除塞黑，由世界上球迷最多的国家顶替，名额恰巧来到中国。举国上下一片欢腾，中国足协决定由“成世铎”（成龙+阎世铎）组队，进军世界杯。");
        newsModel.setAuthor("ddc");
        newsModel.setMedia_from("新华08");
        client.indexNews(newsModel);
        transport.close();
        System.out.println((System.currentTimeMillis()-start));
        System.out.println("client sucess!");
	}

}
