package com.rule.data.engine.functions._O;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.service.core.Cache4_O_;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.engine.functions.Function;

import java.util.Map;

/**
 * _O_RANK(1,17,"A","B",0)
 */
public class _O_RANKBYPARA extends Function {
    public static final String NAME = _O_RANKBYPARA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 6) {
            throw new ArgsCountException(NAME);
        }

        final String reqKey = DataUtil.getStringValue(args[0]);
        final String serviceName = DataUtil.getServiceName(args[1]);
        final String colBy = DataUtil.getStringValue(args[2]);
        final String colCal = DataUtil.getStringValue(args[3]);
        int order = DataUtil.getNumberValue(args[4]).intValue();
        boolean isPassZero = false;
        if (args[5] instanceof Boolean) {
            isPassZero = (Boolean) args[5];
        }


        final Map currentParam = getParam(args, 6, calInfo.getParam(), true);
        Map<Object, Object> cache = Cache4_O_.cache4_O_(serviceName, currentParam, NAME);
        __Key key = new __Key(colBy, colCal, order, isPassZero);
        Map<String, Long> result = (Map<String, Long>) cache.get(key);

        if (result == null) {
            result
                    = _O_RANK.init(calInfo, serviceName, currentParam, colBy, colCal, order, isPassZero, NAME);
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
}
