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

public class MAX extends Function {

    public static final String NAME = MAX.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length == 0) {
            return NumberPool.DOUBLE_0;
        }

        boolean hasMax = false;
        double max = Long.MIN_VALUE;

        for (Object arg : args) {
            if (arg instanceof ExcelRange) {
                Map<Object, Object> cache = calInfo.getCache(NAME);

                Object result = cache.get(arg);

                if (result == null) {
                    Iterator<Object> ite = ((ExcelRange) arg).getIterator();

                    boolean hasMaxInRange = false;
                    double maxInRange = Long.MIN_VALUE;
                    while (ite.hasNext()) {
                        Object tmp = ite.next();

                        if (tmp != null && canNumberOP(tmp)) {
                            hasMaxInRange = true;
                            double tmpD = DataUtil.getNumberValue(tmp).doubleValue();
                            maxInRange = tmpD > maxInRange ? tmpD : maxInRange;
                        }
                    }

                    if (hasMaxInRange) {
                        result = maxInRange;
                        cache.put(arg, maxInRange);
                    } else {
                        result = false;
                        cache.put(arg, false);
                    }
                }

                if (!(result instanceof Boolean)) {
                    hasMax = true;
                    max = ((Double) result) > max ? ((Double) result) : max;
                }
            } else if (arg != null && canNumberOP(arg)) {
                hasMax = true;
                double tmpD = DataUtil.getNumberValue(arg).doubleValue();
                max = tmpD > max ? tmpD : max;
            }
        }

        if (hasMax) {
            return max;
        }

        return NumberPool.DOUBLE_0;
    }
}
