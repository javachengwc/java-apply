package com.demo.controller;


import com.demo.grpc.client.GrpcClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/grpc")
public class GrpcController {

    @Autowired
    private GrpcClientService grpcClientService;

    @GetMapping("/sayHello")
    public ResponseEntity<Map<String, Object>> sayHello(String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = grpcClientService.sayHello(name);
            response.put("success", true);
            response.put("data", result);
            response.put("message", "grpc call succeeded");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(502).body(response);
        }
    }
}