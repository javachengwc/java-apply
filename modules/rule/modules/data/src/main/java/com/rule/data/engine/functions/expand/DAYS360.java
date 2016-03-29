package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

import java.util.Calendar;
import java.util.Map;

public final class DAYS360 extends Function {

    public static final String NAME = DAYS360.class.getSimpleName();

    class __Key {
        Object start, stop;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (start != null ? !start.equals(key.start) : key.start != null)
                return false;
            if (stop != null ? !stop.equals(key.stop) : key.stop != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = start != null ? start.hashCode() : 0;
            result = 31 * result + (stop != null ? stop.hashCode() : 0);
            return result;
        }

        __Key(Object start, Object stop) {
            this.start = start;
            this.stop = stop;
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 1) {
            __Key key = new __Key(args[0], args[1]);
            final Map<Object, Object> cache = calInfo.getCache(NAME);
            Long days = (Long) cache.get(key);

            if (days == null) {
                Calendar start = Calendar.getInstance();
                start.setTimeInMillis(DataUtil.getNumberValue(args[0]).longValue() * 1000);

                Calendar stop = Calendar.getInstance();
                stop.setTimeInMillis(DataUtil.getNumberValue(args[1]).longValue() * 1000);
                days = NumberPool.LONG_0;

                boolean isBack = false;
                if (start.after(stop)) {
                    isBack = true;
                    Calendar tmp = start;
                    start = stop;
                    stop = tmp;
                }

                days += Math.abs(start.get(Calendar.YEAR) - stop.get(Calendar.YEAR)) * 360;
                days -= start.get(Calendar.MONTH) * 30 + start.get(Calendar.DAY_OF_MONTH);
                days += stop.get(Calendar.MONTH) * 30 + stop.get(Calendar.DAY_OF_MONTH);

                if (isBack) {
                    days = NumberPool.LONG_0 - days;
                }

                cache.put(key, days);
            }

            return days;
        }

        throw new ArgsCountException(NAME);
    }
}
