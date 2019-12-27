package com.websocket.service;

import java.util.Objects;
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

        if(!session.isOpen()){
            System.out.println("session is not open ----------------");
        } else {
            session.getBasicRemote().sendText(message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WebsocketSession)) {
            return false;
        }
        WebsocketSession that = (WebsocketSession) o;
        return Objects.equals(getName(), that.getName()) &&
            Objects.equals(getSession(), that.getSession());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSession());
    }
}
