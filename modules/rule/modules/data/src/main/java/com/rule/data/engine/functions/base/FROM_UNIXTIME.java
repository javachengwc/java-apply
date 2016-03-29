package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

import java.util.Date;

public final class FROM_UNIXTIME extends Function {

    public static final String NAME = FROM_UNIXTIME.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        long time = NumberPool.LONG_0;

        if (args.length > 0) {
            time = DataUtil.getNumberValue(args[0]).longValue();
        }

        return new Date(time * 1000);
    }
}
