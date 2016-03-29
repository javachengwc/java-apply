package com.rule.data.engine.functions.type;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.exception.CalculateException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.util.DataUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public final class DATE extends Function {

    public static final String NAME = DATE.class.getSimpleName();

    class __Key {
        Object year, month, day;

        __Key(Object year, Object month, Object day) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (day != null ? !day.equals(key.day) : key.day != null)
                return false;
            if (month != null ? !month.equals(key.month) : key.month != null)
                return false;
            if (year != null ? !year.equals(key.year) : key.year != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = year != null ? year.hashCode() : 0;
            result = 31 * result + (month != null ? month.hashCode() : 0);
            result = 31 * result + (day != null ? day.hashCode() : 0);
            return result;
        }
    }


    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 3) {
            throw new ArgsCountException(NAME);
        }

        final Map<Object, Object> cache = calInfo.getCache(NAME);
        __Key key = new __Key(args[0], args[1], args[2]);

        Date time = (Date) cache.get(key);

        if (time == null) {
            Calendar calendar = Calendar.getInstance();
            long year = DataUtil.getNumberValue(args[0]).longValue();
            long month = DataUtil.getNumberValue(args[1]).longValue() - 1;
            long day = DataUtil.getNumberValue(args[2]).longValue();

            calendar.set((int) year, (int) month, (int) day, 0, 0, 0);
            time = calendar.getTime();
            cache.put(key, time);
        }

        return time;
    }
}
