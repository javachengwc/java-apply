package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

import java.math.BigDecimal;

public final class ROUND extends Function {

    public static final String NAME = ROUND.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        double number = NumberPool.DOUBLE_0;
        long digit = NumberPool.LONG_6;

        if (args[0] != null) {
            number = DataUtil.getNumberValue(args[0]).doubleValue();
        }
        if (args[1] != null) {
            digit = DataUtil.getNumberValue(args[1]).longValue();
        }

        return new BigDecimal(DataUtil.number2String(number)).setScale((int) digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
