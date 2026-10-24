package com.demo.grpc;

import com.alibaba.fastjson.JSONObject;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
@GrpcService
@Slf4j
public class HelloServiceGrpcImpl extends HelloServiceGrpc.HelloServiceImplBase{

    @Override
    public void sayHello(HelloRequest request,
                        StreamObserver<HelloReply> responseObserver) {
        log.info("sayHello param request={}", JSONObject.toJSONString(request));
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello " + request.getName()+", reply by grpc server")
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
