package com.rule.data.engine.functions.expand;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.util.Type;


import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class EDATE extends Function {

    public static final String NAME = EDATE.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        __Key key = new __Key(args[0], args[1]);
        final Map<Object, Object> cache = calInfo.getCache(NAME);
        Date result = (Date) cache.get(key);
        if (result == null) {
            Date startDate = null;
            Type t = DataUtil.getType(args[0]);
            if (t == Type.DATE) {
                startDate = (Date) args[0];
            } else {
                long time = DataUtil.getNumberValue(args[0]).longValue();
                startDate = new Date(time * NumberPool.LONG_1000);
            }
            if (startDate == null) {
                throw new RengineException(calInfo.getServiceName(), NAME + "不能识别目标起始日期");
            }

            String months = DataUtil.getStringValue(args[1]);
            int diff = 0;
            try {
                diff = Integer.parseInt(months);
            } catch (Exception e) {
                throw new RengineException(calInfo.getServiceName(), NAME + "不能识别输入的指定月份,月份只能使整数");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);


            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            int newMonth = month + diff;
            calendar.set((int) year, (int) newMonth, (int) day, 0, 0, 0);
            if (day != calendar.get(Calendar.DAY_OF_MONTH)) {
                calendar.set(Calendar.DATE, 1);  //设置为当月的第一天
                calendar.add(calendar.DATE, -1);//往前推一天，获取上月的最后一天
            }
            result = calendar.getTime();
            cache.put(key, result);
        }
        return result;
    }


    class __Key {
        Object startDate, months;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof __Key)) return false;

            __Key key = (__Key) o;

            if (months != null ? !months.equals(key.months) : key.months != null)
                return false;
            if (startDate != null ? !startDate.equals(key.startDate) : key.startDate != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = startDate != null ? startDate.hashCode() : 0;
            result = 31 * result + (months != null ? months.hashCode() : 0);
            return result;
        }

        public __Key(Object startDate, Object months) {
            this.startDate = startDate;
            this.months = months;
        }
    }


}
