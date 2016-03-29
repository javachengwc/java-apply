package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;

public final class EXACT extends Function {

    public static final String NAME = EXACT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 2) {
            throw new ArgsCountException(NAME);
        }

        final String target = DataUtil.getStringValue(args[0]);
        final String source = DataUtil.getStringValue(args[1]);

        return target.equals(source);
    }
}
