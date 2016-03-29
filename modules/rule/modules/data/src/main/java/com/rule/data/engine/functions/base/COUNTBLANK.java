package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.engine.excel.NumberPool;

import java.util.Iterator;
import java.util.Map;

public class COUNTBLANK extends Function {

    public static final String NAME = COUNTBLANK.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] instanceof ExcelRange) {
            ExcelRange range = (ExcelRange) args[0];

            Map<Object, Object> cache = calInfo.getCache(NAME);
            Long result = (Long) cache.get(range);

            if (result == null) {
                Iterator<Object> ite = range.getIterator();
                result = NumberPool.LONG_0;

                while (ite.hasNext()) {
                    Object value = ite.next();
                    if (value == null) {
                        result++;
                    }
                }

                cache.put(range, result);
            }

            return result;
        }

        throw new RengineException(calInfo.getServiceName(), NAME + "输入不是数列");
    }
}