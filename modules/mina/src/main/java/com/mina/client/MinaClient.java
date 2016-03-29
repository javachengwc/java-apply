package com.mina.client;

import com.mina.server.RemoteInvokeResult;
import com.mina.service.ClientInvokeService;
import com.mina.service.impl.ClientInvokeServiceImpl;

public class MinaClient {

    public static void main(String args []) throws Exception
    {
        ClientInvokeService service = new ClientInvokeServiceImpl();

        service.invokeRemoteMethod(new SocketSender() {
            @Override
            public InvokeBean sender() {

                return null;
            }

            @Override
            public void received(Object message) throws Exception {

                RemoteInvokeResult remoteInvokeResult = (RemoteInvokeResult) message;
                int status = remoteInvokeResult.getStatus();
                if (status ==RemoteInvokeResult.SUCCESS) {
                    System.out.println("执行成功");
                } else if (status == RemoteInvokeResult.FAIL) {
                    System.out.println("执行失败");
                    Throwable throwable = remoteInvokeResult.getThrowable();
                    if (throwable != null) {
                        System.out.println("执行异常信息:" + throwable.getMessage());
                    }
                    return;
                }

                remoteInvokeResult.getObj();

            }
        },"127.0.0.1",10000);

    }
}
