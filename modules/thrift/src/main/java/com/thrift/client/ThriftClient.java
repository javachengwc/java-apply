package com.thrift.client;

import com.thrift.finagle.trans.Result;
import com.thrift.finagle.trans.StringResult;
import com.thrift.finagle.trans.TransDealService;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import org.apache.thrift.protocol.TBinaryProtocol;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * thrift客户端
 */
public class ThriftClient {

    public static void main(String args[]) throws Exception
    {
        String host="127.0.0.1";
        int port = 17001;

        com.twitter.finagle.Service<ThriftClientRequest, byte[]> service = ClientBuilder.safeBuild(ClientBuilder.get()
                .hosts(new InetSocketAddress(host, port))
                .codec(ThriftClientFramedCodec.get())
                .hostConnectionLimit(3)
                .retries(3));

        TransDealService.ServiceToClient client = new TransDealService.ServiceToClient(service, new TBinaryProtocol.Factory());

        StringResult stringResult =client.transDeal(1).toJavaFuture().get();

        if(stringResult!=null)
        {
            System.out.println("-----stringResult:"+stringResult.getValue()+","+stringResult.getExtend());
        }else
        {
            System.out.println("-----stringResult is null");
        }

        List<Integer> dealIdList= new ArrayList<Integer>();
        dealIdList.add(1);
        dealIdList.add(2);


        Result result =client.batchTransDeal(dealIdList).toJavaFuture().get();

        if(result!=null)
        {
            System.out.println("-----result.getCode()="+result.getCode());
        }else
        {
            System.out.println("-----result is null");
        }

        service.close();

    }
}
