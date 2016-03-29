package com.rule.data.util;

import com.alibaba.fastjson.JSON;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.engine.excel.StringPool;
import com.rule.data.exception.CalculateException;
import com.rule.data.exception.RengineException;
import com.rule.data.model.SerColumn;
import com.rule.data.service.core.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.rule.data.engine.excel.NumberPool.*;
import static com.rule.data.engine.excel.StringPool.FALSE;
import static com.rule.data.engine.excel.StringPool.TRUE;
import static com.rule.data.util.Type.*;

public class DataUtil {

    public static final HashMap<String, Object> EMPTY = new HashMap<String, Object>(0);

    private static final Class CLASS_STRING = String.class,
                               CLASS_LONG = Long.class,
                               CLASS_DATE = Date.class,
                               CLASS_DOUBLE = Double.class,
                               CLASS_BOOL = Boolean.class,
                               CLASS_RANGE = ExcelRange.class;

    private static final ConcurrentHashMap<String, Integer> columnIndexMap= new ConcurrentHashMap<String, Integer>();

    public static ConcurrentHashMap<String, AtomicLong> serviceTimes = new ConcurrentHashMap<String, AtomicLong>();

    public static void inheritParam(Map<String, Object> current, Map<String, Object> father) {

        for (Map.Entry<String, Object> entry : father.entrySet()) {
            if (!current.containsKey(entry.getKey())) {
                current.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static final ThreadLocal<NumberFormat> numberFormat = new ThreadLocal<NumberFormat>() {

        @Override
        protected NumberFormat initialValue() {

            NumberFormat tmp = NumberFormat.getInstance();
            tmp.setGroupingUsed(false);
            tmp.setMaximumFractionDigits(6);
            tmp.setRoundingMode(RoundingMode.HALF_EVEN);

            return tmp;
        }
    };

    private static final ThreadLocal<SimpleDateFormat> simpleDateFormat = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };


    public static String getServiceName(Object input) throws RengineException {

        Type type = getType(input);
        String name = null;

        if (type == Type.LONG) {

            name = Services.id2Name(((Long) input).intValue());
        } else {
            name = DataUtil.getStringValue(input, type).trim();
        }

        if (name == null) {
            throw new RengineException(String.valueOf(input), "数据源未找到, " + input);
        }

        return name;
    }

    public static int getColumnIntIndex(String source, List<SerColumn> columnPos) throws RengineException {

        boolean isColumnIndex = isColumnIndex(source);

        if (isColumnIndex) {
            source = source.toUpperCase();
        }

        for (SerColumn columnPo : columnPos) {
            String tmp = isColumnIndex ? columnPo.getColumnIndex() : columnPo.getColumnName();
            if (tmp.equals(source)) {
                return columnPo.getColumnIntIndex();
            }
        }

        return -1;
    }

    public static boolean isColumnIndex(String source) {
        if (source.length() > 3) {
            return false;
        }

        for (int i = 0; i < source.length(); i++) {
            char tmp = source.charAt(i);
            if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
                //
            } else {
                return false;
            }
        }
        return true;
    }

    public static Type getType(Object input) throws RengineException {

        if (input == null) {
            return Type.NULL;
        }

        final Class clazz = input.getClass();
        if (clazz == CLASS_STRING) {
            return STRING;
        } else if (clazz == CLASS_DOUBLE) {
            return DOUBLE;
        } else if (clazz == CLASS_LONG) {
            return LONG;
        } else if (clazz == CLASS_BOOL) {
            return BOOLEAN;
        } else if (clazz == CLASS_DATE) {
            return DATE;
        } else if (clazz == CLASS_RANGE) {
            return RANGE;
        } else {
            throw new RengineException(null, "类型不支持, " + input.getClass().getSimpleName());
        }
    }


    /**
     * -1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.
     */
    public static int compare(double d1, double d2) {
        return new BigDecimal(d1).setScale(6, BigDecimal.ROUND_HALF_EVEN).compareTo(
                new BigDecimal(d2).setScale(6, BigDecimal.ROUND_HALF_EVEN));
    }

    public static String getNextColumnIndex(String columnIndex) throws RengineException {
        int index = countIndex(columnIndex);
        return extract(index + 1);
    }

    public static String extract(int i) {
        i += 1;
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);

        while (i > 0) {
            final int j = (i % 26);
            sb.append("" + (char) ('A' + (j == 0 ? 25 : (j - 1))));
            i = (i - 1) / 26;
        }


        return sb.reverse().toString();
    }

