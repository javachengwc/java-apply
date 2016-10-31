package com.mountain.util;

import com.util.http.UrlUtil;
import org.apache.commons.lang3.StringUtils;

public class SpecUrlUtil extends UrlUtil
{

    public static String getProtocal(String url)
    {
        int i = url.indexOf("://");
        if (i > 0) {
            return url.substring(0, i);
        } else {
            //file:/path/to/file.txt
            i = url.indexOf(":/");
            if(i>0) {
                return url.substring(0, i);
            }
        }
        return "";
    }

    public static String reduceProtocal(String url)
    {
        String u =url;
        int i = url.indexOf("://");
        int step=3;
        if(i<0)
        {
            i = url.indexOf(":/");
            step=2;
        }
        if(i<0)
        {
            return "";
        }
        u=u.substring(i+step);
        return u;
    }

    public static String getDomain(String url)
    {

        String u =reduceProtocal(url);
        String userpwd = getUserpwd(url);
        if(!StringUtils.isBlank(userpwd))
        {
            u = u.substring(userpwd.length()+1);
        }
        int i = u.indexOf(":");
        if(i>=0)
        {
            return u.substring(0,i);
        }
        int j = u.indexOf("/");
        if(j<0)
        {
            return u;
        }
        return u.substring(0,j);
    }

    public static int getPort(String url)
    {
        String u =reduceProtocal(url);
        String userpwd = getUserpwd(url);
        if(!StringUtils.isBlank(userpwd))
        {
            u = u.substring(userpwd.length());
        }
        int i = u.indexOf(":");
        if(i<0)
        {
            return 0;
        }
        u = u.substring(i+1);
        int j = u.indexOf("/");
        if(j<0)
        {
            int c = u.indexOf("?");
            if(c>0)
            {
                return Integer.parseInt(u.substring(0,c));
            }
            return Integer.parseInt(u);
        }
        return Integer.parseInt(u.substring(0,j));
    }


    public static  String getUserpwd(String url)
    {
        int k=url.indexOf("@");
        if(k<=0)
        {
            return "";
        }
        String u =reduceProtocal(url);
        k = u.indexOf("@");
        if (k > 0) {
            String userpwd = u.substring(0, k);
            int j = userpwd.indexOf(":");
            if (j > 0) {
                String password = userpwd.substring(j + 1);
                String username = userpwd.substring(0, j);
                return userpwd;
            }
        }
        return "";
    }

    public static String getPath(String url)
    {
        String u =reduceProtocal(url);
        int i = u.indexOf("/");
        if(i>=0)
        {
            u= u.substring(i+1);
            i= u.indexOf("?");
            if(i>0)
            {
                u=u.substring(0,i);
            }
            return u;
        }
        return "";
    }

    public static void main(String args [])
    {
        String url="http://aa:bb@127.0.0.1:100/cc?ab=cd";
        System.out.println(SpecUrlUtil.getProtocal(url));
        System.out.println(SpecUrlUtil.getDomain(url));
        System.out.println(SpecUrlUtil.getPath(url));
        System.out.println(SpecUrlUtil.getPort(url));
        System.out.println(SpecUrlUtil.getQueryString(url));
        System.out.println(SpecUrlUtil.getUserpwd(url));
    }
}