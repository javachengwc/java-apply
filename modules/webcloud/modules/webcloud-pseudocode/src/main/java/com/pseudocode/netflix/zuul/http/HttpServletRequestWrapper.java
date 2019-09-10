package com.pseudocode.netflix.zuul.http;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.util.HTTPRequestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper
{
    private static final HashMap<String, String[]> EMPTY_MAP = new HashMap();
    protected static final Logger LOG = LoggerFactory.getLogger(HttpServletRequestWrapper.class);
    private HttpServletRequest req;
    private byte[] contentData = null;
    private HashMap<String, String[]> parameters = null;

    private long bodyBufferingTimeNs = 0L;

    public HttpServletRequestWrapper() {
        super(groovyTrick());
    }

    private static HttpServletRequest groovyTrick()
    {
        throw new IllegalArgumentException("Please use HttpServletRequestWrapper(HttpServletRequest request) constructor!");
    }

    private HttpServletRequestWrapper(HttpServletRequest request, byte[] contentData, HashMap<String, String[]> parameters) {
        super(request);
        this.req = request;
        this.contentData = contentData;
        this.parameters = parameters;
    }

    public HttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.req = request;
    }

    public HttpServletRequest getRequest()
    {
        try
        {
            parseRequest();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse the request!", e);
        }
        return this.req;
    }

    public byte[] getContentData()
    {
        return this.contentData;
    }

    public HashMap<String, String[]> getParameters()
    {
        if (this.parameters == null) return EMPTY_MAP;
        HashMap map = new HashMap(this.parameters.size() * 2);
        for (String key : this.parameters.keySet()) {
            map.put(key, ((String[])this.parameters.get(key)).clone());
        }
        return map;
    }

    private void parseRequest() throws IOException {
        if (this.parameters != null) {
            return;
        }
        HashMap<String,List> mapA = new HashMap<String,List>();

        Map<String,List<String>> query = HTTPRequestUtils.getInstance().getQueryParams();
        if (query != null)
            for (String key : query.keySet()) {
                List list = (List)query.get(key);
                mapA.put(key, list);
            }
        boolean isPost;
        if (shouldBufferBody())
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try
            {
                long bufferStartTime = System.nanoTime();
                IOUtils.copy(this.req.getInputStream(), baos);
                this.bodyBufferingTimeNs = (System.nanoTime() - bufferStartTime);

                this.contentData = baos.toByteArray();
            }
            catch (SocketTimeoutException e)
            {
                LOG.error("SocketTimeoutException reading request body from inputstream. error=" + String.valueOf(e.getMessage()));
                if (this.contentData == null) {
                    this.contentData = new byte[0];
                }
            }
            try
            {
                LOG.debug("Length of contentData byte array = " + this.contentData.length);
                if (this.req.getContentLength() != this.contentData.length)
                    LOG.warn("Content-length different from byte array length! cl=" + this.req.getContentLength() + ", array=" + this.contentData.length);
            }
            catch (Exception e) {
                LOG.error("Error checking if request body gzipped!", e);
            }

            isPost = this.req.getMethod().equals("POST");

            String contentType = this.req.getContentType();
            boolean isFormBody = (contentType != null) && (contentType.contains("application/x-www-form-urlencoded"));

            if ((isPost) && (isFormBody)) {
                String enc = this.req.getCharacterEncoding();

                if (enc == null) enc = "UTF-8";
                String s = new String(this.contentData, enc);
                StringTokenizer st = new StringTokenizer(s, "&");

                boolean decode = this.req.getContentType() != null;
                while (st.hasMoreTokens()) {
                    s = st.nextToken();
                    int i = s.indexOf("=");
                    if ((i > 0) && (s.length() > i + 1)) {
                        String name = s.substring(0, i);
                        String value = s.substring(i + 1);
                        if (decode) {
                            try {
                                name = URLDecoder.decode(name, "UTF-8");
                            } catch (Exception localException1) {
                            }
                            try {
                                value = URLDecoder.decode(value, "UTF-8");
                            } catch (Exception localException2) {
                            }
                        }
                        List list = (List)mapA.get(name);
                        if (list == null) {
                            list = new LinkedList();
                            mapA.put(name, list);
                        }
                        list.add(value);
                    }
                }
            }
        }

        HashMap map = new HashMap(mapA.size() * 2);
        for (String key : mapA.keySet()) {
            List list = (List)mapA.get(key);
            map.put(key, list.toArray(new String[list.size()]));
        }

        this.parameters = map;
    }

    private boolean shouldBufferBody()
    {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Path = " + this.req.getPathInfo());
            LOG.debug("Transfer-Encoding = " + String.valueOf(this.req.getHeader("transfer-encoding")));
            LOG.debug("Content-Encoding = " + String.valueOf(this.req.getHeader("Content-Encoding")));
            LOG.debug("Content-Length header = " + this.req.getContentLength());
        }

        boolean should = false;
        if (this.req.getContentLength() > 0) {
            should = true;
        }
        else if (this.req.getContentLength() == -1) {
            String transferEncoding = this.req.getHeader("transfer-encoding");
            if ((transferEncoding != null) && (transferEncoding.equals("chunked"))) {
                RequestContext.getCurrentContext().setChunkedRequestBody();
                should = true;
            }
        }

        return should;
    }

    public long getBodyBufferingTimeNs()
    {
        return this.bodyBufferingTimeNs;
    }

    public ServletInputStream getInputStream() throws IOException
    {
        parseRequest();
//        return new ServletInputStreamWrapper(this.contentData);
        return null;
    }

    public BufferedReader getReader() throws IOException
    {
        parseRequest();
        String enc = this.req.getCharacterEncoding();
        if (enc == null)
            enc = "UTF-8";
        byte[] data = this.contentData;
        if (data == null)
            data = new byte[0];
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), enc));
    }

    public String getParameter(String name)
    {
        try
        {
            parseRequest();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse the request!", e);
        }
        if (this.parameters == null) return null;
        String[] values = (String[])this.parameters.get(name);
        if ((values == null) || (values.length == 0))
            return null;
        return values[0];
    }

    public Map getParameterMap()
    {
        try
        {
            parseRequest();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse the request!", e);
        }
        return getParameters();
    }

    public Enumeration getParameterNames()
    {
        try
        {
            parseRequest();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse the request!", e);
        }
        return new Enumeration() {
            private String[] arr = (String[])HttpServletRequestWrapper.this.getParameters().keySet().toArray(new String[0]);
            private int idx = 0;

            public boolean hasMoreElements()
            {
                return this.idx < this.arr.length;
            }

            public String nextElement()
            {
                return this.arr[(this.idx++)];
            }
        };
    }

    public String[] getParameterValues(String name)
    {
        try
        {
            parseRequest();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse the request!", e);
        }
        if (this.parameters == null) return null;
        String[] arr = (String[])this.parameters.get(name);
        if (arr == null)
            return null;
        return (String[])arr.clone();
    }
}
