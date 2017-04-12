package com.websocket.service;

import javax.websocket.Session;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

public class WebsocketSessionFactory {

    private static AtomicLong onlineCount =new AtomicLong(0);

    private static CopyOnWriteArraySet<WebsocketSession> sessionSet = new CopyOnWriteArraySet<WebsocketSession>();

    public static WebsocketSession addSession(Session session)
    {
        WebsocketSession webSession = new WebsocketSession();
        webSession.setSession(session);
        sessionSet.add(webSession);
        long curCnt= onlineCount.addAndGet(1);
        System.out.println("WebsocketSessionFactory addSession,onlineCount=" +curCnt);
        return webSession;
    }

    public static void closeSession(Session session) {
        sessionSet.remove(session);
        long curCnt = onlineCount.decrementAndGet();
        System.out.println("WebsocketSessionFactory closeSession,onlineCount=" +curCnt);
    }

    public static Set<WebsocketSession> getSessionSet() {
        return sessionSet;
    }
}
