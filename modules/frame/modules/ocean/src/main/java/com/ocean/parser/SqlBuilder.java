package com.ocean.parser;

import com.google.common.base.Joiner;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.IOException;
import java.util.*;

/**
 * sql构建器
 */
public class SqlBuilder implements Appendable {

    //组建语句的片段列表(必须有序)
    private List<Object> segments = new LinkedList<Object>();

    //可替代值的map
    private Map<String, StringToken> tokenMap = new HashMap<String, StringToken>();

    //当前片段
    private StringBuilder currentSegment;

    public SqlBuilder() {
        currentSegment = new StringBuilder();
        segments.add(currentSegment);
    }

    /**
     * 增加占位符
     */
    public SqlBuilder appendToken(String token) {
        return appendToken(token, true);
    }

    /**
     * 增加占位符
     */
    public SqlBuilder appendToken(String token, boolean isSetValue) {
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
    public SqlBuilder buildSQL( String originToken, String newToken) {
        //此处比较巧妙，改变StringToken的value值同时也改变了segments对应片段的值，因为segments对应片段其实就是StringToken
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
    public Appendable append(CharSequence sql) throws IOException {
        currentSegment.append(sql);
        return this;
    }

    @Override
    public Appendable append(CharSequence sql, int start, int end) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Appendable append(char c) throws IOException {
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
            return (null == value)?"":Joiner.on("").join("[Token(", value, ")]");
        }

        @Override
        public String toString() {
            return (null == value)?"":value;
        }
    }
}

