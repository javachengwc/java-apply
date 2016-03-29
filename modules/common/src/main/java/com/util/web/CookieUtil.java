package com.util.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

   /**
    * 保存Cookie
    * @param times                多少秒过期 , 负数表示不保存
    */
   public static void setCookie(HttpServletResponse response, String name, String value,
                                String domain, String path, int times) {
       final Cookie cookie = new Cookie(name, value);
       //cookie.setSecure(false);
       cookie.setDomain(domain);
       cookie.setPath(path);
       if (times > 0) {
           cookie.setMaxAge(times);
       }
       response.addCookie(cookie);
   }

    public static Cookie makeCookie(String name, String value, String path, String domain, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setDomain( domain);
        if (expiry > 0) {
            cookie.setMaxAge(expiry);
        }
        return cookie;
    }

   /**
    * 获取Cookie
    */
   public static Cookie getCookie(HttpServletRequest request, String cookieName) {
       Cookie retCookie = null;
       Cookie[] cookies = request.getCookies();
       if (cookies == null) {
           return null;
       }
       for (Cookie cookie : cookies) {
           String cookiePath = cookie.getPath();
           if ((cookiePath == null || cookiePath.equals("/"))
                   &&  cookieName.trim().equalsIgnoreCase(cookie.getName().trim())) {
               retCookie = cookie;
               break;
           }
       }
       return retCookie;
   }

    public static void deleteCookie(final HttpServletResponse response, final Cookie cookie, final String path) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }
}
