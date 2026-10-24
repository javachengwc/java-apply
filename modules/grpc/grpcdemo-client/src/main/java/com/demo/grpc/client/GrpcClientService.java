package com.demo.grpc.client;

import com.demo.grpc.HelloReply;
import com.demo.grpc.HelloRequest;
import com.demo.grpc.HelloServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class GrpcClientService {

    private static final Logger log = LoggerFactory.getLogger(GrpcClientService.class);

    @Autowired
    private HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    private long callTimeout = 30;

    public String sayHello(String name) {
        log.info("GrpcClientService sayHello name={}", name);
        try {
            HelloRequest request = HelloRequest.newBuilder()
                    .setName(name.trim())
                    .build();
            HelloServiceGrpc.HelloServiceBlockingStub stubWithDeadline = blockingStub
                    .withDeadlineAfter(callTimeout, TimeUnit.SECONDS);
            HelloReply response = stubWithDeadline.sayHello(request);
            log.info("GrpcClientService sayHello grpc call success, response={}", response.getMessage());
            return response.getMessage();
        } catch (Exception e) {
            log.error("GrpcClientService sayHello error during grpc call", e);
            throw new RuntimeException("grpc call error" + e.getMessage(), e);
        }
    }
}