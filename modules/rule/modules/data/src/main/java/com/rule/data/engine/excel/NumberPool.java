package com.rule.data.engine.excel;

import com.rule.data.exception.CalculateException;

import java.util.concurrent.ConcurrentHashMap;

public final class NumberPool {
    public static final long LONG_M_1 = -1L, LONG_0 = 0L, LONG_6 = 6L, LONG_1 = 1L, LONG_2 = 2L, LONG_1000 = 1000L;
    public static final double DOUBLE_0 = 0D, DOUBLE_1 = 1D, DOUBLE_2 = 2D;

    private static final ConcurrentHashMap<String, Number> cache
            = new ConcurrentHashMap<String, Number>();

    public static Long getLong(String input) {
        if (input == null) {
            throw new NullPointerException("input");
        }

        Number result = cache.get(input);
        if (result == null) {
            try {
                result = Long.parseLong(input);
            } catch (Exception e) {
                result = LONG_0;
            }
            cache.put(input, result);
        }

        return result.longValue();
    }

    public static Double parseDouble(String input) throws CalculateException {
        try {
            return Double.parseDouble(input);
        } catch (Exception e) {
            throw new CalculateException("数字解析失败, " + input);
        }
    }

    public static Double getDouble(String input) {
        if (input == null) {
            throw new NullPointerException("input");
        }

        Number result = cache.get(input);
        if (result == null) {
            try {
                result = Double.parseDouble(input);
            } catch (Exception e) {
                result = DOUBLE_0;
            }

            cache.put(input, result);
        }

        return result.doubleValue();
    }

    static boolean isPositive(String input) {
        if (input == null) {
            throw new NullPointerException("input");
        }

        Number result = cache.get(input);
        if (result == null) {
            int cnt = 0;

            for (char c : input.toCharArray()) {
                if (c == '-') {
                    cnt++;
                }
            }

            result = (cnt & 0x1);
            cache.put(input, result);
        }

        return result.intValue() == 0;
    }
}