    public static int countIndex(String columnIndex) throws RengineException {
        if (columnIndex == null) {
            throw new RengineException(null, "columnIndex为空");
        }

        if (columnIndex.length() > 3) {
            throw new RengineException(null, "列位置长度最大为3, " + columnIndex);
        }

        Integer index = columnIndexMap.get(columnIndex);

        if (index == null) {
            columnIndex = columnIndex.toUpperCase();
            int count = 0;

            for (char c : columnIndex.toCharArray()) {
                if (c < 'A' || c > 'Z') {
                    throw new RengineException(null, "列位置非法, " + columnIndex);
                }

                count = count * 26 + (c - 'A' + 1);
            }

            index = count - 1;

            columnIndexMap.put(columnIndex, index);
        }

        return index;
    }

    public static boolean isValid(String columnIndex) {
        if (columnIndex.isEmpty()) {
            return false;
        }

        for (char c : columnIndex.toCharArray()) {
            if ((c >= 'A' && c <= 'Z')) {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }

    public static <T> T parse(String content, Class<T> clazz) throws RengineException {

        if (content == null || content.trim().length() == 0) {
            content = "{}";
        }

        try {
            return JSON.parseObject(content, clazz);
        } catch (Throwable e) {

            try {
                DebugUtil.debugJSON(e.getMessage(), content); //对于json解析错误的加入json传入内容
            } catch (RengineException re) {
                throw re;
            } catch (Exception ex) {
                //
            }

            throw new RengineException(null, "JSON解析错误, " + e.getMessage());
        }
    }

    public static Map<String, Object> parse(String content) throws RengineException {
        if (content == null || content.trim().length() == 0) {
            content = "{}";
        }

        try {
            return JSON.parseObject(content);
        } catch (Throwable e) {
            try {
                DebugUtil.debugJSON(e.getMessage(), content); // llcheng 对于json解析错误的加入json传入内容
            } catch (RengineException re) {
                throw re;
            } catch (Exception ex) {
                //
            }

            throw new RengineException(null, "JSON解析错误, " + e.getMessage());
        }
    }

    public static String getEncryptedPassword(String source) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(source.getBytes());

        byte[] data = md.digest();
        char[] buff = new char[data.length * 2];
        int tmp;

        for (int i = 0; i < data.length; i++) {
            tmp = ((data[i] >> 4) & 0xf);
            buff[i * 2] = tmp < 10 ? (char) ('0' + tmp) : (char) ('a' + tmp - 10);
            tmp = data[i] & 0xf;
            buff[i * 2 + 1] = tmp < 10 ? (char) ('0' + tmp) : (char) ('a' + tmp - 10);
        }

        return new String(buff);
    }

    public static String date2String(Date date) {
        if (date == null) {
            return null;
        }

        return simpleDateFormat.get().format(date);
    }

    public static String number2String(double d) {
        final String ret = numberFormat.get().format(d);

        if (ret.length() == 2 && ret.equals("-0")) {
            return "0";
        }

        return ret;
    }

    public static String getStringValue(Object input) throws RengineException {
        return getStringValue(input, getType(input));
    }

    public static String getStringValue(Object input, Type type) {
        String result = StringPool.EMPTY;

        switch (type) {
            case LONG:
                result = Long.toString((Long) input);
                break;
            case DOUBLE:
                result = number2String((Double) input);
                break;
            case BOOLEAN:
                result = (Boolean) input ? TRUE : FALSE;
                break;
            case DATE:
                result = date2String((Date) input);
                break;
            case STRING:
                result = ((String) input);
                break;
        }

        return result;
    }

    public static Number getNumberValue(Object input) throws RengineException, CalculateException {
        return getNumberValue(input, getType(input));
    }

    public static Number getNumberValue(Object input, Type type) throws CalculateException {
        Number result = LONG_0;

        switch (type) {
            case LONG:
                result = (Long) input;
                break;
            case DOUBLE:
                result = (Double) input;
                break;
            case BOOLEAN:
                result = (Boolean) input ? LONG_1 : LONG_0;
                break;
            case DATE:
                result = ((Date) input).getTime() / LONG_1000;
                break;
            case STRING:
                result = parseDouble((String) input);
                break;
        }

        return result;
    }


    public static Map<String, Object> convert2String(Map<String, Object> param) {
        Map<String, Object> retValue = new HashMap<String, Object>(param.size());

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            String key2 = entry.getKey();
            Object value = entry.getValue();

            if (value != null) {
                if (value instanceof Map) {
                    retValue.put(key2, convert2String((Map) value));
                } else if (value instanceof List) {
                    retValue.put(key2, convert2String((List) value));
                } else {
                    retValue.put(key2, value.toString());
                }
            }
        }

        return retValue;
    }


