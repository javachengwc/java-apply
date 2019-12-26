package com.websocket.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

public class WebsocketSessionFactory {

    private static AtomicLong onlineCount =new AtomicLong(0);

    private static ConcurrentHashMap<Session,WebsocketSession> sessionMap = new ConcurrentHashMap<Session,WebsocketSession>();

    public static WebsocketSession addSession(Session session,String name)
    {
        WebsocketSession webSession = new WebsocketSession();
        webSession.setName(name);
        webSession.setSession(session);
        sessionMap.put(session,webSession);
        long curCnt= onlineCount.addAndGet(1);
        System.out.println("WebsocketSessionFactory addSession,onlineCount=" +curCnt);
        return webSession;
    }

    public static void closeSession(Session session) {
        sessionMap.remove(session);
        long curCnt = onlineCount.decrementAndGet();
        System.out.println("WebsocketSessionFactory closeSession,onlineCount=" +curCnt);
    }

    public static Collection<WebsocketSession> getSessionCollection() {
        System.out.println("sessionMap size ="+sessionMap.size());
        return sessionMap.values();
    }

    public static long getOnlineCount() {
        return onlineCount.get();
    }

}
