package com.manageplat.assemb;

import com.manageplat.exception.SqlAssembException;
import com.util.PropertiesLoader;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SqlAssembler {

    private static Logger logger = LoggerFactory.getLogger(SqlAssembler.class);

    private static final ConcurrentHashMap<String, List<SqlSegment>> CACHE_REQUIREDPARA  = new ConcurrentHashMap<String, List<SqlSegment>>();

    private static final ThreadLocal<VelocityEngine> VELOCITY = new ThreadLocal<VelocityEngine>() {
        @Override
        protected VelocityEngine initialValue() {
            Properties p =PropertiesLoader.load("vtl.properties");
            if(p==null)
            {
                logger.error("告警: VTL配置载入失败", "error load vtl.proerptes, ");
            }
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init(p);
            return velocityEngine;
        }
    };

    /**
     * 根据sql和对应参数渲染出最终的sql
     * 先渲染必选参数, 再渲染可选参数
     * @param sql
     * @param param
     * @return
     */
    public static String renderSql(String sql, Map<String, Object> param) throws SqlAssembException {
        if (sql == null || sql.length() == 0) {
            return "";
        }

        List<SqlSegment> segments = CACHE_REQUIREDPARA.get(sql);

        if (segments == null) {
            segments = new ArrayList<SqlSegment>();
            Pattern p = Pattern.compile("#[\\u4e00-\\u9fa5a-zA-Z0-9_-]+#");
            Matcher m = p.matcher(sql);
            int last = 0;

            while (m.find()) {
                String requiredPara = m.group();
                requiredPara = requiredPara.substring(1, requiredPara.length() - 1);

                segments.add(new SqlSegment(sql.substring(last, m.start()), false));
                segments.add(new SqlSegment(requiredPara, true));

                last = m.end();
            }

            if (last == 0) {
                segments.add(new SqlSegment(sql, false));
            } else if (last < sql.length()) {
                segments.add(new SqlSegment(sql.substring(last), false));
            }

            CACHE_REQUIREDPARA.put(sql, segments);
        }

        final StringBuilder stringBuilder = new StringBuilder(sql.length() + 16);

        for (SqlSegment sqlSegment : segments) {
            if (sqlSegment.getIsRequiredPara()) {
                final Object value = param.get(sqlSegment.getStr());

                if (value != null && !(value instanceof Map) && !(value instanceof List)) {
                    stringBuilder.append(String.valueOf(value));
                } else {
                    stringBuilder.append('#').append(sqlSegment.getStr()).append('#');
                }
            } else {
                stringBuilder.append(sqlSegment.getStr());
            }
        }

        final String renderFirstSql = stringBuilder.append('\n').toString();
        final VelocityContext context = new VelocityContext();

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }

        StringWriter writer = new StringWriter();

        try {
            VELOCITY.get().mergeTemplate(renderFirstSql, "utf-8", context, writer);
        } catch (Exception e) {
            throw new SqlAssembException( "SQL中存在VTL语法错误,sql= "+sql + e.getMessage());
        }

        return writer.toString();
    }


    public static String format(String sql) {

        final char[] arr = sql.replaceAll("[\n\r\t]+", " ").toCharArray();
        final boolean[] skip = new boolean[arr.length];
        int stateFlag = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            final char c = arr[i];

            if (c == '\'') {
                skip[i] = true;
                if (stateFlag == 0) {
                    stateFlag = 1; // 单引号范畴
                } else if (stateFlag == 1) {
                    if (c != '\\') {
                        stateFlag = 0;
                    }
                }
            } else if (c == '"') {
                skip[i] = true;
                if (stateFlag == 0) {
                    stateFlag = 2;
                } else if (stateFlag == 2) {
                    if (c != '\\') {
                        stateFlag = 0;
                    }
                }
            } else {
                if (stateFlag != 0) {
                    skip[i] = true;
                }
            }

        }

        int tabCount = 0;
        for (int i = 0; i < arr.length; i++) {
            final char c = arr[i];
            final char preC = i == 0 ? ' ' : arr[i - 1];

            if (skip[i]) {
                sb.append(c);
                continue;
            }


            if (c == ',') {
                sb.append('\n').append(getTabs(tabCount)).append(c);
            } else if (c == '(') {
                tabCount++;
                sb.append(c);
            } else if (c == ')') {
                tabCount--;
                sb.append('\n').append(getTabs(tabCount)).append(c);
            } else if (preC == ' ') {
                if (c == ' ') {
                    continue;
                }

                if (isSpecial(arr, i, skip)) {
                    sb.append('\n').append(getTabs(tabCount)).append(arr[i]);
                } else {
                    sb.append(c);
                }

            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static String[] specials = new String[]{
            "AND"
            , "SELECT"
            , "FROM"
            , "WHERE"
            , "EXISTS"
            , "IN"
            , "#if"
            , "#end"
    };

    private static boolean isSpecial(char[] arr, int i, boolean[] skip) {
        StringBuilder sb = new StringBuilder();

        for (int ix = i; ix < arr.length; ix++) {
            if (skip[ix] || arr[ix] == ' ' || arr[ix] == '(') {
                break;
            }

            sb.append(arr[ix]);
        }

        final String str = sb.toString();
        for (int ix = 0; ix < specials.length; ix++) {
            if (specials[ix].equalsIgnoreCase(str)) {
                for (int jx = i; jx < arr.length; jx++) {
                    if (skip[jx] || arr[jx] == ' ' || arr[jx] == '(') {
                        break;
                    }
                }

                return true;
            }
        }

        return false;
    }

    private static String getTabs(int tabCount) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tabCount * 2; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
