package com.rule.data.engine.functions.time;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

import java.util.Calendar;

public final class WEEKDAY extends Function {
    public static final String NAME = WEEKDAY.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DataUtil.getNumberValue(args[0]).longValue() * 1000);

            return new Long(calendar.get(Calendar.DAY_OF_WEEK));
        }

        return NumberPool.LONG_0;
    }
}
