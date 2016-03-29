package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.Iterator;
import java.util.Map;

public class COUNTA extends Function {

    public static final String NAME = COUNTA.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        long count = NumberPool.LONG_0;

        for (Object arg : args) {
            if (arg == null) {
                continue;
            }

            if (arg instanceof ExcelRange) {
                ExcelRange range = (ExcelRange) arg;

                Map<Object, Object> cache = calInfo.getCache(NAME);
                Long result = (Long) cache.get(range);

                if (result == null) {
                    Iterator<Object> ite = range.getIterator();

                    result = NumberPool.LONG_0;

                    while (ite.hasNext()) {
                        Object value = ite.next();
                        if (value != null) {
                            result++;
                        }
                    }

                    cache.put(range, result);
                }

                count += result;
            } else {
                count++;
            }
        }

        return count;
    }
}