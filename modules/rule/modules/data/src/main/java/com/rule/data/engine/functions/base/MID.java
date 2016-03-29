package com.rule.data.engine.functions.base;

import com.rule.data.engine.functions.Function;
import com.rule.data.util.DataUtil;
import com.rule.data.exception.RengineException;
import com.rule.data.model.vo.CalInfo;
import com.rule.data.exception.CalculateException;
import com.rule.data.engine.excel.StringPool;

public final class MID extends Function {

    public static final String NAME = MID.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateException {
        if (args.length > 2) {
            final String str = DataUtil.getStringValue(args[0]);
            int start = DataUtil.getNumberValue(args[1]).intValue();
            if (start > 0) {
                start--;
            }

            if (start >= str.length()) {
                return StringPool.EMPTY;
            }

            int end = DataUtil.getNumberValue(args[2]).intValue() + start;

            if (end > str.length()) {
                end = str.length();
            }

            if (start < 0 || start > end) {
                return StringPool.EMPTY;
            }

            return str.substring(start, end);
        }

        return StringPool.EMPTY;
    }
}
