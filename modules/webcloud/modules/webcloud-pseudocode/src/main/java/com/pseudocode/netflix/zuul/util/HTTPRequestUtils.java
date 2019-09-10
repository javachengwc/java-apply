package com.pseudocode.netflix.zuul.util;

import com.pseudocode.netflix.zuul.context.RequestContext;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

public class HTTPRequestUtils {

    private final static HTTPRequestUtils INSTANCE = new HTTPRequestUtils();

    public static final String X_FORWARDED_FOR_HEADER = "x-forwarded-for";

    public String getClientIP(HttpServletRequest request) {
        final String xForwardedFor = request.getHeader(X_FORWARDED_FOR_HEADER);
        String clientIP = null;
        if (xForwardedFor == null) {
            clientIP = request.getRemoteAddr();
        } else {
            clientIP = extractClientIpFromXForwardedFor(xForwardedFor);
        }
        return clientIP;
    }

    public final String extractClientIpFromXForwardedFor(String xForwardedFor) {
        if (xForwardedFor == null) {
            return null;
        }
        xForwardedFor = xForwardedFor.trim();
        String tokenized[] = xForwardedFor.split(",");
        if (tokenized.length == 0) {
            return null;
        } else {
            return tokenized[0].trim();
        }
    }

    public static HTTPRequestUtils getInstance() {
        return INSTANCE;
    }

    public String getHeaderValue(String sHeaderName) {
        return RequestContext.getCurrentContext().getRequest().getHeader(sHeaderName);
    }

    public String getFormValue(String sHeaderName) {
        return RequestContext.getCurrentContext().getRequest().getParameter(sHeaderName);
    }

    public Map<String, List<String>> getRequestHeaderMap() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        Map<String,List<String>> headers = new HashMap<String,List<String>>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if(headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);

                if(name != null && !name.isEmpty() && value != null) {
                    List<String> valueList = new ArrayList<String>();
                    if(headers.containsKey(name)) {
                        headers.get(name).add(value);
                    }
                    valueList.add(value);
                    headers.put(name, valueList);
                }
            }
        }
        return Collections.unmodifiableMap(headers);

    }

    public Map<String, List<String>> getQueryParams() {

        Map<String, List<String>> qp = RequestContext.getCurrentContext().getRequestQueryParams();
        if (qp != null) return qp;

        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        qp = new LinkedHashMap<String, List<String>>();

        if (request.getQueryString() == null) return null;
        StringTokenizer st = new StringTokenizer(request.getQueryString(), "&");
        int i;

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            i = s.indexOf("=");
            if (i > 0 && s.length() >= i + 1) {
                String name = s.substring(0, i);
                String value = s.substring(i + 1);

                try {
                    name = URLDecoder.decode(name, "UTF-8");
                } catch (Exception e) {
                }
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (Exception e) {
                }

                List<String> valueList = qp.get(name);
                if (valueList == null) {
                    valueList = new LinkedList<String>();
                    qp.put(name, valueList);
                }

                valueList.add(value);
            }
            else if (i == -1)
            {
                String name=s;
                String value="";
                try {
                    name = URLDecoder.decode(name, "UTF-8");
                } catch (Exception e) {
                }

                List<String> valueList = qp.get(name);
                if (valueList == null) {
                    valueList = new LinkedList<String>();
                    qp.put(name, valueList);
                }

                valueList.add(value);

            }
        }

        RequestContext.getCurrentContext().setRequestQueryParams(qp);
        return qp;
    }

    public String getValueFromRequestElements(String sName) {
        String sValue = null;
        if (getQueryParams() != null) {
            final List<String> v = getQueryParams().get(sName);
            if (v != null && !v.isEmpty()) sValue = v.iterator().next();
        }
        if (sValue != null) return sValue;
        sValue = getHeaderValue(sName);
        if (sValue != null) return sValue;
        sValue = getFormValue(sName);
        if (sValue != null) return sValue;
        return null;
    }

    public boolean isGzipped(String contentEncoding) {
        return contentEncoding.contains("gzip");
    }

}
