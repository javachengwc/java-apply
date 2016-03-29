package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

public final class ISEVEN extends Function {

    public static final String NAME = ISEVEN.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            return DataUtil.getNumberValue(args[0]).intValue() % 2 == 0;
        }

        throw new ArgsCountException(NAME);
    }
}
