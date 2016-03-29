package com.configcenter.service;

import com.configcenter.vo.OnlineUser;
import com.util.date.SysDateTime;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理器
 */
public class SessionManager {

    private static SessionManager inst = new SessionManager();

    public static SessionManager getInstance()
    {
        return inst;
    }

    //在线用户 账号-->用户信息
    private ConcurrentHashMap<String,OnlineUser> onlineUserMap = new ConcurrentHashMap<String, OnlineUser>();

    private static ThreadLocal<OnlineUser> curUser=new ThreadLocal<OnlineUser>();

    private SessionManager()
    {
        init();
    }

    //初始化
    public void init()
    {

       //暂且什么都不做
    }

    //添加会话
    public void addSession(OnlineUser onlineUser)
    {
        OnlineUser user = onlineUserMap.get(onlineUser.getAccount());
        if(user!=null)
        {
            int loginCount =user.getLoginCount();
            loginCount++;
            onlineUser.setLoginCount(loginCount);

        }
        onlineUserMap.put(onlineUser.getAccount(), onlineUser);
    }

    //删除会话
    public void delSession(String account)
    {
        OnlineUser user = onlineUserMap.get(account);
        if(user!=null)
        {
            int loginCount =user.getLoginCount();
            loginCount--;
            if(loginCount<=0)
            {
                onlineUserMap.remove(account);
            }else
            {
                user.setLoginCount(loginCount);
            }
        }

    }

    //心跳
    public void heartbeat(String account)
    {
        OnlineUser user = onlineUserMap.get(account);
        if(user!=null)
        {
            user.setLastedHeartbeat(SysDateTime.getNow());
        }
    }

    //获取在线账户数
    public int getOnlineCount()
    {
        return onlineUserMap.size();
    }

    //获取在线账户
    public OnlineUser getOnlineUser(String account)
    {
        return onlineUserMap.get(account);
    }

    //设置当前用户
    public static void setCurUser(OnlineUser onlineUser)
    {
        curUser.set(onlineUser);
    }

    public static OnlineUser getCurUser()
    {
        return curUser.get();
    }

    //清除当前用户
    public static void cleanCurUser()
    {
        curUser.remove();
    }

}
