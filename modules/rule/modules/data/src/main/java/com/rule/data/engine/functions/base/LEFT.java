package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.StringPool;

public final class LEFT extends Function {

    public static final String NAME = LEFT.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 0) {
            final String source = DataUtil.getStringValue(args[0]);
            int end = 1;
            if (args.length > 1) {
                end = DataUtil.getNumberValue(args[1]).intValue();
            }

            end = end > source.length() ? source.length() : end;
            end = end < 0 ? 0 : end;

            return source.substring(0, end);
        }

        return StringPool.EMPTY;
    }
}
