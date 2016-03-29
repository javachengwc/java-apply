package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.exception.ArgsCountException;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.StringPool;

public final class TRIM extends Function {
    public static final String NAME = TRIM.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length < 1) {
            throw new ArgsCountException(NAME);
        }

        if (args[0] != null) {
            String value = DataUtil.getStringValue(args[0]);
            return value.trim();
        }

        return StringPool.EMPTY;
    }
}
