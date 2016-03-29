package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.NumberPool;

public final class LEN extends Function {

    public static final String NAME = LEN.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length > 0) {
            return new Long((DataUtil.getStringValue(args[0]).length()));
        }

        return NumberPool.LONG_0;
    }
}
