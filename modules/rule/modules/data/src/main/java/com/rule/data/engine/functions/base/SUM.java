package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.Iterator;
import java.util.Map;

public class SUM extends Function {

    public static final String NAME = SUM.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length == 0) {
            return NumberPool.LONG_0;
        }

        double sum = NumberPool.DOUBLE_0;

        for (Object arg : args) {
            if (arg instanceof ExcelRange) {
                ExcelRange range = (ExcelRange) arg;

                Map<Object, Object> cache = calInfo.getCache(NAME);

                Double result = (Double) cache.get(range);
                if (result == null) {
                    Iterator<Object> ite = range.getIterator();

                    result = NumberPool.DOUBLE_0;

                    while (ite.hasNext()) {
                        Object tmp = ite.next();
                        if (tmp != null && canNumberOP(tmp)) {
                            result += DataUtil.getNumberValue(tmp).doubleValue();
                        }
                    }

                    cache.put(range, result);
                }

                sum += result;
            } else if (arg != null && canNumberOP(arg)) {
                sum += DataUtil.getNumberValue(arg).doubleValue();
            }
        }

        return sum;
    }
}
