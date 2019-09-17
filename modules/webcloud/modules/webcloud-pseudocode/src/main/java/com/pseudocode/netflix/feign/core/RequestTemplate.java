package com.pseudocode.netflix.feign.core;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.pseudocode.netflix.feign.core.Util.CONTENT_LENGTH;
import static com.pseudocode.netflix.feign.core.Util.UTF_8;
import static com.pseudocode.netflix.feign.core.Util.checkArgument;
import static com.pseudocode.netflix.feign.core.Util.checkNotNull;
import static com.pseudocode.netflix.feign.core.Util.emptyToNull;
import static com.pseudocode.netflix.feign.core.Util.toArray;
import static com.pseudocode.netflix.feign.core.Util.valuesOrEmpty;

//请求模板
public final class RequestTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Map<String, Collection<String>> queries = new LinkedHashMap<String, Collection<String>>();
    private final Map<String, Collection<String>> headers = new LinkedHashMap<String, Collection<String>>();
    private String method;
    /* final to encourage mutable use vs replacing the object. */
    private StringBuilder url = new StringBuilder();
    private transient Charset charset;
    private byte[] body;
    private String bodyTemplate;
    private boolean decodeSlash = true;

    public RequestTemplate() {
    }

    public RequestTemplate(RequestTemplate toCopy) {
        checkNotNull(toCopy, "toCopy");
        this.method = toCopy.method;
        this.url.append(toCopy.url);
        this.queries.putAll(toCopy.queries);
        this.headers.putAll(toCopy.headers);
        this.charset = toCopy.charset;
        this.body = toCopy.body;
        this.bodyTemplate = toCopy.bodyTemplate;
        this.decodeSlash = toCopy.decodeSlash;
    }

    private static String urlDecode(String arg) {
        try {
            return URLDecoder.decode(arg, UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static String urlEncode(Object arg) {
        try {
            return URLEncoder.encode(String.valueOf(arg), UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isHttpUrl(CharSequence value) {
        return value.length() >= 4 && value.subSequence(0, 3).equals("http".substring(0,  3));
    }

    private static CharSequence removeTrailingSlash(CharSequence charSequence) {
        if (charSequence != null && charSequence.length() > 0 && charSequence.charAt(charSequence.length() - 1) == '/') {
            return charSequence.subSequence(0, charSequence.length() - 1);
        } else {
            return charSequence;
        }
    }

    public static String expand(String template, Map<String, ?> variables) {
        // first valid
        if (checkNotNull(template, "template").length() < 3) {
            return template;
        }
        checkNotNull(variables, "variables for %s", template);

        boolean inVar = false;
        StringBuilder var = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (char c : template.toCharArray()) {
            switch (c) {
                case '{':
                    if (inVar) {
                        // '{{' is an escape: write the brace and don't interpret as a variable
                        builder.append("{");
                        inVar = false;
                        break;
                    }
                    inVar = true;
                    break;
                case '}':
                    if (!inVar) { // then write the brace literally
                        builder.append('}');
                        break;
                    }
                    inVar = false;
                    String key = var.toString();
                    Object value = variables.get(var.toString());
                    if (value != null) {
                        builder.append(value);
                    } else {
                        builder.append('{').append(key).append('}');
                    }
                    var = new StringBuilder();
                    break;
                default:
                    if (inVar) {
                        var.append(c);
                    } else {
                        builder.append(c);
                    }
            }
        }
        return builder.toString();
    }

    private static Map<String, Collection<String>> parseAndDecodeQueries(String queryLine) {
        Map<String, Collection<String>> map = new LinkedHashMap<String, Collection<String>>();
        if (emptyToNull(queryLine) == null) {
            return map;
        }
        if (queryLine.indexOf('&') == -1) {
            putKV(queryLine, map);
        } else {
            char[] chars = queryLine.toCharArray();
            int start = 0;
            int i = 0;
            for (; i < chars.length; i++) {
                if (chars[i] == '&') {
                    putKV(queryLine.substring(start, i), map);
                    start = i + 1;
                }
            }
            putKV(queryLine.substring(start, i), map);
        }
        return map;
    }

    private static void putKV(String stringToParse, Map<String, Collection<String>> map) {
        String key;
        String value;
        // note that '=' can be a valid part of the value
        int firstEq = stringToParse.indexOf('=');
        if (firstEq == -1) {
            key = urlDecode(stringToParse);
            value = null;
        } else {
            key = urlDecode(stringToParse.substring(0, firstEq));
            value = urlDecode(stringToParse.substring(firstEq + 1));
        }
        Collection<String> values = map.containsKey(key) ? map.get(key) : new ArrayList<String>();
        values.add(value);
        map.put(key, values);
    }

    public RequestTemplate resolve(Map<String, ?> unencoded) {
        return resolve(unencoded, Collections.<String, Boolean>emptyMap());
    }

    RequestTemplate resolve(Map<String, ?> unencoded, Map<String, Boolean> alreadyEncoded) {
        replaceQueryValues(unencoded, alreadyEncoded);
        Map<String, String> encoded = new LinkedHashMap<String, String>();
        for (Entry<String, ?> entry : unencoded.entrySet()) {
            final String key = entry.getKey();
            final Object objectValue = entry.getValue();
            String encodedValue = encodeValueIfNotEncoded(key, objectValue, alreadyEncoded);
            encoded.put(key, encodedValue);
        }
        String resolvedUrl = expand(url.toString(), encoded).replace("+", "%20");
        if (decodeSlash) {
            resolvedUrl = resolvedUrl.replace("%2F", "/");
        }
        url = new StringBuilder(resolvedUrl);

        Map<String, Collection<String>> resolvedHeaders = new LinkedHashMap<String, Collection<String>>();
        for (String field : headers.keySet()) {
            Collection<String> resolvedValues = new ArrayList<String>();
            for (String value : valuesOrEmpty(headers, field)) {
                String resolved = expand(value, unencoded);
                resolvedValues.add(resolved);
            }
            resolvedHeaders.put(field, resolvedValues);
        }
        headers.clear();
        headers.putAll(resolvedHeaders);
        if (bodyTemplate != null) {
            body(urlDecode(expand(bodyTemplate, encoded)));
        }
        return this;
    }

    private String encodeValueIfNotEncoded(String key, Object objectValue, Map<String, Boolean> alreadyEncoded) {
        String value = String.valueOf(objectValue);
        final Boolean isEncoded = alreadyEncoded.get(key);
        if (isEncoded == null || !isEncoded) {
            value = urlEncode(value);
        }
        return value;
    }

    public Request request() {
        Map<String, Collection<String>> safeCopy = new LinkedHashMap<String, Collection<String>>();
        safeCopy.putAll(headers);
        return Request.create(
                method, url + queryLine(),
                Collections.unmodifiableMap(safeCopy),
                body, charset
        );
    }

    public RequestTemplate method(String method) {
        this.method = checkNotNull(method, "method");
        checkArgument(method.matches("^[A-Z]+$"), "Invalid HTTP Method: %s", method);
        return this;
    }

    public String method() {
        return method;
    }

    public RequestTemplate decodeSlash(boolean decodeSlash) {
        this.decodeSlash = decodeSlash;
        return this;
    }

    public boolean decodeSlash() {
        return decodeSlash;
    }

    public RequestTemplate append(CharSequence value) {
        url.append(value);
        url = pullAnyQueriesOutOfUrl(url);
        return this;
    }

    public RequestTemplate insert(int pos, CharSequence value) {
        if(isHttpUrl(value)) {
            value = removeTrailingSlash(value);
            if(url.length() > 0 && url.charAt(0) != '/') {
                url.insert(0, '/');
            }
        }
        url.insert(pos, pullAnyQueriesOutOfUrl(new StringBuilder(value)));
        return this;
    }

    public String url() {
        return url.toString();
    }

    public RequestTemplate query(boolean encoded, String name, String... values) {
        return doQuery(encoded, name, values);
    }

    public RequestTemplate query(boolean encoded, String name, Iterable<String> values) {
        return doQuery(encoded, name, values);
    }

    public RequestTemplate query(String name, String... values) {
        return doQuery(false, name, values);
    }

    public RequestTemplate query(String name, Iterable<String> values) {
        return doQuery(false, name, values);
    }

    private RequestTemplate doQuery(boolean encoded, String name, String... values) {
        checkNotNull(name, "name");
        String paramName = encoded ? name : encodeIfNotVariable(name);
        queries.remove(paramName);
        if (values != null && values.length > 0 && values[0] != null) {
            ArrayList<String> paramValues = new ArrayList<String>();
            for (String value : values) {
                paramValues.add(encoded ? value : encodeIfNotVariable(value));
            }
            this.queries.put(paramName, paramValues);
        }
        return this;
    }

    private RequestTemplate doQuery(boolean encoded, String name, Iterable<String> values) {
        if (values != null) {
            return doQuery(encoded, name, toArray(values, String.class));
        }
        return doQuery(encoded, name, (String[]) null);
    }

    private static String encodeIfNotVariable(String in) {
        if (in == null || in.indexOf('{') == 0) {
            return in;
        }
        return urlEncode(in);
    }

    public RequestTemplate queries(Map<String, Collection<String>> queries) {
        if (queries == null || queries.isEmpty()) {
            this.queries.clear();
        } else {
            for (Entry<String, Collection<String>> entry : queries.entrySet()) {
                query(entry.getKey(), toArray(entry.getValue(), String.class));
            }
        }
        return this;
    }

    public Map<String, Collection<String>> queries() {
        Map<String, Collection<String>> decoded = new LinkedHashMap<String, Collection<String>>();
        for (String field : queries.keySet()) {
            Collection<String> decodedValues = new ArrayList<String>();
            for (String value : valuesOrEmpty(queries, field)) {
                if (value != null) {
                    decodedValues.add(urlDecode(value));
                } else {
                    decodedValues.add(null);
                }
            }
            decoded.put(urlDecode(field), decodedValues);
        }
        return Collections.unmodifiableMap(decoded);
    }

    public RequestTemplate header(String name, String... values) {
        checkNotNull(name, "header name");
        if (values == null || (values.length == 1 && values[0] == null)) {
            headers.remove(name);
        } else {
            List<String> headers = new ArrayList<String>();
            headers.addAll(Arrays.asList(values));
            this.headers.put(name, headers);
        }
        return this;
    }

    public RequestTemplate header(String name, Iterable<String> values) {
        if (values != null) {
            return header(name, toArray(values, String.class));
        }
        return header(name, (String[]) null);
    }

    public RequestTemplate headers(Map<String, Collection<String>> headers) {
        if (headers == null || headers.isEmpty()) {
            this.headers.clear();
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    public Map<String, Collection<String>> headers() {
        return Collections.unmodifiableMap(headers);
    }

    public RequestTemplate body(byte[] bodyData, Charset charset) {
        this.bodyTemplate = null;
        this.charset = charset;
        this.body = bodyData;
        int bodyLength = bodyData != null ? bodyData.length : 0;
        header(CONTENT_LENGTH, String.valueOf(bodyLength));
        return this;
    }

    public RequestTemplate body(String bodyText) {
        byte[] bodyData = bodyText != null ? bodyText.getBytes(UTF_8) : null;
        return body(bodyData, UTF_8);
    }

    public Charset charset() {
        return charset;
    }

    public byte[] body() {
        return body;
    }

    public RequestTemplate bodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
        this.charset = null;
        this.body = null;
        return this;
    }

    public String bodyTemplate() {
        return bodyTemplate;
    }

    private StringBuilder pullAnyQueriesOutOfUrl(StringBuilder url) {
        // parse out queries
        int queryIndex = url.indexOf("?");
        if (queryIndex != -1) {
            String queryLine = url.substring(queryIndex + 1);
            Map<String, Collection<String>> firstQueries = parseAndDecodeQueries(queryLine);
            if (!queries.isEmpty()) {
                firstQueries.putAll(queries);
                queries.clear();
            }
            for (String key : firstQueries.keySet()) {
                Collection<String> values = firstQueries.get(key);
                if (allValuesAreNull(values)) {
                    queries.put(urlEncode(key), values);
                } else {
                    query(key, values);
                }

            }
            return new StringBuilder(url.substring(0, queryIndex));
        }
        return url;
    }

    private boolean allValuesAreNull(Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return true;
        }
        for (String val : values) {
            if (val != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return request().toString();
    }

    public void replaceQueryValues(Map<String, ?> unencoded) {
        replaceQueryValues(unencoded, Collections.<String, Boolean>emptyMap());
    }

    void replaceQueryValues(Map<String, ?> unencoded, Map<String, Boolean> alreadyEncoded) {
        Iterator<Entry<String, Collection<String>>> iterator = queries.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Collection<String>> entry = iterator.next();
            if (entry.getValue() == null) {
                continue;
            }
            Collection<String> values = new ArrayList<String>();
            for (String value : entry.getValue()) {
                if (value.indexOf('{') == 0 && value.indexOf('}') == value.length() - 1) {
                    Object variableValue = unencoded.get(value.substring(1, value.length() - 1));
                    // only add non-null expressions
                    if (variableValue == null) {
                        continue;
                    }
                    if (variableValue instanceof Iterable) {
                        for (Object val : Iterable.class.cast(variableValue)) {
                            String encodedValue = encodeValueIfNotEncoded(entry.getKey(), val, alreadyEncoded);
                            values.add(encodedValue);
                        }
                    } else {
                        String encodedValue = encodeValueIfNotEncoded(entry.getKey(), variableValue, alreadyEncoded);
                        values.add(encodedValue);
                    }
                } else {
                    values.add(value);
                }
            }
            if (values.isEmpty()) {
                iterator.remove();
            } else {
                entry.setValue(values);
            }
        }
    }

    public String queryLine() {
        if (queries.isEmpty()) {
            return "";
        }
        StringBuilder queryBuilder = new StringBuilder();
        for (String field : queries.keySet()) {
            for (String value : valuesOrEmpty(queries, field)) {
                queryBuilder.append('&');
                queryBuilder.append(field);
                if (value != null) {
                    queryBuilder.append('=');
                    if (!value.isEmpty()) {
                        queryBuilder.append(value);
                    }
                }
            }
        }
        queryBuilder.deleteCharAt(0);
        return queryBuilder.insert(0, '?').toString();
    }

    interface Factory {

        RequestTemplate create(Object[] argv);
    }
}
