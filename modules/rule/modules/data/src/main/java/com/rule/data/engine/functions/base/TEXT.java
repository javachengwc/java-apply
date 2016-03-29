package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.engine.excel.StringPool;

public final class TEXT extends Function {
    public static final String NAME = TEXT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException {
        if (args.length > 0) {
            return DataUtil.getStringValue(args[0]);
        }

        return StringPool.EMPTY;
    }
}
