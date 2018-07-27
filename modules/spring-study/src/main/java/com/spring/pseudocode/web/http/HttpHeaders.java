package com.spring.pseudocode.web.http;

import com.spring.pseudocode.core.util.MultiValueMap;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.io.Serializable;
import java.util.*;

public class HttpHeaders implements MultiValueMap<String, String>, Serializable {

    private static TimeZone GMT = TimeZone.getTimeZone("GMT");

    private Map<String, List<String>> headers;

    public HttpHeaders()
    {

    }

    private HttpHeaders(Map<String, List<String>> headers, boolean readOnly)
    {
        if (readOnly)
        {
            Map map = new LinkedCaseInsensitiveMap(headers
                    .size(), Locale.ENGLISH);
            for (Map.Entry entry : headers.entrySet()) {
                List values = Collections.unmodifiableList((List)entry.getValue());
                map.put(entry.getKey(), values);
            }
            this.headers = Collections.unmodifiableMap(map);
        }
        else {
            this.headers = headers;
        }
    }


    public String getFirst(String paramK) {
        return null;
    }

    public void add(String paramK, String paramV) {

    }

    public void set(String paramK, String paramV) {

    }

    public void setAll(Map<String, String> paramMap) {

    }

    public Map<String, String> toSingleValueMap() {
        return null;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public List<String> get(Object key) {
        return null;
    }

    public List<String> put(String key, List<String> value) {
        return null;
    }

    public List<String> remove(Object key) {
        return null;
    }

    public void putAll(Map<? extends String, ? extends List<String>> m) {

    }

    public void clear() {

    }

    public Set<String> keySet() {
        return null;
    }

    public Collection<List<String>> values() {
        return null;
    }

    public Set<Entry<String, List<String>>> entrySet() {
        return null;
    }

    public long getContentLength()
    {
        String value = getFirst("Content-Length");
        return value != null ? Long.parseLong(value) : -1L;
    }

    public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers)
    {
        return new HttpHeaders(headers, true);
    }
}
