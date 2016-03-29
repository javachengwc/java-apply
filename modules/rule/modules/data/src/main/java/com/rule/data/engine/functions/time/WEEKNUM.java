package com.rule.data.engine.functions.time;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

import java.util.Calendar;

public final class WEEKNUM extends Function {
    public static final String NAME = WEEKNUM.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            Calendar calendar = Calendar.getInstance();

            int firstDayOfWeek = 1;
            if (args.length > 1) {
                firstDayOfWeek = DataUtil.getNumberValue(args[1]).intValue();
            }

            switch (firstDayOfWeek) {
                case 1:
                case 17:
                    calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                    break;
                case 2:
                case 11:
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    break;
                case 12:
                    calendar.setFirstDayOfWeek(Calendar.TUESDAY);
                    break;
                case 13:
                    calendar.setFirstDayOfWeek(Calendar.WEDNESDAY);
                    break;
                case 14:
                    calendar.setFirstDayOfWeek(Calendar.THURSDAY);
                    break;
                case 15:
                    calendar.setFirstDayOfWeek(Calendar.FRIDAY);
                    break;
                case 16:
                    calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                    break;
            }

            calendar.setTimeInMillis(DataUtil.getNumberValue(args[0]).longValue() * 1000);
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (month == 11 && week == 1) {
                calendar.set(year, month, day - 7);
                week = calendar.get(Calendar.WEEK_OF_YEAR) + 1;
            }

            return new Long(week);
        }

        throw new ArgsCountException(NAME);
    }
}
