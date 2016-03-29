package com.rule.data.util;

import org.jboss.netty.handler.codec.http.HttpRequest;

public class RequestIpUtil {

    public static String getNativeIp(HttpRequest request) {
        try {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("http_client_ip");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            // 如果是多级代理，那么取第一个ip为客户ip
            if (ip != null) {
                String[] ips = ip.split(",");
                if (ips != null) {
                    if (ips.length > 0) {
                        ip = ips[0];
                    }
                }
            }
            return ip;
        } catch (Exception e) {
            return "获取ip异常";
        }
    }

    public static String getNativeIpByHttp(HttpRequest request) {
        try {
            String value = request.getHeader("HTTP_X_FORWARDED_FOR");
            String ip = "";
            if (value != null) {
                if (!"unknown".equalsIgnoreCase(value)) {
                    String ipFromNginx = value;
                    if (ipFromNginx != null) {
                        String[] ips = ipFromNginx.split(",");
                        if (ips != null) {
                            if (ips.length > 0) {
                                ip = ips[0];
                            }
                        }
                    }
                }
            }
            if ("".equals(ip)) {
                return null;
            } else {
                return ip;
            }
        } catch (Exception e) {
            return "获取ip异常";
        }

    }
}
