package com.rule.data.engine.functions.math;

import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.engine.functions.Function;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

public final class ABS extends Function {

    public  static final String NAME = ABS.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        Number number = DataUtil.getNumberValue(args[0]);

        if (number instanceof Double) {
            return Math.abs(number.doubleValue());
        } else {
            return Math.abs(number.longValue());
        }
    }
}
