package com.ocean.parser;

import com.google.common.base.Joiner;

import java.io.IOException;
import java.util.*;

/**
 * sql构建器
 */
public class SqlBuilder implements Appendable {

    private List<Object> segments = new LinkedList<Object>();

    private Map<String, StringToken> tokenMap = new HashMap<String, StringToken>();

    private StringBuilder currentSegment;

    public SqlBuilder() {
        currentSegment = new StringBuilder();
        segments.add(currentSegment);
    }

    /**
     * 增加占位符
     */
    public SqlBuilder appendToken(final String token) {
        return appendToken(token, true);
    }

    /**
     * 增加占位符
     */
    public SqlBuilder appendToken(final String token, final boolean isSetValue) {
        StringToken stringToken;
        if (tokenMap.containsKey(token)) {
            stringToken = tokenMap.get(token);
        } else {
            stringToken = new StringToken();
            if (isSetValue) {
                stringToken.value = token;
            }
            tokenMap.put(token, stringToken);
        }
        segments.add(stringToken);
        currentSegment = new StringBuilder();
        segments.add(currentSegment);
        return this;
    }

    /**
     * 用实际的值替代占位符
     * @param originToken 占位符
     * @param newToken 实际的值
     */
    public SqlBuilder buildSQL(final String originToken, final String newToken) {
        if (tokenMap.containsKey(originToken)) {
            tokenMap.get(originToken).value = newToken;
        }
        return this;
    }

    /**
     * 生成SQL语句
     */
    public String toSql() {
        StringBuilder result = new StringBuilder();
        for (Object each : segments) {
            result.append(each.toString());
        }
        return result.toString();
    }

    @Override
    public Appendable append(final CharSequence sql) throws IOException {
        currentSegment.append(sql);
        return this;
    }

    @Override
    public Appendable append(final CharSequence sql, final int start, final int end) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Appendable append(final char c) throws IOException {
        currentSegment.append(c);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Object each : segments) {
            if (each instanceof StringToken) {
                result.append(((StringToken) each).toToken());
            } else {
                result.append(each.toString());
            }
        }
        return result.toString();
    }

    private class StringToken {

        private String value;

        public String toToken() {
            return null == value ? "" : Joiner.on("").join("[Token(", value, ")]");
        }

        @Override
        public String toString() {
            return null == value ? "" : value;
        }
    }
}

