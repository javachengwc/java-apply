package com.rule.data.engine.functions.math;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.NumberPool;

public final class LN extends Function {

    public static final String NAME = LN.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            return Math.log(DataUtil.getNumberValue(args[0]).doubleValue());
        }

        return NumberPool.LONG_0;
    }
}
