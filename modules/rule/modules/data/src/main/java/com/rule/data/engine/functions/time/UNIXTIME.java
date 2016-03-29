package com.rule.data.engine.functions.time;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;
import com.rule.data.util.Type;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class UNIXTIME extends Function {
    public static final String NAME = UNIXTIME.class.getSimpleName();
    public static final Function now = Function.getFunction("NOW");

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        Date date = null;
        String format = null;

        if (args.length > 0) {
            Type t = DataUtil.getType(args[0]);

            if (t == Type.DATE) {
                date = (Date) args[0];
            } else {
                long time = DataUtil.getNumberValue(args[0]).longValue();
                date = new Date(time * NumberPool.LONG_1000);
            }
        } else {
            date = (Date) now.eval(calInfo, args);
        }

        if (args.length > 1 && DataUtil.getType(args[1]) == Type.STRING) {
            format = (String) args[1];

            try {
                return new SimpleDateFormat(format).format(date);
            } catch (Exception e) {
                //
            }
        }

        return date.getTime() / NumberPool.LONG_1000;
    }
}
