package com.util.web;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 无数据绑定功能也能使用
     * @param <T>
     * @param request
     * @param beanType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T requestParametersWrapper(HttpServletRequest request, Class<T> beanType) {
        try {
            Enumeration<String> parameterNames = request.getParameterNames();
            String parameterName, parameterValue;
            T bean = beanType.newInstance();
            // Map<String, String> parametersMap = new HashMap<String, String>();
            while (parameterNames.hasMoreElements()) {
                parameterName = parameterNames.nextElement();
                parameterValue= request.getParameter(parameterName);
                BeanUtils.setProperty(bean, parameterName, parameterValue);
            }
            // BeanUtils.populate(bean, parametersMap);
            return bean;
        } catch (Exception e) {
            LOGGER.error("request parameters & " + beanType.getName() + " bond exception! " + e.getMessage(), e);
        }
        return null;
    }

    public static String getIpFromRequest(HttpServletRequest request) {
        String ipStr = request.getHeader("HTTP_X_FORWARDED_FOR");

        if (StringUtils.isBlank(ipStr)) {
            ipStr = request.getHeader("X-Real-IP");

        }

        if (StringUtils.isBlank(ipStr)) {
            ipStr = request.getRemoteAddr();
        }

        if (StringUtils.isNotEmpty(ipStr)) {
            if (ipStr.split(",").length > 0) {
                ipStr = ipStr.split("\\,")[0];
            }
        }
        return ipStr;
    }

    public static String getIpAddr(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");

        if(ip!=null && ip.contains(","))
        {
            String[] ips = ip.split(",");
            ip = ips[ips.length-1];
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return ip.trim();
    }

    /**
     * 获得项目跟目录
     */
    public static String getBasePath(HttpServletRequest request){
        String path = request.getContextPath();
        String basePath = "";
        if (!"80".equals(request.getServerPort()+"")) {
            basePath = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + path;
        }else{
            basePath = request.getScheme() + "://" + request.getServerName() + path ;
        }
        return basePath;
    }

    public static String getParameter(HttpServletRequest request, String param) {
        return request.getParameter(param);
    }

    public static String getParameter(HttpServletRequest request, String param, String defaultValue) {
        String ret = request.getParameter(param);
        if (ret == null)
            ret = defaultValue;
        if(ret!=null)
        {
            ret =ret.trim();
        }
        return ret;

    }

    public static int getParameterInt(HttpServletRequest request, String param) {
        return Integer.parseInt(getParameter(request, param));
    }

    public static int getParameterInt(HttpServletRequest request, String param, int defaultValue) {
        String inputStr = getParameter(request, param, "");
        if (inputStr.length() == 0) {
            return defaultValue;
        }
        int ret;
        try {
            ret = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return ret;
    }

    public static Integer[] getIntArrayParam(HttpServletRequest request, String param, String spliter) {
        String value = request.getParameter(param);
        if (StringUtils.isBlank(value))
            return null;
        String regexp = spliter == null ? "," : spliter;
        String[] values = value.split(regexp);
        Integer[] result = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            String s = values[i];
            try {
                result[i] = Integer.valueOf(s);
            } catch (Exception e) {
                continue;
            }
        }
        if (result.length == 0)
            return null;
        return result;
    }

    public static String[] getStrArrayParam(HttpServletRequest request, String param, String spliter) {
        String value = request.getParameter(param);
        if (StringUtils.isBlank(value))
            return null;
        String regexp = spliter == null ? "," : spliter;
        String[] values = value.split(regexp);
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            String s = values[i];
            if (StringUtils.isBlank(s))
                continue;
            result[i] = s;
        }
        if (result.length == 0)
            return null;
        return result;
    }

    public static long getParameterLong(HttpServletRequest request, String param) {
        return Long.parseLong(getParameter(request, param));
    }

    public static long getParameterLong(HttpServletRequest request, String param, long defaultValue) {
        String inputStr = getParameter(request, param, "");
        if (inputStr.length() == 0) {
            return defaultValue;
        }
        long ret;
        try {
            ret = Long.parseLong(inputStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return ret;
    }

    public static boolean getParameterBoolean(HttpServletRequest request, String param) {
        String inputStr = getParameter(request, param, "");
        if (inputStr.length() == 0)
            return false;
        if ("0".equals(inputStr.trim()) || "false".equals(inputStr.trim()) || "no".equals(inputStr.trim())
                || "f".equals(inputStr.trim()))
            return false;
        if ("1".equals(inputStr.trim()) || "true".equals(inputStr.trim()) || "yes".equals(inputStr.trim())
                || "t".equals(inputStr.trim()))
            return true;
        return false;
    }

    public static boolean getParameterBoolean(HttpServletRequest request, String param, boolean defaultValue) {
        String inputStr = getParameter(request, param, "");
        if (inputStr.length() == 0)
            return defaultValue;
        if ("0".equals(inputStr.trim()) || "false".equals(inputStr.trim()) || "no".equals(inputStr.trim())
                || "f".equals(inputStr.trim()))
            return false;
        if ("1".equals(inputStr.trim()) || "true".equals(inputStr.trim()) || "yes".equals(inputStr.trim())
                || "t".equals(inputStr.trim()))
            return true;
        return defaultValue;
    }

    public static double getParameterDouble(HttpServletRequest request, String param) {
        return Double.parseDouble(getParameter(request, param));
    }

    public static double getParameterDouble(HttpServletRequest request, String param, long defaultValue) {
        String inputStr = getParameter(request, param, "");
        if (inputStr.length() == 0) {
            return defaultValue;
        }
        double ret;
        try {
            ret = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
        return ret;
    }

    /**
     * 读取url流转换为字符串,可以用来生成静态网页
     * @param url
     * @param characterSet
     * @return
     */
    public static final String readHtml(final String url,String characterSet) {
        if (org.apache.commons.lang.StringUtils.isBlank(url)) {
            return null;
        }
        Pattern pattern = Pattern
                .compile("(http://|https://){1}[\\w\\.\\-/:]+");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            URL _url = new URL(url);
            URLConnection urlConnection = _url.openConnection();
            InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream(), characterSet);
            BufferedReader in = new BufferedReader(isr);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