    public static List<Object> convert2String(List<Object> param) {
        List<Object> retValue = new ArrayList<Object>(param.size());

        for (Object value : param) {
            if (value != null) {
                if (value instanceof Map) {
                    retValue.add(convert2String((Map) value));
                } else if (value instanceof List) {
                    retValue.add(convert2String((List) value));
                } else {
                    retValue.add(value.toString());
                }
            }
        }

        return retValue;
    }

    public static Object getValueEval(Object input) throws RengineException {
        Type type = getType(input);

        switch (type) {
            case BOOLEAN:
            case STRING:
            case DOUBLE:
            case LONG:
            case NULL:
                return input;
            case DATE:
                return new String(date2String((Date) input));
            default:
                return input;
        }
    }

    public static long compareO(Object left, Object right) throws RengineException, CalculateException {
        if (left == null || right == null) {
            if (left != null) {
                Type leftType = getType(left);

                if (leftType == Type.STRING) {
                    if (DataUtil.getStringValue(left, leftType).length() == 0) {
                        return NumberPool.LONG_0;
                    }
                } else if (leftType == Type.LONG) {
                    if (((Long) left) == LONG_0) {
                        return LONG_0;
                    }
                } else if (leftType == Type.DOUBLE) {
                    if (DataUtil.compare((Double) left, DOUBLE_0) == 0) {
                        return LONG_0;
                    }
                }

                return LONG_1;
            }

            if (right != null) {
                Type rightType = getType(right);

                if (rightType == Type.STRING) {
                    if (DataUtil.getStringValue(right, rightType).length() == 0) {
                        return NumberPool.LONG_0;
                    }
                } else if (rightType == Type.LONG) {
                    if (((Long) right) == LONG_0) {
                        return LONG_0;
                    }

                } else if (rightType == Type.DOUBLE) {
                    if (DataUtil.compare((Double) right, DOUBLE_0) == 0) {
                        return LONG_0;
                    }
                }

                return LONG_M_1;
            }

            return LONG_0;
        } else {
            Type leftType = getType(left);
            Type rightType = getType(right);

            if ((leftType == Type.STRING || leftType == Type.DATE)
                    && (rightType == Type.STRING || rightType == Type.DATE)) {
                return (getStringValue(left, leftType)).compareTo(getStringValue(right, rightType));
            } else {
                if (leftType == Type.STRING) {
                    return 1;
                }

                if (rightType == Type.STRING) {
                    return -1;
                }

                Number leftN = getNumberValue(left, leftType);
                Number rightN = getNumberValue(right, rightType);

                if (leftType == Type.DOUBLE || rightType == Type.DOUBLE) {
                    return DataUtil.compare(leftN.doubleValue(), rightN.doubleValue());
                }

                return leftN.longValue() - rightN.longValue();
            }
        }
    }


    public static String getColumnName(List<SerColumn> columnPos, Integer index) throws RengineException {
        if (columnPos.size() <= index) {
            return StringPool.EMPTY;
        }
        return columnPos.get(index).getColumnName();
    }
}
