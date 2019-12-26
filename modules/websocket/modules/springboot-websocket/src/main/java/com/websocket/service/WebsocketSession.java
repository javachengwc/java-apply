package com.websocket.service;

import javax.websocket.Session;
import java.io.IOException;

public class WebsocketSession {

    private String name;

    private Session session;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
