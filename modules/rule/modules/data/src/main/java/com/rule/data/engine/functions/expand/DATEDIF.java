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

public final class DATEDIF extends Function {

    public static final String NAME = DATEDIF.class.getSimpleName();

    class __Key {
        Object start, stop, flag;

        __Key(Object start, Object stop, Object flag) {
            this.start = start;
            this.stop = stop;
            this.flag = flag;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (flag != null ? !flag.equals(key.flag) : key.flag != null)
                return false;
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
            result = 31 * result + (flag != null ? flag.hashCode() : 0);
            return result;
        }
    }

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 2) {
            __Key key = new __Key(args[0], args[1], args[2]);
            final Map<Object, Object> cache = calInfo.getCache(NAME);
            Long dif = (Long) cache.get(key);

            if (dif == null) {
                Calendar start = Calendar.getInstance();
                start.setTimeInMillis(DataUtil.getNumberValue(args[0]).longValue() * 1000);

                Calendar stop = Calendar.getInstance();
                stop.setTimeInMillis(DataUtil.getNumberValue(args[1]).longValue() * 1000);

                boolean isBack = false;
                if (start.after(stop)) {
                    isBack = true;
                    Calendar tmp = start;
                    start = stop;
                    stop = tmp;
                }

                String flag = DataUtil.getStringValue(args[2]).trim();
                int startY = start.get(Calendar.YEAR);
                int endY = stop.get(Calendar.YEAR);

                if (flag.equalsIgnoreCase("Y")) {
                    dif = (long) (endY - startY);
                } else if (flag.equalsIgnoreCase("M")) {
                    int startM = start.get(Calendar.MONTH);
                    int endM = stop.get(Calendar.MONTH);
                    dif = (long) ((endY - startY) * 12 + endM - startM);
                } else {
                    int startD = start.get(Calendar.DAY_OF_YEAR);
                    int endD = stop.get(Calendar.DAY_OF_YEAR);

                    dif = NumberPool.LONG_0;
                    for (int i = startY; i < endY; i++) {
                        dif += countDays(i);
                    }

                    dif += endD;
                    dif -= startD;
                }

                if (isBack) {
                    dif = NumberPool.LONG_0 - dif;
                }
                cache.put(key, dif);
            }

            return dif;
        }

        throw new ArgsCountException(NAME);
    }

    public static int countDays(int year) {
        if (((year % 100 == 0) && (year % 400 == 0))
                || ((year % 100 != 0) && (year % 4 == 0))) {
            return 366;
        } else {
            return 365;
        }
    }
}
