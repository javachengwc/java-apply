package com.rule.data.handler;


import com.rule.data.service.login.LoginHandler;
import com.rule.data.service.preview.PreviewHandler;
import com.rule.data.service.query.ServiceHandler;
import com.rule.data.service.query.ServiceListHandler;
import com.rule.data.service.userlist.UserListHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置处理特定uri的handler
 */
public final class Handlers {

    private static Map<String, Handler> handlers = new HashMap<String, Handler>();

    static {
        handlers.put("/service", new ServiceHandler());
        handlers.put("/servicelist", new ServiceListHandler());
        handlers.put("/login", new LoginHandler());
        handlers.put("/preview", new PreviewHandler());
        handlers.put("/userlist", new UserListHandler());
    }

    private Handlers() {
    }

    public static Handler getHandler(String path) {
        return handlers.get(path);
    }
}
