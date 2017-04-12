package com.websocket.controller;

import com.websocket.service.WebsocketSession;
import com.websocket.service.WebsocketSessionFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/websocket")
public class WebsocketController {

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session){
        WebsocketSessionFactory.addSession(session);
        System.out.println("WebsocketController onOpen......");
    }

    //连接关闭调用
    @OnClose
    public void onClose(Session session){
        WebsocketSessionFactory.closeSession(session);
        System.out.println("WebsocketController onClose......");
    }

    //收到消息后调用
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("WebsocketController onMessage invoked,message=" +message);
        for(WebsocketSession per: WebsocketSessionFactory.getSessionSet()){
            try {
                per.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("WebsocketController onError......");
        error.printStackTrace(System.out);
    }

}
