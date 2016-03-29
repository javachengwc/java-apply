package com.rule.data.engine.functions._O;

import com.rule.data.exception.*;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.engine.functions.Function;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.service.core.Cache4D2Data;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.service.core.Services;

import java.util.*;

/**
 * _O_RANK(1,17,"A","B",0)
 */
public class _O_RANK extends Function {
    public static final String NAME = _O_RANK.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 4) {
            throw new ArgsCountException(NAME);
        }

        final String reqKey = DataUtil.getStringValue(args[0]);
        final String serviceName = DataUtil.getServiceName(args[1]);
        final String colBy = DataUtil.getStringValue(args[2]);
        final String colCal = DataUtil.getStringValue(args[3]);
        int order = 0;
        if (args.length > 4) {
            order = DataUtil.getNumberValue(args[4]).intValue();
        }
        boolean isPassZero = false;
        if (args.length > 5 && args[5] instanceof Boolean) {
            isPassZero = (Boolean) args[5];
        }

        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, calInfo.getParam(), NAME);
        __Key key = new __Key(colBy, colCal, order, isPassZero);
        Map<String, Long> result = (Map<String, Long>) cache.get(key);

        if (result == null) {
            result = init(calInfo, serviceName, calInfo.getParam()
                    , colBy, colCal, order, isPassZero, NAME);
            cache.put(key, result);
        }

        Long ret = result.get(reqKey);
        if (ret == null) {
            return NumberPool.LONG_0;
        }

        return ret;
    }

    class __Key {
        Object colBy, colCal, order, isPassZero;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (colBy != null ? !colBy.equals(key.colBy) : key.colBy != null)
                return false;
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
            int result = colBy != null ? colBy.hashCode() : 0;
            result = 31 * result + (colCal != null ? colCal.hashCode() : 0);
            result = 31 * result + (order != null ? order.hashCode() : 0);
            result = 31 * result + (isPassZero != null ? isPassZero.hashCode() : 0);
            return result;
        }

        __Key(Object colBy, Object colCal, Object passZero, Object order) {
            this.colBy = colBy;
            this.colCal = colCal;
            this.isPassZero = passZero;
            this.order = order;
        }
    }

    static Map<String, Long> init(CalInfo calInfo, String serviceName, Map<String, Object> current
            , String colBy, String colCal
            , int order, boolean isPassZero, String funcName) throws RengineException, CalculateException {
        SerService servicePo = Services.getService(serviceName);
        if (servicePo == null) {
            throw new ServiceNotFoundException(serviceName);
        }

        final D2Data d2Data =
                Cache4D2Data.getD2Data(servicePo, current, calInfo.getCallLayer()
                        , calInfo.getServicePo(), calInfo.getParam(), funcName);

        final Object[][] value = d2Data.getData();
        int colByInt = DataUtil.getColumnIntIndex(colBy, d2Data.getColumnList());
        int colCalInt = DataUtil.getColumnIntIndex(colCal, d2Data.getColumnList());

        if (colByInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colBy);
        }
        if (colCalInt == -1) {
            throw new ArgColumnNotFoundException(NAME, colCal);
        }

        List<Double> values = new ArrayList<Double>();
        Map<String, Long> result = new HashMap<String, Long>();
        Map<String, Double> map1 = new HashMap<String, Double>();
        Map<Double, Long> map2 = new HashMap<Double, Long>();

        for (int i = 0; i < value.length; i++) {
            final Object colByValue = value[i][colByInt];
            final Object colCalValue = value[i][colCalInt];
            if (colByValue == null || colCalValue == null) {
                continue;
            }


            if (canNumberOP(colCalValue)) {
                double d = DataUtil.getNumberValue(colCalValue).doubleValue();

                if (isPassZero
                        && DataUtil.compare(d, 0D) == 0) {
                    continue;
                }

                values.add(d);
                map1.put(DataUtil.getStringValue(colByValue), d);
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
                    map2.put(values.get(i), rank);
                } else {
                    if (values.get(i).equals(values.get(i + 1))) {
                        map2.put(values.get(i), rank);
                    } else {
                        rank = size - i;
                        map2.put(values.get(i), rank);
                    }
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    map2.put(values.get(i), rank);
                } else {
                    if (values.get(i).equals(values.get(i - 1))) {
                        map2.put(values.get(i), rank);
                    } else {
                        rank = i + 1;
                        map2.put(values.get(i), rank);
                    }
                }
            }
        }

        for (Map.Entry<String, Double> entry : map1.entrySet()) {
            result.put(entry.getKey(), map2.get(entry.getValue()));
        }

        return result;
    }

}
