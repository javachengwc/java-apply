package com.mountain.model;

import com.mountain.constant.Constant;
import com.mountain.util.SpecUrlUtil;
import com.util.DeepCopyUtil;
import com.util.StringUtil;
import com.util.encrypt.EncodeUtil;
import com.util.net.NetUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 定制的url
 */
public class SpecUrl implements Serializable {

    private static final long serialVersionUID = -1985909830936569507L;

    private String urlStr;

    private String protocol;

    private String host;

    private int port;

    private String path;

    private Map<String, String> parameters;

    private String username;

    private String password;

    private String ip;

    public SpecUrl() {
    }

    public SpecUrl(String protocol, String host, int port) {
        this(protocol, host, port,null,null);
    }

    public SpecUrl(String protocol, String host, int port, Map<String, String> parameters) {
        this(protocol, host, port,null, parameters);
    }

    public SpecUrl(String protocol, String host, int port, String path) {
        this(protocol, host, port, path,null);
    }

    public SpecUrl(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this(protocol, host, port, path, parameters,null,null);
    }

    public SpecUrl(String protocol, String host, int port, String path, String username, String password) {
        this(protocol, host, port, path, null,username,password);
    }

    public SpecUrl(String protocol, String host, int port, String path, Map<String, String> parameters, String username, String password) {
        this.protocol = protocol;
        this.host = host;
        this.port = (port < 0 )? 0 : port;
        this.path = path;
        while(path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        this.parameters = Collections.unmodifiableMap(parameters);
        this.username = username;
        this.password = password;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return (port <= 0) ? host : (host + ":" + port);
    }

    public void setAddress(String address) {
        int i = address.lastIndexOf(':');
        if (i >= 0) {
            this.host = address.substring(0, i);
            this.port = Integer.parseInt(address.substring(i + 1));
        } else {
            this.host = address;
        }
    }

    public String getIp() {
        if (ip == null) {
            ip = NetUtil.getIpByHost(host);
        }
        return ip;
    }

    public String getAbsPath() {
        if (path != null && !path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }

    public String getParameter(String key) {
        return  parameters.get(key);
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        if (value == null || value=="") {
            return defaultValue;
        }
        return value;
    }

    public String[] getParameter(String key, String[] defaultValue) {
        String value = getParameter(key);
        if (value == null || value=="") {
            return defaultValue;
        }
        return Pattern.compile("\\s*[,]+\\s*").split(value);
    }

    public String getParameterWithDecoded(String key) {
        return getParameterWithDecoded(key, null);
    }

    public String getParameterWithDecoded(String key, String defaultValue) {
        String value =getParameter(key, defaultValue);
        if(StringUtils.isBlank(value))
        {
            return "";
        }
        try
        {
           return EncodeUtil.urlDecode(value);
        }catch(Exception e)
        {
            return value;
        }
    }

    public String getServiceName() {
        String serviceName=path;
        if(!StringUtils.isBlank(serviceName) && serviceName.startsWith("/"))
        {
            return serviceName.substring(1);
        }
        return serviceName;
    }

    public String getService() {
        String serviceName = getServiceName();
        if (StringUtils.isBlank(serviceName))
        {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        String group = getParameter(Constant.GROUP_KEY);
        if (!StringUtils.isBlank(group)) {
            buf.append(group).append("/");
        }
        buf.append(serviceName);
        String version = getParameter(Constant.VERSION_KEY);
        if (!StringUtils.isBlank(version)) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    public java.net.URL toJavaUrl() {
        try {
            return new java.net.URL(toString());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    /**
     * 解析url
     */
    public static SpecUrl valueOf(String url) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("SpecUrl valueOf url is null");
        }
        String protocol =SpecUrlUtil.getProtocal(url);
        String host = SpecUrlUtil.getDomain(url);
        int port = SpecUrlUtil.getPort(url);
        String path = SpecUrlUtil.getPath(url);
        Map<String, String> parameters = SpecUrlUtil.getParamMap(url);
        String username = null;
        String password = null;

        String userpwd=SpecUrlUtil.getUserpwd(url);
        int j = userpwd.indexOf(":");
        if (j >= 0) {
            username = userpwd.substring(0, j);
            password = userpwd.substring(j + 1);
        }
        SpecUrl urlObj= new SpecUrl(protocol, host, port, path, parameters,username, password);
        urlObj.setUrlStr(url);
        return urlObj;
    }

    public String toUrlStr()
    {
        if(StringUtils.isBlank(urlStr))
        {
            urlStr=buildString(true, true, false, false);
        }
        return urlStr;
    }

    public String buildString(boolean appendUser, boolean appendParameter, boolean useIP, boolean useService, String... parameters) {
        StringBuilder buf = new StringBuilder();
        //协议
        if (protocol != null && protocol.length() > 0) {
            buf.append(protocol);
            buf.append("://");
        }
        //账号
        if (appendUser && !StringUtils.isBlank(username)) {
            buf.append(username);
            if ( !StringUtils.isBlank(password))
            {
                buf.append(":").append(password);
            }
            buf.append("@");
        }
        //域名,端口
        String host1;
        if (useIP) {
            host1 = NetUtil.getIpByHost(getHost());
        } else {
            host1 = getHost();
        }
        if(!StringUtils.isBlank(host1)) {
            buf.append(host1);
            if (port > 0) {
                buf.append(":").append(port);
            }
        }
        //路径
        String path;
        if (useService) {
            path = getService();
        } else {
            path = getAbsPath();
        }
        if (!StringUtils.isBlank(path)) {
            if(!path.startsWith("/")) {
                buf.append("/");
            }
            buf.append(path);
        }
        //参数
        if (appendParameter) {
            String paramStr =buildParameters( parameters,true);
            buf.append(paramStr);
        }
        return buf.toString();
    }

    public String buildParameters(String[] parameters, boolean concat) {
        StringBuilder buf = new StringBuilder();
        if (getParameters() !=null && getParameters().size() > 0) {
            List<String> includes = (parameters == null || parameters.length == 0 )? null : Arrays.asList(parameters);
            boolean first = true;
            for (Map.Entry<String, String> entry :getParameters().entrySet()) {
                String key = entry.getKey();
                if (!StringUtils.isBlank(key) && (includes == null || includes.contains(key)) ) {
                    if (first) {
                        if (concat) {
                            buf.append("?");
                        }
                        first = false;
                    } else {
                        buf.append("&");
                    }
                    buf.append(key).append("=");
                    buf.append((entry.getValue() == null) ? "" : entry.getValue().trim());
                }
            }
        }
        return buf.toString();
    }

    public String getBackupAddress(int defPort) {
        StringBuilder address = new StringBuilder(appendDefPort(getAddress(), defPort));
        String[] backups = getParameter(Constant.BACKUP_KEY, new String[0]);
        if (backups != null && backups.length > 0) {
            for (String backup : backups) {
                address.append(",");
                address.append(appendDefPort(backup, defPort));
            }
        }
        return address.toString();
    }

    private String appendDefPort(String address, int defPort) {
        if (!StringUtils.isBlank(address)  && defPort > 0) {
            int i = address.indexOf(':');
            if (i < 0) {
                return address + ":" + defPort;
            } else if (Integer.parseInt(address.substring(i + 1)) == 0) {
                return address.substring(0, i + 1) + defPort;
            }
        }
        return address;
    }

    public SpecUrl genUrlWithParamAdd(String key,Object value)
    {
        Map<String,String> param = new HashMap<String,String>();
        param.put(key,(value==null)?"":value.toString());

        return genUrlWithParamAdd(param);
    }

    public SpecUrl genUrlWithParamAdd(Map<String,String> addParam)
    {
        if(addParam==null || addParam.size()<=0 )
        {
            SpecUrl newUrl=null;
            try {
                newUrl= DeepCopyUtil.deepCopy(this);
            }catch(Exception e)
            {
                newUrl = new SpecUrl(getProtocol(), getHost(), getPort(), getPath(), getParameters(),getUsername(),getPassword());
            }
            return newUrl;
        }
        Map<String,String> newParam = new HashMap<String,String>();
        if(getParameters()!=null)
        {
            newParam.putAll(getParameters());
        }
        newParam.putAll(addParam);
        SpecUrl newUrl = new SpecUrl(getProtocol(), getHost(), getPort(), getPath(), newParam,getUsername(),getPassword());
        return newUrl;
    }

    public SpecUrl genUrlWithParamDel(String ...params )
    {
        Map<String,String> paramMap = getParameters();
        if(params==null || params.length==0 || paramMap==null || paramMap.size()<=0 )
        {
            SpecUrl newUrl=null;
           try {
               newUrl= DeepCopyUtil.deepCopy(this);
           }catch(Exception e)
           {
               newUrl = new SpecUrl(getProtocol(), getHost(), getPort(), getPath(), paramMap,getUsername(),getPassword());
           }
            return newUrl;
        }

        Map<String,String> newParam = new HashMap<String,String>(paramMap);
        for(String per:params)
        {
            newParam.remove(per);
        }
        SpecUrl newUrl = new SpecUrl(getProtocol(), getHost(), getPort(), getPath(), newParam,getUsername(),getPassword());
        return newUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + port;
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        SpecUrl other = (SpecUrl) obj;
        boolean protocolEql = StringUtil.strEquals(protocol, other.getProtocol());
        boolean hostEql = StringUtil.strEquals(host, other.getHost());
        boolean portEql =(port == other.getPort());
        if(!protocolEql || !hostEql || !portEql)
        {
            return false;
        }
        boolean pathEql = StringUtil.strEquals(path, other.getPath());
        boolean usernameEql = StringUtil.strEquals(username, other.getUsername());
        boolean passwordEql = StringUtil.strEquals(password, other.getPassword());
        if(!pathEql || !usernameEql || !passwordEql)
        {
            return false;
        }
        int len =(parameters==null)?0:parameters.size();
        int otherLen =(other.getParameters()==null)?0:other.getParameters().size();
        if(len!=otherLen)
        {
            return false;
        }
        if(len>0)
        {
            for(String key:parameters.keySet())
            {
                if(!StringUtil.strEquals(parameters.get(key), other.getParameters().get(key)))
                {
                    return false;
                }
            }
        }
        return true;
    }

}