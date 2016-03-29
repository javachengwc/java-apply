package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.*;

public class RANK extends Function {

    public static final String NAME = RANK.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        if (args[1] instanceof ExcelRange) {
            final String reqKey = DataUtil.getStringValue(args[0]);
            final ExcelRange range = (ExcelRange) args[1];
            int order = 0;
            if (args.length > 2) {
                order = DataUtil.getNumberValue(args[2]).intValue();
            }
            boolean isPassZero = false;
            if (args.length > 3 && args[3] instanceof Boolean) {
                isPassZero = (Boolean) args[3];
            }

            Map<Object, Object> cache = calInfo.getCache(NAME);
            __Key key = new __Key(range, order, isPassZero);

            Map<String, Long> result = (Map<String, Long>) cache.get(key);

            if (result == null) {
                result = init(range, order, isPassZero);
                cache.put(key, result);
            }

            Long ret = result.get(reqKey);
            if (ret == null) {
                return NumberPool.LONG_0;
            }

            return ret;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }

    class __Key {
        Object colCal, order, isPassZero;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colCal != null ? !colCal.equals(key.colCal) : key.colCal != null)
                return false;
            if (isPassZero != null ? !isPassZero.equals(key.isPassZero) : key.isPassZero != null)
                return false;
            if (order != null ? !order.equals(key.order) : key.order != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = colCal != null ? colCal.hashCode() : 0;
            result = 31 * result + (order != null ? order.hashCode() : 0);
            result = 31 * result + (isPassZero != null ? isPassZero.hashCode() : 0);
            return result;
        }

        __Key(Object colCal, Object order, Object passZero) {
            this.colCal = colCal;
            this.isPassZero = passZero;
            this.order = order;
        }
    }

    private Map<String, Long> init(ExcelRange range, int order, boolean isPassZero) throws RengineException, CalculateException {
        List<Double> values = new ArrayList<Double>();
        Map<String, Long> result = new HashMap<String, Long>();

        Iterator<Object> ite = range.getIterator();

        while (ite.hasNext()) {
            Object tmp = ite.next();

            if (canNumberOP(tmp)) {
                double d = DataUtil.getNumberValue(tmp).doubleValue();

                if (isPassZero
                        && DataUtil.compare(d, NumberPool.DOUBLE_0) == 0) {
                    continue;
                }
                values.add(d);
            }
        }

        final int size = values.size();

        if (size != 0) {
            Collections.sort(values);
        }

        long rank = NumberPool.LONG_1;
        if (order == 0) {
            for (int i = size - 1; i >= 0; i--) {
                if (i == size - 1) {
                    result.put(DataUtil.getStringValue(values.get(i)), rank);
                } else {
                    if (values.get(i).equals(values.get(i + 1))) {
                        result.put(DataUtil.getStringValue(values.get(i)), rank);
                    } else {
                        rank = size - i;
                        result.put(DataUtil.getStringValue(values.get(i)), rank);
                    }
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    result.put(DataUtil.getStringValue(values.get(i)), rank);
                } else {
                    if (values.get(i).equals(values.get(i - 1))) {
                        result.put(DataUtil.getStringValue(values.get(i)), rank);
                    } else {
                        rank = i + 1;
                        result.put(DataUtil.getStringValue(values.get(i)), rank);
                    }
                }
            }
        }

        return result;
    }

}
