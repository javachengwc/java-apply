package com.rule.data.engine.functions.program;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;

public final class JSONOBJ extends Function {

    public static final String NAME = JSONOBJ.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        String str = DataUtil.getStringValue(args[0]);
        return "{" + str + "}";
    }
}
