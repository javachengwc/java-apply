package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.StringPool;

public final class RIGHT extends Function {

    public static final String NAME = RIGHT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            final String source = DataUtil.getStringValue(args[0]);
            int len = 1;
            if (args.length > 1) {
                len = DataUtil.getNumberValue(args[1]).intValue();
            }

            int start = source.length() - len;
            start = start > source.length() ? source.length() : start;
            start = start < 0 ? 0 : start;

            return source.substring(start);
        }

        return StringPool.EMPTY;
    }
}
