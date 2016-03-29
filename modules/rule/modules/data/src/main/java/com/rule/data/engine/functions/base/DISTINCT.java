package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.ExcelRange;
import com.rule.data.model.vo.D2Data;

import java.util.*;

public class DISTINCT extends Function {

    public static final String NAME = DISTINCT.class.getSimpleName();

    //去重函数,返回去重后的数列
    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }
        Set<Object> set = new LinkedHashSet<Object>();
        Map<Object, Object> cache = calInfo.getCache(NAME);
        List<Object> argList = new ArrayList<Object>();
        for (int i = 0; i < args.length; i++) {
            argList.add(args[i]);
        }
        __Key key = new __Key(argList);
        ExcelRange result = (ExcelRange) cache.get(key);
        if (result == null) {
            for (Object arg : args) {
                if (arg == null) {
                    continue;
                }
                if (arg instanceof ExcelRange) {
                    ExcelRange range = (ExcelRange) arg;
                    Set<Object> rangeSet = (Set<Object>) cache.get(range);
                    if (rangeSet == null) {
                        rangeSet = new LinkedHashSet<Object>();
                        Iterator<Object> ite = range.getIterator();
                        while (ite.hasNext()) {
                            Object value = ite.next();
                            if (value != null) {
                                rangeSet.add(value);
                            }
                        }
                        cache.put(range, rangeSet);
                    }
                    set.addAll(rangeSet);
                } else if (arg != null) {
                    set.add(arg);
                }
            }

            Object[] values = set.toArray();
            Object[][] data = new Object[values.length][1];
            for (int i = 0; i < values.length; i++) {
                data[i][0] = values[i];
            }
            D2Data d2Data = new D2Data(null);
            d2Data.setData(data);
            result = new ExcelRange(0, 0, 0, values.length, d2Data);
            cache.put(key, result);
        }
        return result;
    }


    class __Key {
        List<Object> argList;      // 由于数列有顺序，故需要用List作为key

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (argList != null ? !argList.equals(key.argList) : key.argList != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            return argList != null ? argList.hashCode() : 0;
        }

        public __Key(List argList) {
            this.argList = argList;
        }
    }
}
