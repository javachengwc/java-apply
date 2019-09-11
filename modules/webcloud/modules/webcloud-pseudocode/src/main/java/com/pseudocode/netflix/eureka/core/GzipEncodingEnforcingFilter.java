package com.pseudocode.netflix.eureka.core;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

//GZIP 编码过滤器
public class GzipEncodingEnforcingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if ("GET".equals(httpRequest.getMethod())) {
            String acceptEncoding = httpRequest.getHeader(HttpHeaders.ACCEPT_ENCODING);
            if (acceptEncoding == null) {
                chain.doFilter(addGzipAcceptEncoding(httpRequest), response);
                return;
            }
            if (!acceptEncoding.contains("gzip")) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private static HttpServletRequest addGzipAcceptEncoding(HttpServletRequest request) {
        return new HttpServletRequestWrapper(request) {

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (HttpHeaders.ACCEPT_ENCODING.equals(name)) {
                    return new EnumWrapper<String>("gzip");
                }
                return new EnumWrapper<String>(super.getHeaders(name), HttpHeaders.ACCEPT_ENCODING);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return new EnumWrapper<String>(super.getHeaderNames(), HttpHeaders.ACCEPT_ENCODING);
            }

            @Override
            public String getHeader(String name) {
                if (HttpHeaders.ACCEPT_ENCODING.equals(name)) {
                    return "gzip";
                }
                return super.getHeader(name);
            }
        };
    }

    private static class EnumWrapper<E> implements Enumeration<E> {

        private final Enumeration<E> delegate;
        private final AtomicReference<E> extraElementRef;

        private EnumWrapper(E extraElement) {
            this(null, extraElement);
        }

        private EnumWrapper(Enumeration<E> delegate, E extraElement) {
            this.delegate = delegate;
            this.extraElementRef = new AtomicReference<>(extraElement);
        }

        @Override
        public boolean hasMoreElements() {
            return extraElementRef.get() != null || delegate != null && delegate.hasMoreElements();
        }

        @Override
        public E nextElement() {
            E extra = extraElementRef.getAndSet(null);
            if (extra != null) {
                return extra;
            }
            if (delegate == null) {
                throw new NoSuchElementException();
            }
            return delegate.nextElement();
        }
    }
}

