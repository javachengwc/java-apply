package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;

public final class UPPER extends Function {
    public static final String NAME = UPPER.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        return DataUtil.getStringValue(args[0]).toUpperCase();
    }
}
