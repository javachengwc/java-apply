package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.util.Type;

public final class ISNUMBER extends Function {

    public static final String NAME = ISNUMBER.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length > 0) {
            Type type = DataUtil.getType(args[0]);

            return type == Type.DOUBLE || type == Type.LONG;
        }

        throw new ArgsCountException(NAME);
    }
}
